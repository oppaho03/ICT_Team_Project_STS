package com.ict.vita.controller.member;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.member.MemberDto;
import com.ict.vita.service.member.MemberJoinDto;
import com.ict.vita.service.member.MemberLoginDto;
import com.ict.vita.service.member.MemberService;
import com.ict.vita.util.Commons;
import com.ict.vita.util.Result;
import com.ict.vita.util.ResultUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
	//서비스 주입
	private final MemberService memberService;
	
	/**
	 * 회원가입
	 * @param joinDto(회원가입 요청 DTO) bindingResult(앞의 DTO객체의 유효성 검증 실패시 담기는 에러)
	 * @param bindingResult joinDto의 유효성 검증 실패시 에러가 담기는 객체
	 * @return ResponseEntity(사용자의 HttpRequest에 대한 HTTP 응답을 감싸는 클래스로 HttpStatus, HttpHeaders, HttpBody를 포함)
	 */
	@PostMapping("/member")
	public ResponseEntity<?> join(@RequestBody @Valid MemberJoinDto joinDto,BindingResult bindingResult){
		//<DTO 객체 필드의 유효성 검증 실패시>
		if(bindingResult.hasErrors()) {
			System.out.println("회원가입 유효성 검증 실패: "+bindingResult.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.joining(",")));
			
			StringBuffer buffer = new StringBuffer("");
			for(FieldError e : bindingResult.getFieldErrors()) {
				System.out.println(String.format("%s:%s", e.getField(),e.getDefaultMessage()));
				String errorMessage = String.format("%s:%s\r\n", e.getField(),e.getDefaultMessage());
				buffer.append(errorMessage);
			}
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultUtil.fail(buffer.toString())); 
		}
		//<DTO 객체 필드의 유효성 검증 성공시>
		//회원가입에 실패한 경우
		if(memberService.isExistsEmail(joinDto.getEmail())) 
			return ResponseEntity.status(HttpStatus.CONFLICT).body(ResultUtil.fail("이미 사용 중인 이메일입니다")); 
		//회원가입에 성공한 경우
		if(Commons.isNull(joinDto.getContact())) {
			MemberDto memberDto = memberService.join(joinDto);
			return ResponseEntity.status(HttpStatus.CREATED).body(ResultUtil.success(memberDto));
		}
		//회원가입에 실패한 경우
		if(memberService.isExistsContact(joinDto.getContact()))
			return ResponseEntity.status(HttpStatus.CONFLICT).body(ResultUtil.fail("이미 사용 중인 전화번호입니다"));
		//회원가입에 성공한 경우
		MemberDto memberDto = memberService.join(joinDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(ResultUtil.success(memberDto));
	}
	
	/**
	 * direct 로그인 처리
	 * @param loginDto 로그인 요청 객체
	 * @param bindingResult joinDto의 유효성 검증 실패시 에러가 담기는 객체
	 * @return ResponseEntity
	 */
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @Valid MemberLoginDto loginDto,BindingResult bindingResult){
		//[이메일과 비밀번호가 일치하는 회원 조회]
		MemberDto findedMember = memberService.validateLogin(loginDto);
		//<회원이 아닌 경우>
		if(findedMember == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail("아이디 또는 비밀번호 불일치"));
		}
		//<회원인 경우>
		//회원의 token필드(JWT) 설정
		findedMember.setToken("testToken"); // * 임시로 테스트용 토큰 넣어놓음!!!! *
		//회원 정보 수정
		MemberDto updatedMember = memberService.updateMember(findedMember);
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(updatedMember));
	}

	/**
	 * [로그아웃 처리]
	 * @param token 회원의 토큰값(JWT)
	 * @return ResponseEntity
	 */
	@PostMapping("/logout")
	//public ResponseEntity<?> logout(@RequestBody Map<String, String> token){
	public ResponseEntity<?> logout(@RequestHeader(name = "authorization") String token){
		System.out.println("token:"+token+"/");
		//[토큰값으로 회원 조회]
		//MemberDto findedMember = memberService.findMemberByToken(token.get("token"));
		MemberDto findedMember = memberService.findMemberByToken(token);
		//<찾은 회원이 존재하는 경우>
		if(findedMember != null) {
			findedMember.setToken(null);
			MemberDto updatedMember = memberService.updateMember(findedMember);
			System.out.println("[로그아웃]찾은 회원의 토큰값을 null 로 설정");
			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(null));
		}
		//<찾은 회원이 존재하지 않는 경우>
		// 토큰값과 일치하는 회원이 없더라도 로그아웃 처리는 완료됨
		System.out.println("[로그아웃]찾은 회원은 없지만 로그아웃 처리 완");
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(null));
	}
}
