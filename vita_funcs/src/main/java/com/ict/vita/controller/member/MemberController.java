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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.member.MemberDto;
import com.ict.vita.service.member.MemberJoinDto;
import com.ict.vita.service.member.MemberService;
import com.ict.vita.util.Commons;
import com.ict.vita.util.Result;

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
	 * @return ResponseEntity(사용자의 HttpRequest에 대한 응답 데이터를 포함하는 클래스로 HttpStatus, HttpHeaders, HttpBody를 포함)
	 */
	@PostMapping("/member")
	public ResponseEntity<Object> join(@RequestBody @Valid MemberJoinDto joinDto,BindingResult bindingResult){
		//DTO 객체 필드의 유효성 검증 실패시
		if(bindingResult.hasErrors()) {
			System.out.println("회원가입 유효성 검증 실패: "+bindingResult.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.joining(",")));
			Map<String, String> errors = new HashMap<>();
			bindingResult.getFieldErrors().stream().map(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage())).count();
			for(String errorField : errors.keySet()) {
				System.out.println(String.format("검증실패 필드:%s,에러메세지:%s", errorField,errors.get(errorField)));
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); //"필드 유효성 검증 실패"
		}
		//DTO 객체 필드의 유효성 검증 성공시
		//회원가입이 불가능한 경우
		if(memberService.isExistsEmail(joinDto.getEmail())) 
			return ResponseEntity.status(HttpStatus.CONFLICT).body("이메일 이미 존재");
		//회원가입이 가능한 경우
		if(Commons.isNull(joinDto.getContact())) {
			memberService.join(joinDto);
			return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
		}
		//회원가입이 불가능한 경우
		if(memberService.isExistsContact(joinDto.getContact()))
			return ResponseEntity.status(HttpStatus.CONFLICT).body("전화번호 이미 존재");
		//회원가입이 가능한 경우
		memberService.join(joinDto);
		return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
	}

}
