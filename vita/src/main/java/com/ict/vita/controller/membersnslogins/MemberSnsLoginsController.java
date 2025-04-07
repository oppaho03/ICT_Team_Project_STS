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
import com.ict.vita.service.member.MemberTempJoinDto;
import com.ict.vita.service.membermeta.MemberMetaDto;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
	@Operation( summary = "SNS 로그인", description = "SNS 로그인 API" )
	@ApiResponses({
		@ApiResponse( 
			responseCode = "200-SNS 로그인 성공",
			description = "SUCCESS",
			content = @Content(	
				schema = @Schema(implementation = MemberSnsResponseDto.class),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":{\"id\":24,\"member\":{\"id\":101,\"email\":\"user01@gmail.com\",\"name\":\"박윤성\",\"nickname\":\"user01\",\"birth\":null,\"gender\":\""
				)
			) 
		)
	})
	@PostMapping("/api/sns/login")
	public ResponseEntity<?> snsLogin(@RequestBody MemberSnsRequestDto snsReqDto){
	
		System.out.println(String.format("[sns]이메일:%s, 인증토큰:%s", snsReqDto.getEmail(), snsReqDto.getAccess_token()));
		
		//<sns회원 테이블에서 조회>
		MemberSnsDto snsDto = memberSnsLoginsService.getSnsMemberByEmail(snsReqDto.getEmail().trim());
		//<회원 테이블에서 이메일 조회>
		MemberDto findedMember = memberService.findMemberByEmail(snsReqDto.getEmail().trim());
		
		//회원 메타 정보들
		List<MemberMetaResponseDto> metas = null;

		MemberSnsDto updatedSnsDto = snsDto;
		MemberDto updatedDto = findedMember;
		
		//서버로 넘어온 옵션값들
		String name = null;
		if(!Commons.isNull(snsReqDto.getName())) 
			name = snsReqDto.getName();
		
		String picture = null;
		if(!Commons.isNull(snsReqDto.getPicture())) 
			picture = snsReqDto.getPicture();
		
		
		//[sns회원 존재시]
		if(snsDto != null) {	

			snsDto.setLogin_modified_at(LocalDateTime.now());
			snsDto.setProvider_id(snsReqDto.getProvider_id()); 
			snsDto.setAccess_token(snsReqDto.getAccess_token().trim());
			
			updatedSnsDto = memberSnsLoginsService.update(snsDto);
			
			//회원 테이블에 존재
			if(findedMember != null && findedMember.getStatus() == 1) { 

				String token = null;
				Map<String, Object> claims = new HashMap<>();
				claims.put( "email" , findedMember.getEmail());
				claims.put( "role" , findedMember.getRole());
				token = jwtutil.CreateToken(findedMember.getId().toString(), claims);	
				
				findedMember.setStatus(1); //status를 1로(가입상태)
				findedMember.setToken(token);
				updatedDto = memberService.updateMember(findedMember);
				
				metas = memberMetaService.findAll(updatedDto).stream().map(meta -> MemberMetaResponseDto.toResponseDto(meta)).toList();

				//사진 업데이트
				for(MemberMetaDto meta : memberMetaService.findAll(updatedDto)) {
					if(meta != null && meta.getMeta_key().equals("picture")) {
						ObjectMetaRequestDto metaReq = ObjectMetaRequestDto.builder()
														.id(meta.getMemberDto().getId())
														.meta_key("picture")
														.meta_value(picture)
														.build();
						memberMetaService.save(metaReq);
					}
				}
			}
			
			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( MemberSnsResponseDto.toResponseDto(updatedSnsDto,updatedDto,metas) ));
		}
		
		//[sns회원 미존재시]
		//회원 테이블에 없을 때
		if(findedMember == null) {
			//회원 테이블에 저장

			MemberTempJoinDto tempJoinDto = MemberTempJoinDto.builder()
												.email(snsReqDto.getEmail().trim())
												.name(name)
												.role(Commons.ROLE_USER)
												.password(EncryptAES256.encrypt(snsReqDto.getProvider_id()))
												.nickname( snsReqDto.getEmail().trim().split("@")[0] )
												.created_at(LocalDateTime.now())
												.updated_at(LocalDateTime.now())
												.status(9)
												.build();

			MemberDto tempDto = memberService.tempJoin(tempJoinDto);
			
			MemberDto joinedDto = memberService.join(tempDto);
			

			//토큰 설정
			String token = null;
			Map<String, Object> claims = new HashMap<>();
			claims.put( "email" , joinedDto.getEmail());
			claims.put( "role" , joinedDto.getRole());
			token = jwtutil.CreateToken(joinedDto.getId().toString(), claims);
			joinedDto.setToken(token);
			
			updatedDto = memberService.updateMember(joinedDto);
			
			//회원 메타 테이블에 사진 정보 저장
			ObjectMetaRequestDto metaInfo = ObjectMetaRequestDto.builder()
												.id(updatedDto.getId())
												.meta_key("picture")
												.meta_value(picture)
												.build();
			
			memberMetaService.save(metaInfo);
		
			metas = memberMetaService.findAll(updatedDto).stream().map(meta -> MemberMetaResponseDto.toResponseDto(meta)).toList();
			
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
				
				updatedDto = memberService.updateMember(findedMember);
				
				metas = memberMetaService.findAll(updatedDto).stream().map(meta -> MemberMetaResponseDto.toResponseDto(meta)).toList();
			
				//사진 업데이트
				for(MemberMetaDto meta : memberMetaService.findAll(updatedDto)) {
					if(meta != null && meta.getMeta_key().equals("picture")) {
						ObjectMetaRequestDto metaReq = ObjectMetaRequestDto.builder()
														.id(meta.getMemberDto().getId())
														.meta_key("picture")
														.meta_value(picture)
														.build();
						memberMetaService.save(metaReq);
					}
				}
			}
		}
		
		//sns회원 테이블에 저장
		MemberSnsDto newSnsDto = MemberSnsDto.builder()
								.member( updatedDto )
								.login_id( snsReqDto.getEmail().trim() )
								.access_token(snsReqDto.getAccess_token().trim())
								.refresh_token(null)
								.status(1) //status를 1로
								.login_created_at(LocalDateTime.now())
								.login_modified_at(LocalDateTime.now())
								.provider(snsReqDto.getProvider()) 
								.provider_id(snsReqDto.getProvider_id()) 
								.build();
		
		updatedSnsDto = memberSnsLoginsService.save(newSnsDto);
		
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( MemberSnsResponseDto.toResponseDto( updatedSnsDto,updatedDto,metas ) ));
		
	}
	
}
