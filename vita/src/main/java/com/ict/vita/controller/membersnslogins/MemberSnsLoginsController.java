package com.ict.vita.controller.membersnslogins;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.member.MemberDto;
import com.ict.vita.service.member.MemberResponseDto;
import com.ict.vita.service.member.MemberService;
import com.ict.vita.service.membermeta.MemberMetaResponseDto;
import com.ict.vita.service.membermeta.MemberMetaService;
import com.ict.vita.service.membersnslogins.MemberSnsDto;
import com.ict.vita.service.membersnslogins.MemberSnsRequestDto;
import com.ict.vita.service.membersnslogins.MemberSnsResponseDto;
import com.ict.vita.service.others.ObjectMetaRequestDto;
import com.ict.vita.service.membersnslogins.MemberSnsLoginsService;
import com.ict.vita.util.Commons;
import com.ict.vita.util.EncryptAES256;
import com.ict.vita.util.JwtUtil;
import com.ict.vita.util.ResultUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class MemberSnsLoginsController {
	//서비스 주입
	private final MemberSnsLoginsService memberSnsLoginsService;
	private final MemberService memberService;
	private final MemberMetaService memberMetaService;
	
	private final JwtUtil jwtutil;
	
	/**
	 * [SNS 로그인]
	 * *필수값: email, access_token, provider_id, provider + *옵션값: name, picture
	 * @param infos SNS로그인요청 정보
	 * @return ResponseEntity
	 */
	@PostMapping("/api/sns/login")
	public ResponseEntity<?> snsLogin(@RequestBody MemberSnsRequestDto snsReqDto){
	
		System.out.println(String.format("[sns]이메일:%s, 인증토큰:%s", snsReqDto.getEmail(), snsReqDto.getAccess_token()));
		
		//<sns회원 테이블에서 조회>
		MemberSnsDto snsDto = memberSnsLoginsService.getSnsMemberByEmail(snsReqDto.getEmail().trim());
		//<회원 테이블에서 이메일 조회>
		MemberDto findedMember = memberService.findMemberByEmail(snsReqDto.getEmail().trim());
		
		//회원 메타 정보들
		List<MemberMetaResponseDto> metas = null;
		
		//[sns회원 존재시]
		if(snsDto != null) {
			
			snsDto.setLogin_modified_at(LocalDateTime.now());
			snsDto.setProvider_id(snsReqDto.getProvider_id()); 
			snsDto.setAccess_token(snsReqDto.getAccess_token().trim());
			
			memberSnsLoginsService.update(snsDto);
			
			//회원 테이블에 존재
			if(findedMember.getStatus() != 1) { 
				String token = null;
				Map<String, Object> claims = new HashMap<>();
				claims.put( "email" , findedMember.getEmail());
				claims.put( "role" , findedMember.getRole());
				token = jwtutil.CreateToken(findedMember.getId().toString(), claims);
				
				metas = memberMetaService.findAll(findedMember).stream().map(meta -> MemberMetaResponseDto.toResponseDto(meta)).toList();
				
				findedMember.setStatus(1); //status를 1로(가입상태)
				findedMember.setToken(token);
				memberService.updateMember(findedMember);
				
			}
			
//			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( snsDto ));
			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( MemberSnsResponseDto.toResponseDto(snsDto,findedMember,metas) ));
		}
		
		//[sns회원 미존재시]
		//회원 테이블에 없을 때
		if(findedMember == null) {
			//서버로 넘어온 옵션값들
			String name = null;
			if(!Commons.isNull(snsReqDto.getName())) 
				name = snsReqDto.getName();
			
			String picture = null;
			if(!Commons.isNull(snsReqDto.getPicture())) 
				picture = snsReqDto.getPicture();
			
			//회원 테이블에 저장
			findedMember = MemberDto.builder()
									.email(snsReqDto.getEmail().trim())
									.name(name)
									.password(EncryptAES256.encrypt(snsReqDto.getProvider_id()) )
									.nickname( snsReqDto.getEmail().trim().split("@")[0] )
									.created_at(LocalDateTime.now())
									.updated_at(LocalDateTime.now())
									.status(1) //status를 1로(가입상태)
									.build();
			
			findedMember = memberService.join(findedMember);
			
			//토큰 설정
			String token = null;
			Map<String, Object> claims = new HashMap<>();
			claims.put( "email" , findedMember.getEmail());
			claims.put( "role" , findedMember.getRole());
			token = jwtutil.CreateToken(findedMember.getId().toString(), claims);
			findedMember.setToken(token);
			memberService.updateMember(findedMember);
			
			//회원 메타 테이블에 사진 정보 저장
			ObjectMetaRequestDto metaInfo = ObjectMetaRequestDto.builder()
												.id(findedMember.getId())
												.meta_key("picture")
												.meta_value(picture)
												.build();
			memberMetaService.save(metaInfo);
			
			metas = memberMetaService.findAll(findedMember).stream().map(meta -> MemberMetaResponseDto.toResponseDto(meta)).toList();
			
		}
		
		else { //회원 테이블에 있을 때
			if(findedMember.getStatus() != 1) {
				//토큰 설정
				String token = null;
				Map<String, Object> claims = new HashMap<>();
				claims.put( "email" , findedMember.getEmail());
				claims.put( "role" , findedMember.getRole());
				token = jwtutil.CreateToken(findedMember.getId().toString(), claims);
				findedMember.setToken(token);
				
				findedMember.setStatus(1); //status를 1로(가입상태)
				findedMember.setPassword(EncryptAES256.encrypt(snsReqDto.getProvider_id()));
				
				findedMember = memberService.updateMember(findedMember);
				
				metas = memberMetaService.findAll(findedMember).stream().map(meta -> MemberMetaResponseDto.toResponseDto(meta)).toList();
			}
		}
		
		//sns회원 테이블에 저장
		snsDto = MemberSnsDto.builder()
								.member( findedMember )
								.login_id( snsReqDto.getEmail().trim() )
								.access_token(snsReqDto.getAccess_token().trim())
								.refresh_token(null)
								.status(1) //status를 1로
								.login_created_at(LocalDateTime.now())
								.login_modified_at(LocalDateTime.now())
								.provider(snsReqDto.getProvider()) 
								.provider_id(snsReqDto.getProvider_id()) 
								.build();
		
		memberSnsLoginsService.save(snsDto);
//		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(snsDto));
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( MemberSnsResponseDto.toResponseDto( snsDto,findedMember,metas ) ));
		
	}
	
}
