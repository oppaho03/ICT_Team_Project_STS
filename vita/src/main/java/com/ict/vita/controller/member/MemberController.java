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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.member.MemberDto;
import com.ict.vita.service.member.MemberJoinDto;
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
	 * @return ResponseEntity(사용자의 HttpRequest에 대한 응답 데이터를 포함하는 클래스로 HttpStatus, HttpHeaders, HttpBody를 포함)
	 */
	@PostMapping("/member")
	public ResponseEntity<Object> join(@RequestBody @Valid MemberJoinDto joinDto,BindingResult bindingResult){
		//<DTO 객체 필드의 유효성 검증 실패시>
		if(bindingResult.hasErrors()) {
			System.out.println("회원가입 유효성 검증 실패: "+bindingResult.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.joining(",")));
			
			StringBuffer buffer = new StringBuffer("");
			for(FieldError e : bindingResult.getFieldErrors()) {
				System.out.println(String.format("%s:%s", e.getField(),e.getDefaultMessage()));
				String errorMessage = String.format("%s:%s", e.getField(),e.getDefaultMessage());
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

}
