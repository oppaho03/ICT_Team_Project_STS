package com.ict.vita.util;

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
import com.ict.vita.service.member.MemberJoinDto;
import com.ict.vita.service.member.MemberLoginDto;
import com.ict.vita.service.member.MemberResponseDto;
import com.ict.vita.service.member.MemberService;
import com.ict.vita.service.member.MemberTempJoinDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin
//[이메일 인증코드 생성 관련 테스트 컨트롤러]
public class AuthCodeTestController {
	
	private final MemberService memberService;
	private final MessageSource messageSource;
	private final JwtUtil jwtutil;
	
	//[이메일 인증코드 생성 + 임시 회원가입 테스트 메서드]
	@PostMapping("/test/authcode")
	public ResponseEntity<?> getAuthCode(@RequestBody Map<String, String> parameters){
		System.out.println("*** email:"+parameters.get("email"));
		
		//코드 생성
		String authCode = AuthCode.generateAuthCode();
		
		MemberTempJoinDto joinDto = MemberTempJoinDto.builder()
									.email(parameters.get("email").trim())
									.password(authCode)
									.role("USER")
									.nickname("authCodeTest") // ***임시로 해놓음
									.created_at(LocalDateTime.now())
									.updated_at(LocalDateTime.now())
									.status(9) //임시가입
									.build();
		
		if(memberService.isExistsEmail(joinDto.getEmail())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(ResultUtil.fail(messageSource.getMessage("user.invalid_value_mail_exist", null, new Locale("ko")))); 
		}
		
		//임시 회원가입
		MemberDto tempJoinedMember = memberService.tempJoin(joinDto);
		
		Map<String, Object> map = new HashMap<>();
		map.put("member", tempJoinedMember);
		map.put("authCode", authCode);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(ResultUtil.success(map));
	}
	
	//[direct 회원가입 테스트 메서드]
	@PostMapping("/test/join")
	public ResponseEntity<?> join(@RequestBody MemberJoinDto joinDto){
		System.out.println("direct회원가입 address:"+joinDto.getAddress());
		System.out.println("direct회원가입 birth:"+joinDto.getBirth().toString());
		
		MemberDto findedMember = memberService.findMemberByEmail(joinDto.getEmail()); //임시 가입된 회원
		
		//<이미 회원가입된 경우 - status가 1>
		if(findedMember.getStatus() == 1) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(ResultUtil.fail(messageSource.getMessage("user.join_fail_already_user", null, new Locale("ko")))); 
		}
		//<이메일 인증이 안 된 경우>
		if(joinDto.getIsEmailAuth() != 1) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail( messageSource.getMessage("user.join_fail_not_verified", null, new Locale("ko")))); 
		}
	
		//<이메일 인증이 된 경우>
		//회원가입에 실패한 경우 - 이미 사용중인 전화번호 존재시
		if( ! Commons.isNull(joinDto.getContact()) && memberService.isExistsContact(joinDto.getContact()))
			return ResponseEntity.status(HttpStatus.CONFLICT).body(ResultUtil.fail(messageSource.getMessage("user.invalid_value_contact_exist", null, new Locale("ko")) ));
		
		//회원가입에 성공한 경우
		//DB에 저장한 회원을 회원가입할 때 입력한 정보로 설정
		try {
			findedMember.setPassword(joinDto.getPassword());
			findedMember.setRole(joinDto.getRole());
			findedMember.setName(joinDto.getName());
			findedMember.setNickname(joinDto.getNickname());
			findedMember.setBirth(joinDto.getBirth());
			findedMember.setGender(joinDto.getGender());
			findedMember.setContact(joinDto.getContact());
			findedMember.setAddress(joinDto.getAddress());
			findedMember.setCreated_at(joinDto.getCreated_at());
			findedMember.setUpdated_at(LocalDateTime.now());
		}
		catch(Exception e) {
			System.out.println("회원가입 실패:"+e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.fail( messageSource.getMessage("user.join_fail", null, new Locale("ko")) ));
		} 
		
		MemberDto memberDto = memberService.join(findedMember);
		
		return memberDto != null ? ResponseEntity.status(HttpStatus.CREATED).body(ResultUtil.success(MemberResponseDto.toDto(memberDto))) :
					ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.fail(messageSource.getMessage("user.join_fail", null, new Locale("ko"))));
	}
	
	//[다이렉트 로그인 테스트 메서드]
	@PostMapping("/test/login")
	public ResponseEntity<?> login(@RequestBody MemberLoginDto loginDto){
		
		MemberDto findedMember = memberService.validateLogin(loginDto);
		
		if(findedMember == null || ( findedMember != null && findedMember.getStatus() != 1 ) ) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail(messageSource.getMessage("user.login_fail", null, new Locale("ko")) ));
		}
		
		try {
			Map<String, Object> claims = new HashMap<>();
			claims.put( "email" , findedMember.getEmail());
			claims.put( "role" , findedMember.getRole());
			
			String token = jwtutil.CreateToken(findedMember.getId().toString(), claims);			
			findedMember.setToken(token); 
			System.out.println("[로그인]토큰:"+token);
		}
		catch( Exception ex ) {
			return ResponseEntity
					.status(HttpStatus.UNAUTHORIZED)
					.body(ResultUtil.fail(messageSource.getMessage("user.login_fail_token", null, new Locale("ko"))));
		}
		
		//회원 정보 수정
		MemberDto updatedMember = memberService.updateMember(findedMember);
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(MemberResponseDto.toDto(updatedMember)));
	}
	
}
