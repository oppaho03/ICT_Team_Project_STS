package com.ict.vita.controller.member;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
@CrossOrigin
public class MemberController {
	//서비스 주입
	private final MemberService memberService;
	
	private final JwtUtil jwtutil; // Constructor Injection, JwtUtil
	
	/**
	 * [모든 회원 조회] - 회원의 status에 상관없이 모든 회원 조회
	 * @param token 로그인한 회원 토큰값
	 * @return ResponseEntity
	 */
	@Operation( summary = "모든 회원 조회", description = "모든 회원 조회 API" )
	@ApiResponses({
		@ApiResponse( 
			responseCode = "200-모든 회원 조회 성공",
			description = "SUCCESS",
			content = @Content(	
				array = @ArraySchema(
						schema = @Schema(implementation = MemberDto.class)
				),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":[{\"id\":29,\"email\":\"oppaho123@gmail.com\",\"password\":\"pwd\",\"role\":\"USER\",\"name\":\"홍길동\",\"nickname\":\"oppaho123\",\"birth\":\"2025-02-27\",\"gender\":\"M\",\"contact\":null,\"address\":null,\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVVNFUiIsImVtYWlsIjoieWVzbmlja0BuYXZlci5jb20iLCJzdWIiOiI0MiIsImlhdCI6MTc0MTM0MjI5OCwiZXhwIjoxNzQxMzQzMTk4fQ.0tjhRQMEjNruFwoI8g6F1QISOcjF1qIZ77ktq_R4fL0\",\"created_at\":\"2025-02-27T20:28:06.48291\",\"updated_at\":\"2025-02-27T20:28:06.475567\",\"status\":1},{\"id\":35,\"email\":\"abc@naver.com\",\"password\":\"TEMPORARY\",\"role\":\"USER\",\"name\":null,\"nickname\":\"TEMPORARY\",\"birth\":null,\"gender\":\""
				)
			) 
		),
		@ApiResponse( 
			responseCode = "401-모든 회원 조회 실패",
			description = "FAIL", 
			content = @Content(					
				examples = @ExampleObject(
					value = "{\"success\":0,\"response\":{\"message\":\"관리자만모든회원조회가능합니다\"}}"
				)
			) 
		),
		@ApiResponse( 
				responseCode = "404-모든 회원 조회 실패",
				description = "FAIL", 
				content = @Content(					
					examples = @ExampleObject(
						value = "{\"success\":0,\"response\":{\"message\":\"해당하는회원이존재하지않습니다\"}}"
					)
				) 
			)
	})
	@GetMapping("/members")
	public ResponseEntity<?> getAllMembers(@Parameter(description = "로그인한 회원 토큰값") @RequestHeader("Authorization") String token){
		MemberDto findedMember = Commons.findMemberByToken(token, memberService);
		//관리자인 경우
		if( findedMember != null && findedMember.getRole().equals(Commons.ROLE_ADMINISTRATOR) ) {
			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(memberService.getAllMembers()));
		}
		
		if(findedMember == null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResultUtil.fail("해당하는 회원이 존재하지 않습니다"));
		
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail("관리자만 모든 회원 조회 가능합니다"));
		
	}
	
	/**
	 * [모든 일반회원(USER) 조회]
	 * @param token 로그인한 회원 토큰값
	 * @return ResponseEntity
	 */
	@Operation( summary = "모든 일반회원(USER) 조회", description = "모든 일반회원(USER) 조회 API" )
	@ApiResponses({
		@ApiResponse( 
			responseCode = "200-모든 일반회원(USER) 조회 성공",
			description = "SUCCESS",
			content = @Content(	
				array = @ArraySchema(
						schema = @Schema(implementation = MemberDto.class)
				),
				examples = @ExampleObject(
					value = ""
				)
			) 
		),
		@ApiResponse( 
			responseCode = "401-모든 일반회원(USER) 조회 실패",
			description = "FAIL", 
			content = @Content(					
				examples = @ExampleObject(
					value = "{\"success\":0,\"response\":{\"message\":\"관리자만모든회원조회가능합니다\"}}"
				)
			) 
		),
		@ApiResponse( 
				responseCode = "404-모든 일반회원(USER) 조회 실패",
				description = "FAIL", 
				content = @Content(					
					examples = @ExampleObject(
						value = "{\"success\":0,\"response\":{\"message\":\"해당하는회원이존재하지않습니다\"}}"
					)
				) 
			)
	})
	@GetMapping("/members/users")
	public ResponseEntity<?> getUserMembers(@Parameter(description = "로그인한 회원 토큰값") @RequestHeader("Authorization") String token){
		MemberDto findedMember = Commons.findMemberByToken(token, memberService);
		//관리자인 경우
		if( findedMember != null && findedMember.getRole().equals(Commons.ROLE_ADMINISTRATOR) ) {
			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(memberService.findMemberByRole(Commons.ROLE_USER)));
		}
		
		if(findedMember == null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResultUtil.fail("해당하는 회원이 존재하지 않습니다"));
		
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail("관리자만 모든 회원 조회 가능합니다"));
		
	}
	
	/**
	 * [상태(status)별 회원 조회]
	 * @param token 로그인한 회원 토큰값
	 * @param status 회원의 status값
	 * @return ResponseEntity
	 */
	@Operation( summary = "상태별 회원 조회", description = "상태별 회원 조회 API" )
	@ApiResponses({
		@ApiResponse( 
			responseCode = "200-상태별 회원 조회 성공",
			description = "SUCCESS",
			content = @Content(	
				array = @ArraySchema(
						schema = @Schema(implementation = MemberDto.class)
				),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":[{\"id\":29,\"email\":\"oppaho123@gmail.com\",\"password\":\"pwd\",\"role\":\"USER\",\"name\":\"홍길동\",\"nickname\":\"oppaho123\",\"birth\":\"2025-02-27\",\"gender\":\"M\",\"contact\":null,\"address\":null,\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVVNFUiIsImVtYWlsIjoieWVzbmlja0BuYXZlci5jb20iLCJzdWIiOiI0MiIsImlhdCI6MTc0MTM0MjI5OCwiZXhwIjoxNzQxMzQzMTk4fQ.0tjhRQMEjNruFwoI8g6F1QISOcjF1qIZ77ktq_R4fL0\",\"created_at\":\"2025-02-27T20:28:06.48291\",\"updated_at\":\"2025-02-27T20:28:06.475567\",\"status\":1},{\"id\":37,\"email\":\"abab@naver.com\",\"password\":\"pwdabab\",\"role\":\"USER\",\"name\":\"홍길동\",\"nickname\":\"TEMPORARY\",\"birth\":\"2025-03-01\",\"gender\":\"F\",\"contact\":null,\"address\":null,\"token\":\"testtoken\",\"created_at\":\"2025-03-07T18:50:04.665174\",\"updated_at\":\"2025-03-07T18:50:04.665174\",\"status\":1},{\"id\":40,\"email\":\"a1@naver.com\",\"password\":\"pwdabab\",\"role\":\"USER\",\"name\":\"홍길동\",\"nickname\":\"1닉넴\",\"birth\":\"2025-03-07\",\"gender\":\"F\",\"contact\":null,\"address\":null,\"token\":null,\"created_at\":\"2025-03-07T18:53:58.713134\",\"updated_at\":\"2025-03-07T18:53:58.713134\",\"status\":1},{\"id\":46,\"email\":\"admin@naver.com\",\"password\":\"adminpwd\",\"role\":\"ADMINISTRATOR\",\"name\":\"관리자1\",\"nickname\":\"관리자nick\",\"birth\":null,\"gender\":\"M\",\"contact\":null,\"address\":null,\"token\":\"adminToken\",\"created_at\":\"2025-03-10T18:55:57.835\",\"updated_at\":\"2025-03-10T18:55:57.835\",\"status\":1},{\"id\":26,\"email\":\"oppaho1@gmail.com\",\"password\":\"pwd\",\"role\":\"USER\",\"name\":\"홍길동\",\"nickname\":\"oppaho1\",\"birth\":\"2025-02-27\",\"gender\":\"\u0001\",\"contact\":null,\"address\":null,\"token\":null,\"created_at\":\"2025-02-27T20:07:07.161029\",\"updated_at\":\"2025-02-27T20:07:07.131923\",\"status\":1},{\"id\":27,\"email\":\"oppaho12@gmail.com\",\"password\":\"pwd\",\"role\":\"USER\",\"name\":\"홍길동\",\"nickname\":\"oppaho12\",\"birth\":\"2025-02-27\",\"gender\":\"M\",\"contact\":null,\"address\":null,\"token\":null,\"created_at\":\"2025-02-27T20:08:45.332339\",\"updated_at\":\"2025-02-27T20:08:45.32307\",\"status\":1}]}}"
				)
			) 
		),
		@ApiResponse( 
			responseCode = "401-상태별 회원 조회 실패",
			description = "FAIL", 
			content = @Content(					
				examples = @ExampleObject(
					value = "{\"success\":0,\"response\":{\"message\":\"관리자만상태별회원조회가능합니다\"}}"
				)
			) 
		),
		@ApiResponse( 
				responseCode = "404-상태별 회원 조회 실패",
				description = "FAIL", 
				content = @Content(					
					examples = @ExampleObject(
						value = "{\"success\":0,\"response\":{\"message\":\"해당하는회원이존재하지않습니다\"}}"
					)
				) 
			)
	})
	@GetMapping("/members/status/{status}")
	public ResponseEntity<?> getMembersByStatus(
			@Parameter(description = "로그인한 회원 토큰값") @RequestHeader("Authorization") String token,
			@Parameter(description = "회원의 status") @PathVariable("status") Long status){
		MemberDto findedMember = Commons.findMemberByToken(token, memberService);
		//관리자인 경우
		if( findedMember != null && findedMember.getRole().equals(Commons.ROLE_ADMINISTRATOR) ) {
			List<MemberDto> findedMembers = memberService.findMemberByStatus(status);
			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(findedMembers));
		}
		
		if(findedMember == null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResultUtil.fail("해당하는 회원이 존재하지 않습니다"));
		
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail("관리자만 상태별 회원 조회 가능합니다"));
		
	}
	
	/**
	 * [역할별 회원 조회]
	 * @param token 로그인한 회원 토큰값
	 * @param role 회원의 role값
	 * @return ResponseEntity
	 */
	@Operation( summary = "역할별 회원 조회", description = "역할별 회원 조회 API" )
	@ApiResponses({
		@ApiResponse( 
			responseCode = "200-역할별 회원 조회 성공",
			description = "SUCCESS",
			content = @Content(	
				array = @ArraySchema(
						schema = @Schema(implementation = MemberDto.class)
				),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":[{\"id\":46,\"email\":\"admin@naver.com\",\"password\":\"adminpwd\",\"role\":\"ADMINISTRATOR\",\"name\":\"관리자1\",\"nickname\":\"관리자nick\",\"birth\":null,\"gender\":\"M\",\"contact\":null,\"address\":null,\"token\":\"adminToken\",\"created_at\":\"2025-03-10T18:55:57.835\",\"updated_at\":\"2025-03-10T18:55:57.835\",\"status\":1}]}}"
				) 
			)
		),
		@ApiResponse( 
			responseCode = "401-역할별 회원 조회 실패",
			description = "FAIL", 
			content = @Content(					
				examples = @ExampleObject(
					value = "{\"success\":0,\"response\":{\"message\":\"관리자만역할별회원조회가능합니다\"}}"
				)
			) 
		),
		@ApiResponse( 
				responseCode = "404-역할별 회원 조회 실패",
				description = "FAIL", 
				content = @Content(					
					examples = @ExampleObject(
						value = "{\"success\":0,\"response\":{\"message\":\"해당하는회원이존재하지않습니다\"}}"
					)
				) 
			)
	})
	@GetMapping("/members/role/{role}")
	public ResponseEntity<?> getMembersByRole(
			@Parameter(description = "로그인한 회원 토큰값") @RequestHeader("Authorization") String token,
			@Parameter(description = "회원의 role") @PathVariable("role") String role){
		MemberDto findedMember = Commons.findMemberByToken(token, memberService);
		//관리자인 경우
		if( findedMember != null && findedMember.getRole().equals(Commons.ROLE_ADMINISTRATOR) ) {
			List<MemberDto> findedMembers = memberService.findMemberByRole(role);
			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(findedMembers));
		}
		
		if(findedMember == null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResultUtil.fail("해당하는 회원이 존재하지 않습니다"));
		
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail("관리자만 역할별 회원 조회 가능합니다"));
	}
	
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
				schema = @Schema(implementation = MemberDto.class),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":{\"id\":41,\"email\":\"nonick@naver.com\",\"password\":\"pwd\",\"role\":\"USER\",\"name\":\"노닉네임\",\"nickname\":\"TEMPORARY\",\"birth\":\"2025-03-01\",\"gender\":\"F\",\"contact\":\"123\",\"address\":null,\"token\":null,\"created_at\":\"2025-03-07T18:57:13.2894058\",\"updated_at\":\"2025-03-07T18:57:13.2894058\",\"status\":9}}}"
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
		),
		@ApiResponse( 
				responseCode = "409-임시 회원가입 실패(이미 사용중인 이메일인 경우)",
				description = "FAIL", 
				content = @Content(					
					examples = @ExampleObject(
						value = "{\"success\":0,\"response\":{\"message\":\"이미사용중인이메일입니다\"}}"
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
		//임시 회원가입 실패 - 이미 사용중인 아이디 입력시
		if(memberService.isExistsEmail(tempJoinDto.getEmail())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(ResultUtil.fail("이미 사용 중인 이메일입니다")); 
		}
		//임시 회원가입 처리
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
			),
		@ApiResponse( 
				responseCode = "409-회원가입 실패",
				description = "FAIL(이미 가입된 회원인 경우)", 
				content = @Content(					
					examples = @ExampleObject(
						value = "{\"success\":0,\"response\":{\"message\":\"이미가입된회원입니다\"}}" 
					)
				) 
			),
		@ApiResponse( 
				responseCode = "403-회원가입 실패",
				description = "FAIL(이메일 인증 안 된 경우)", 
				content = @Content(					
					examples = @ExampleObject(
						value = "{\"success\":0,\"response\":{\"message\":\"이메일인증이안됐습니다\"}}"
					)
				) 
			)
	})
	@PostMapping("/members")
	public ResponseEntity<?> join(
			@Parameter(description = "회원가입 요청 객체") @RequestBody @Valid MemberJoinDto joinDto,
			BindingResult bindingResult){
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
		MemberDto findedMember = memberService.findMemberByEmail(joinDto.getEmail()); //임시 가입된 회원
		System.out.println("===============회원: "+findedMember.getStatus());
		//<이미 회원가입된 경우 - status가 1>
		if(findedMember.getStatus() == 1) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(ResultUtil.fail("이미 가입된 회원입니다")); 
		}
		//<이메일 인증이 안 된 경우>
		if(joinDto.getIsEmailAuth() != 1) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResultUtil.fail("이메일 인증이 안 됐습니다")); 
		}
		//<이메일 인증이 된 경우>
		//회원가입에 성공한 경우
		if(Commons.isNull(joinDto.getContact())) {
			//DB에 저장한 회원을 회원가입할때 입력한 정보로 설정
			findedMember.setPassword(joinDto.getPassword());
			findedMember.setName(joinDto.getName());
			findedMember.setNickname(joinDto.getNickname());
			findedMember.setBirth(joinDto.getBirth());
			findedMember.setGender(joinDto.getGender());
			findedMember.setContact(joinDto.getContact());
			findedMember.setAddress(joinDto.getAddress());
			findedMember.setCreated_at(LocalDateTime.now());
			findedMember.setUpdated_at(LocalDateTime.now());
			
			MemberDto memberDto = memberService.join(findedMember);
			return ResponseEntity.status(HttpStatus.CREATED).body(ResultUtil.success(memberDto));
		}
		//회원가입에 실패한 경우 - 이미 사용중인 전화번호 존재시
		if(memberService.isExistsContact(joinDto.getContact()))
			return ResponseEntity.status(HttpStatus.CONFLICT).body(ResultUtil.fail("이미 사용 중인 전화번호입니다"));
		//회원가입에 성공한 경우
		//DB에 저장한 회원을 회원가입할때 입력한 정보로 설정
		findedMember.setPassword(joinDto.getPassword());
		findedMember.setName(joinDto.getName());
		findedMember.setNickname(joinDto.getNickname());
		findedMember.setBirth(joinDto.getBirth());
		findedMember.setGender(joinDto.getGender());
		findedMember.setContact(joinDto.getContact());
		findedMember.setAddress(joinDto.getAddress());
		findedMember.setCreated_at(LocalDateTime.now());
		findedMember.setUpdated_at(LocalDateTime.now());
		
		MemberDto memberDto = memberService.join(findedMember);
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
	public ResponseEntity<?> login(
			@Parameter(description = "로그인 요청 객체") @RequestBody @Valid MemberLoginDto loginDto,
			BindingResult bindingResult){
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
		//토큰값으로 회원 조회
		if(Commons.findMemberByToken(token, memberService) != null) {
			MemberDto findedMember = Commons.findMemberByToken(token, memberService);
			//변경한 회원정보로 기존 회원 dto 수정 (회원정보 수정은 비밀번호,이름,닉네임,전화번호,주소 만 수정 가능)
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
