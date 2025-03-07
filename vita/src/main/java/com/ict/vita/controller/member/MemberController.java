package com.ict.vita.controller.member;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.chatanswer.ChatAnswerDto;
import com.ict.vita.service.member.MemberDto;
import com.ict.vita.service.member.MemberJoinDto;
import com.ict.vita.service.member.MemberLoginDto;
import com.ict.vita.service.member.MemberService;
import com.ict.vita.service.member.MemberUpdateDto;
import com.ict.vita.util.Commons;
import com.ict.vita.util.JwtUtil;
import com.ict.vita.util.Result;
import com.ict.vita.util.ResultUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
	//서비스 주입
	private final MemberService memberService;
	
	private final JwtUtil jwtutil; // Constructor Injection, JwtUtil
	
	
	/**
	 * [임시 회원가입] - 임시 회원가입시 회원의 status는 9(대기)
	 * @param joinDto 회원가입 임시 요청 DTO
	 * @return ResponseEntity
	 */
	@Operation( summary = "임시 회원가입", description = "임시 회원가입 API" )
	@ApiResponses({
		@ApiResponse( 
			responseCode = "201-임시 회원가입 성공",
			description = "SUCCESS",
			content = @Content(	
				schema = @Schema(implementation = MemberJoinDto.class),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":{\"id\":36,\"email\":\"abcde@naver.com\",\"password\":\"TEMPORARY\",\"role\":\"USER\",\"name\":null,\"nickname\":\"TEMPORARY\",\"birth\":null,\"gender\":\""
				)
			) 
		),
		@ApiResponse( 
			responseCode = "400-임시 회원가입 실패",
			description = "FAIL", 
			content = @Content(					
				examples = @ExampleObject(
					value = "{\"success\":0,\"response\":{\"message\":\"이메일을입력하세요\"}}"
				)
			) 
		)
	})
	@PostMapping("/temp_members")
	public ResponseEntity<?> tempJoin(@Parameter(description = "임시 회원가입 요청 객체") @RequestBody MemberJoinDto tempJoinDto){
		//<회원이 이메일을 입력하지 않은 경우>
		if(Commons.isNull(tempJoinDto.getEmail())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultUtil.fail("이메일을 입력하세요"));
		}
		//<회원이 이메일을 입력한 경우>
		MemberDto tempJoinedMember = memberService.tempJoin(tempJoinDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(ResultUtil.success(tempJoinedMember));
	}
	
	/**
	 * [회원가입] - 최종 회원가입 처리 -> 최종 회원가입시 회원의 status는 1(일반)
	 * @param joinDto(회원가입 요청 DTO) 
	 * @param bindingResult joinDto의 유효성 검증 실패시 에러가 담기는 객체(앞의 DTO객체의 유효성 검증 실패시 담기는 에러)
	 * @return ResponseEntity(사용자의 HttpRequest에 대한 HTTP 응답을 감싸는 클래스로 HttpStatus, HttpHeaders, HttpBody를 포함)
	 */
	@Operation( summary = "회원가입", description = "회원가입 API" )
	@ApiResponses({
		@ApiResponse( 
			responseCode = "400-회원가입 실패",
			description = "FAIL(유효성 검증 실패)",
			content = @Content(	
				examples = @ExampleObject(
					value = "{ \"success\": 0, \"response\": { \"message\": \"email:올바른 이메일 주소를 입력하세요.\"  } }" 
				)
			) 
		),
		@ApiResponse( 
			responseCode = "201-회원가입 성공",
			description = "SUCCESS(사용중인 이메일이 아니면서 전화번호 미입력시)",
			content = @Content(	
				schema = @Schema(implementation = MemberDto.class),
				examples = @ExampleObject(
					value = "{ \"success\": 1, \"response\": { \"data\": {\"id\": 1, \"email\": \"test@example.com\", \"password\": \"hashed_password\", \"role\": \"USER\", \"name\": \"홍길동\", \"nickname\": \"gildong123\", \"birth\": \"1990-01-01\", \"gender\": \"M\", \"contact\": \"000-0000-0000\", \"address\": \"서울시 강남구 역삼동\", \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\", \"created_at\": \"2025-03-03T05:46:09.470\", \"updated_at\": \"2025-03-03T05:46:09.470\", \"status\": 1 } } }" 
				)
			) 
		),
		@ApiResponse( 
				responseCode = "201-회원가입 성공",
				description = "SUCCESS(입력한 이메일과 전화번호가 사용중이지 않은 경우)",
				content = @Content(	
					schema = @Schema(implementation = MemberDto.class),
					examples = @ExampleObject(
						value = "{ \"success\": 1, \"response\": { \"data\": {\"id\": 1, \"email\": \"test@example.com\", \"password\": \"hashed_password\", \"role\": \"USER\", \"name\": \"홍길동\", \"nickname\": \"gildong123\", \"birth\": \"1990-01-01\", \"gender\": \"M\", \"contact\": \"000-0000-0000\", \"address\": \"서울시 강남구 역삼동\", \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\", \"created_at\": \"2025-03-03T05:46:09.470\", \"updated_at\": \"2025-03-03T05:46:09.470\", \"status\": 1 } } }" 
					)
				) 
			),
		@ApiResponse( 
			responseCode = "409-회원가입 실패",
			description = "FAIL(이미 이메일이 사용중인 경우)", 
			content = @Content(					
				examples = @ExampleObject(
					value = "{ \"success\": 0, \"response\": { \"message\": \"이미 사용 중인 이메일입니다\"  } }" 
				)
			) 
		),
		@ApiResponse( 
				responseCode = "409-회원가입 실패",
				description = "FAIL(이미 전화번호가 사용중인 경우)", 
				content = @Content(					
					examples = @ExampleObject(
						value = "{ \"success\": 0, \"response\": { \"message\": \"이미 사용 중인 전화번호입니다\"  } }" 
					)
				) 
			)
	})
	@PostMapping("/members")
	public ResponseEntity<?> join(@Parameter(description = "회원가입 요청 객체") @RequestBody @Valid MemberJoinDto joinDto,BindingResult bindingResult){
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
		//입력한 이메일로 회원 조회
		MemberDto findedMember = memberService.findMemberByEmail(joinDto.getEmail());
		//<이미 회원가입된 경우 - status가 1>
		if(findedMember.getStatus() == 1) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(ResultUtil.fail("이미 가입된 회원입니다")); 
		}
		//<회원가입이 안 되어 있는 경우 - status가 1이 아님>
		//회원가입에 실패한 경우
		if(memberService.isExistsEmail(joinDto.getEmail())) 
			return ResponseEntity.status(HttpStatus.CONFLICT).body(ResultUtil.fail("이미 사용 중인 이메일입니다")); 
		//회원가입에 성공한 경우
		if(Commons.isNull(joinDto.getContact())) {
			//DB에 저장한 회원을 회원가입할때 입력한 정보로 설정
			findedMember.setPassword(joinDto.getPassword());
			findedMember.setName(joinDto.getName());
			//findedMember.setNickname();
			findedMember.setBirth(joinDto.getBirth());
			findedMember.setGender(joinDto.getGender());
			findedMember.setContact(joinDto.getContact());
			findedMember.setAddress(joinDto.getAddress());
			
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
	@Operation( summary = "로그인 (direct)", description = "다이렉트 로그인 API" )
	@ApiResponses({
		@ApiResponse( 
			responseCode = "200-로그인 성공",
			description = "SUCCESS",
			content = @Content(	
				schema = @Schema(implementation = MemberDto.class),
				examples = @ExampleObject(
					value = "{ \"success\": 1, \"response\": { \"data\": {\"id\": 1, \"email\": \"test@example.com\", \"password\": \"hashed_password\", \"role\": \"USER\", \"name\": \"홍길동\", \"nickname\": \"gildong123\", \"birth\": \"1990-01-01\", \"gender\": \"M\", \"contact\": \"000-0000-0000\", \"address\": \"서울시 강남구 역삼동\", \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\", \"created_at\": \"2025-03-03T05:46:09.470\", \"updated_at\": \"2025-03-03T05:46:09.470\", \"status\": 1 } } }" 
				)
			) 
		),
		@ApiResponse( 
			responseCode = "401-로그인 실패",
			description = "FAIL", 
			content = @Content(					
				examples = @ExampleObject(
					value = "{ \"success\": 0, \"response\": { \"message\": \"아이디 또는 비밀번호 불일치.\"  } }" 
				)
			) 
		)
	})
	@PostMapping("/login")
	public ResponseEntity<?> login(@Parameter(description = "로그인 요청 객체") @RequestBody @Valid MemberLoginDto loginDto,BindingResult bindingResult){
		//[이메일과 비밀번호가 일치하는 회원 조회]
		MemberDto findedMember = memberService.validateLogin(loginDto);
		//<회원이 아닌 경우>
		if(findedMember == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail("아이디 또는 비밀번호 불일치"));
		}
		
		/*
		 * <회원인 경우>
		 * 회원의 token필드(JWT) 설정
		 * id, email -> JWT token 생성 및 업데이트 
		 * - jwtutil.ParseToken(String token) 을 사용해 Map<String, Object> 반환 
		 * - 예 { sub=1, iat=1741051044, exp=1741051944 } 
		 */	
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
					.body(ResultUtil.fail("토큰 생성 실패"));
		}
		
		//회원 정보 수정
		MemberDto updatedMember = memberService.updateMember(findedMember);
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(updatedMember));
	}

	/**
	 * [로그아웃 처리]
	 * @param token 회원의 토큰값(JWT)
	 * @return ResponseEntity
	 */
	@Operation( summary = "로그아웃", description = "로그아웃 API" )
	@ApiResponses({
		@ApiResponse( 
			responseCode = "200-로그아웃 성공",
			description = "SUCCESS", 
			content = @Content(	
				examples = @ExampleObject(
					value = "{ \"success\": 1, \"response\": { \"data\": null  } }" 
				)
			) 
		)
	})
	@PostMapping("/logout")
	public ResponseEntity<?> logout(@Parameter(description = "로그인한 회원 토큰값") @RequestHeader(name = "authorization") String token){
		//<찾은 회원이 존재하는 경우>
		if(Commons.findMemberByToken(token, memberService) != null) {
			MemberDto findedMember = Commons.findMemberByToken(token, memberService);
			findedMember.setToken(null);
			MemberDto updatedMember = memberService.updateMember(findedMember);
			System.out.println("[로그아웃]찾은 회원의 토큰값을 null 로 설정");
			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(null));
		}
		//<찾은 회원이 존재하지 않는 경우>
		System.out.println("[로그아웃]찾은 회원은 없지만 로그아웃 처리 완");
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(null));
		
	}
	
	/**
	 * [회원정보 수정]
	 * @param updateDto 수정 정보가 들어있는 수정 요청 객체
	 * @param token 로그인한 회원의 토큰값
	 * @return ResponseEntity
	 */
	@Operation( summary = "수정", description = "수정 API" )
	@ApiResponses({
		@ApiResponse( 
			responseCode = "200-수정 성공",
			description = "SUCCESS", 
			content = @Content(	
				schema = @Schema(implementation = MemberDto.class),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":{\"id\":31,\"email\":\"hahaha12@naver.com\",\"password\":\"newPwd\",\"role\":\"USER\",\"name\":\"개명홍길동\",\"nickname\":\"홍홍\",\"birth\":\"2025-02-27\",\"gender\":\"M\",\"contact\":\"0202\",\"address\":\"\",\"token\":\"tokenString\",\"created_at\":\"2025-02-28T20:16:05.570502\",\"updated_at\":\"2025-02-28T20:16:05.549038\",\"status\":9}}}" 
				)
			) 
		),
		@ApiResponse( 
			responseCode = "401-수정 실패",
			description = "FAIL", 
			content = @Content(					
				examples = @ExampleObject(
					value = "{\"success\":0,\"response\":{\"message\":\"유효하지않은토큰입니다\"}}"
				)
			) 
		)
	})
	@PutMapping("/members")
	public ResponseEntity<?> update(@Parameter(description = "수정 요청 객체") @RequestBody MemberUpdateDto updateDto,@RequestHeader(name = "authorization") String token){
		//<찾은 회원이 존재하는 경우>
		if(Commons.findMemberByToken(token, memberService) != null) {
			MemberDto findedMember = Commons.findMemberByToken(token, memberService);
			//변경한 회원정보로 기존 회원 dto 수정
			findedMember.setPassword(updateDto.getPassword());
			findedMember.setName(updateDto.getName());
			findedMember.setNickname(updateDto.getNickname());
			findedMember.setContact(updateDto.getContact());
			findedMember.setAddress(updateDto.getAddress());
			//변경된 회원 정보로 회원 수정
			MemberDto updatedMember = memberService.updateMember(findedMember);
			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(updatedMember));
		}
		//<찾은 회원이 존재하지 않는 경우>
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail("유효하지 않은 토큰입니다"));
		
	}
}
