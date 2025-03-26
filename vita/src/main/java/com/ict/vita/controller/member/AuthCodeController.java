package com.ict.vita.controller.member;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.ict.vita.service.member.MemberDto;
import com.ict.vita.service.member.MemberService;
import com.ict.vita.service.member.MemberTempJoinDto;
import com.ict.vita.util.AuthUtil;
import com.ict.vita.util.Commons;
import com.ict.vita.util.ResultUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@CrossOrigin
//[이메일 인증코드 관련 컨트롤러]
public class AuthCodeController {
	//비동기 작업을 위한 WebClient 객체 생성
	private WebClient webClient = WebClient.builder().build();
	
	//서비스 주입
	private final MemberService memberService;
	private final MessageSource messageSource;
	
	/**
	 * [파이썬 서버로 이메일 인증 코드 전송하는 메서드] - 비동기
	 * @param url 요청url
	 * @param authInfo 이메일 인증코드가 포함된 Map객체
	 */
	private void sendAuthCodeToPython(String url, Map<String, String> authInfo) {
		webClient.post()
			.uri(url)
			.header("Content-Type", "application/json")
			.bodyValue(authInfo) //요청바디에 추가
			.retrieve() //응답 가져오기 위한 메서드
			.toBodilessEntity() //응답바디 필요없을때
			.subscribe(); //비동기 처리
	}
	
	/**
	 * [이메일 인증코드 발급 및 임시 회원가입 처리 메서드]
	 * @param parameters 리액트에서 스프링 서버로 넘어온 정보 ( email )
	 * @return 
	 */
	@PostMapping
	public ResponseEntity<?> initiateEmailVerification(@RequestBody Map<String, String> parameters){
		
		String email = parameters.get("email"); //입력한 이메일
		
		//<회원이 이메일을 입력하지 않은 경우>
		if(Commons.isNull(email)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail(messageSource.getMessage("user.invalid_value_mail", null, new Locale("ko")) ));
		}
		
		//임시 회원가입 실패 - 이미 사용중인 아이디 입력시
		if(memberService.isExistsEmail(email.trim())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(ResultUtil.fail(messageSource.getMessage("user.invalid_value_mail_exist", null, new Locale("ko")))); 
		}	
		
		//이메일 인증 코드 생성
		String authCode = AuthUtil.generateEmailAuthCode();
		
		MemberTempJoinDto tempJoinDto = MemberTempJoinDto.builder()
				.email(email.trim())
				.password(authCode) //비밀번호를 인증코드로 설정
				.role(Commons.ROLE_USER)
				.build();
		
		//임시 회원가입 처리
		MemberDto tempJoinedMember = memberService.tempJoin(tempJoinDto);
		
		//파이썬 서버 url 설정
		String pythonServerUrl = ""; //****설정해야함!!
		
		//이메일 인증코드 전달할 객체 생성
		Map<String, String> authInfo = new HashMap<>();
		authInfo.put("authCode", authCode);
		
		//파이썬으로 인증 코드 전달
		sendAuthCodeToPython(pythonServerUrl,authInfo);
		
		//리액트로 이메일 인증 코드 반환
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(authInfo));
	}
	
	/**
	 * [이메일 인증코드 검증 메서드]
	 * @param parameters 리액트에서 스프링 서버로 넘어온 정보 ( email, code )
	 * @return
	 */
	@GetMapping
	public ResponseEntity<?> verifyEmailAuthCode(@RequestBody Map<String, String> parameters){
		//서버로 넘어온 정보
		String email = parameters.get("email");
		String authCode = parameters.get("code");
		
		//이메일로 회원 조회
		MemberDto findedMember = memberService.findMemberByEmail(email);
		
		//회원 미존재
		if(findedMember == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultUtil.fail( messageSource.getMessage("member.notfound", null, new Locale("ko")) ));
		}
		//이미 가입된 회원인 경우
		if(findedMember.getStatus() == 1) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultUtil.fail( messageSource.getMessage("user.invalid_value_mail_exist", null, new Locale("ko")) ));
		}
		
		//이메일 인증코드 검증
		//실패시
		if(authCode != findedMember.getPassword()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultUtil.fail( messageSource.getMessage("auth.invalid", null, new Locale("ko"))));
		}
		//성공시
		Map<String, Boolean> result = new HashMap<>();
		result.put("isAuth", true);
		
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( result ));
		
	}
}
