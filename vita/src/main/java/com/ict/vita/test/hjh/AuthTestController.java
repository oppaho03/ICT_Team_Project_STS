package com.ict.vita.test.hjh;

import java.time.LocalDateTime;
import java.util.HashMap;
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
import com.ict.vita.util.AuthUtil;
import com.ict.vita.util.Commons;
import com.ict.vita.util.ResultUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin
//[이메일 인증코드 생성 관련 테스트 컨트롤러]
public class AuthTestController {
	
	private final MemberService memberService;
	private final MessageSource messageSource;
	
	//[이메일 인증코드 생성 + 임시 회원가입 테스트 메서드]
	@PostMapping("/test/authcode")
	public ResponseEntity<?> getAuthCode(@RequestBody Map<String, String> parameters){
		
		//<회원이 이메일을 입력하지 않은 경우>
		if(Commons.isNull(parameters.get("email"))) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail(messageSource.getMessage("user.invalid_value_mail", null, new Locale("ko")) ));
		}
		//임시 회원가입 실패 - 이미 사용중인 아이디 입력시
		if(memberService.isExistsEmail( parameters.get("email").trim() )) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(ResultUtil.fail(messageSource.getMessage("user.invalid_value_mail_exist", null, new Locale("ko")))); 
		}
		
		//코드 생성
		String authCode = AuthUtil.generateEmailAuthCode();
		
		MemberTempJoinDto joinDto = MemberTempJoinDto.builder()
									.email(parameters.get("email").trim())
									.password(authCode)
									.role(Commons.ROLE_USER)
									//.nickname("authCodeTest") // ***임시로 해놓음
									.build();
		
		//임시 회원가입
		MemberDto tempJoinedMember = memberService.tempJoin(joinDto);
		
		Map<String, Object> map = new HashMap<>();
		map.put("member", tempJoinedMember);
		map.put("authCode", authCode);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(ResultUtil.success(map));
	}
	
}
