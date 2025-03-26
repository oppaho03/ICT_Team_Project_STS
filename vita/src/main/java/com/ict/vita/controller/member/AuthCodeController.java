package com.ict.vita.controller.member;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.member.MemberDto;
import com.ict.vita.service.member.MemberService;
import com.ict.vita.service.member.MemberTempJoinDto;
import com.ict.vita.util.AuthCode;
import com.ict.vita.util.Commons;
import com.ict.vita.util.ResultUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin
//[이메일 인증코드 관련 컨트롤러]
public class AuthCodeController {
	//서비스 주입
	private final MemberService memberService;
	
	private final MessageSource messageSource;
	
	@PostMapping("/api/authcode")
	public ResponseEntity<?> generateAuthCode(@RequestBody Map<String, String> parameters){
		//코드 생성
		String authCode = AuthCode.generateAuthCode();
		
		MemberTempJoinDto joinDto = MemberTempJoinDto.builder()
				.email(parameters.get("email").trim())
				.password(authCode)
				.role(Commons.ROLE_USER)
				.nickname("authCodeTest") // ***임시로 해놓음
				.created_at(LocalDateTime.now())
				.updated_at(LocalDateTime.now())
				.status(9) //임시가입
				.build();

		if(memberService.isExistsEmail(joinDto.getEmail())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(ResultUtil.fail(messageSource.getMessage("user.invalid_value_mail_exist", null, new Locale("ko")))); 
		}
		
		//임시 회원가입 처리
		MemberDto tempJoinedMember = memberService.tempJoin(joinDto);
		
		//파이썬으로 코드 전달
		sendAuthCodeToPython(authCode);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	//파이썬 서버로 코드 전송하는 메서드
	private void sendAuthCodeToPython(String authCode) {
		//파이썬 서버 url 설정
		String pythonServerUrl = "";
		
		
	}
	
}
