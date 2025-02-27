package com.ict.vita.controller.member;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.member.MemberDto;
import com.ict.vita.service.member.MemberService;

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
	 * @return ResponseEntity(사용자의 HttpRequest에 대한 응답 데이터를 포함하는 클래스로 HttpStatus, HttpHeaders, HttpBody를 포함
	 */
	@PostMapping("/member")
	public ResponseEntity<Object> join(@RequestBody @Valid MemberDto member,BindingResult bindingResult){
		//DTO 객체 필드의 유효성 검증 실패시
		if(bindingResult.hasErrors()) {
			System.out.println("회원가입 유효성 검증 실패: "+bindingResult.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("필드 유효성 검증 실패");
		}
		//DTO 객체 필드의 유효성 검증 성공시
		//서비스 호출
		//회원가입이 불가능한 경우(이메일이나 전화번호가 이미 존재하는 경우)
		
		if(memberService.isExistsEmail(member.getEmail()) || (member.getContact() != null && memberService.isExistsContact(member.getContact()))) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("회원가입 불가");
		}
		//회원가입 가능한 경우
		//서비스 호출
		memberService.signup(member);
		return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
		
	}
	
	/**
	 * @param member 회원정보를 담은 객체
	 * @param errors
	 * @return
	 */
	@PostMapping("member")
	public ResponseEntity<Object> join(@RequestBody MemberDto member){
		//회원가입 테스트!!!!
		return null;
	}
}
