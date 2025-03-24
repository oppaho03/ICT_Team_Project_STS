package com.ict.vita.controller.membersnslogins;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.member.MemberDto;
import com.ict.vita.service.member.MemberService;
import com.ict.vita.service.membersnslogins.MemberSnsLoginsDto;
import com.ict.vita.service.membersnslogins.MemberSnsLoginsRequestDto;
import com.ict.vita.service.membersnslogins.MemberSnsLoginsService;
import com.ict.vita.util.Commons;
import com.ict.vita.util.EncryptAES256;
import com.ict.vita.util.ResultUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class MemberSnsLoginsController {
	//서비스 주입
	private final MemberSnsLoginsService memberSnsLoginsService;
	private final MemberService memberService;
	
	/**
	 * [SNS 로그인]
	 * @param infos SNS로그인요청 정보(email, token 들어있음)
	 * @return ResponseEntity
	 */
	@PostMapping("/api/sns_login")
	public ResponseEntity<?> snsLogin(@RequestBody MemberSnsLoginsRequestDto snsReqDto){
	
		System.out.println(String.format("[sns]이메일:%s, 인증토큰:%s", snsReqDto.getEmail(), snsReqDto.getToken()));
		
		//<sns회원 테이블에서 조회>
		MemberSnsLoginsDto snsDto = memberSnsLoginsService.getSnsMemberByEmail(snsReqDto.getEmail().trim());
		//<회원 테이블에서 이메일 조회>
		MemberDto findedMember = memberService.findMemberByEmail(snsReqDto.getEmail().trim());
		
		//[sns회원 존재시]
		if(snsDto != null) {
			
			snsDto.setLogin_modified_at(LocalDateTime.now());
			snsDto.setProvider_id("aaaaab"); //****임시
			snsDto.setAccess_token(snsReqDto.getToken().trim());
			
			memberSnsLoginsService.update(snsDto);
			
			//회원 테이블에 존재
			if(findedMember.getStatus() != 1) { 
				findedMember.setStatus(1); //status를 1로(가입상태)
				memberService.updateMember(findedMember);
			}
			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(snsDto));
		}
		
		//[sns회원 미존재시]
		//회원 테이블에 없을 때
		if(findedMember == null) {
			//회원 테이블에 저장
			findedMember = MemberDto.builder()
									.email(snsReqDto.getEmail().trim())
									.password(EncryptAES256.encrypt(snsReqDto.getToken()) )
									.nickname( snsReqDto.getEmail().trim().split("@")[0] )
									.created_at(LocalDateTime.now())
									.updated_at(LocalDateTime.now())
									.status(1) //status를 1로(가입상태)
									.build();
			findedMember = memberService.join(findedMember);
		}
		
		else { //회원 테이블에 있을 때
			if(findedMember.getStatus() != 1) {
				findedMember.setStatus(1); //status를 1로(가입상태)
				findedMember.setPassword(EncryptAES256.encrypt(snsReqDto.getToken()));
				findedMember = memberService.updateMember(findedMember);
			}
		}
		
		//sns회원 테이블에 저장
		snsDto = MemberSnsLoginsDto.builder()
								.memberDto(findedMember)
								.login_id( snsReqDto.getEmail().trim() )
								.access_token(snsReqDto.getToken().trim())
								.status(1) //status를 1로
								.login_created_at(LocalDateTime.now())
								.login_modified_at(LocalDateTime.now())
								.provider(Commons.TEMPORARY) //****값 임시로 넣어놓음
								.provider_id("providerrr") //****유니크한 값
								.build();
		memberSnsLoginsService.save(snsDto);
		
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(snsDto));
		
	}
	
}
