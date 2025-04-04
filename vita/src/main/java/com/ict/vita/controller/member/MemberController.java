package com.ict.vita.controller.member;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.anc.AncService;
import com.ict.vita.service.chatanswer.ChatAnswerDto;
import com.ict.vita.service.member.MemberDto;
import com.ict.vita.service.member.MemberJoinDto;
import com.ict.vita.service.member.MemberLoginDto;
import com.ict.vita.service.member.MemberResponseDto;
import com.ict.vita.service.member.MemberService;
import com.ict.vita.service.member.MemberTempJoinDto;
import com.ict.vita.service.member.MemberUpdateDto;
import com.ict.vita.service.membermeta.MemberMetaResponseDto;
import com.ict.vita.service.membermeta.MemberMetaService;
import com.ict.vita.service.postcategoryrelationships.PostCategoryRelationshipsDto;
import com.ict.vita.service.postcategoryrelationships.PostCategoryRelationshipsService;
import com.ict.vita.service.postmeta.PostMetaDto;
import com.ict.vita.service.postmeta.PostMetaResponseDto;
import com.ict.vita.service.postmeta.PostMetaService;
import com.ict.vita.service.posts.PostsDto;
import com.ict.vita.service.posts.PostsResponseDto;
import com.ict.vita.service.posts.PostsService;
import com.ict.vita.service.terms.TermsResponseDto;
import com.ict.vita.util.EncryptAES256;
import com.ict.vita.util.AuthUtil;
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
	private final PostsService postsService;
	private final PostCategoryRelationshipsService pcrService;
	private final PostMetaService postMetaService;
	private final MemberMetaService memberMetaService;
	
	private final JwtUtil jwtutil; // Constructor Injection, JwtUtil
	private final MessageSource messageSource;
	
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
						schema = @Schema(implementation = MemberResponseDto.class)
				),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":[{\"id\":100,\"email\":\"baekjongwon33@naver.com\",\"name\":\"박구글\",\"nickname\":\"baekjongwon33\",\"birth\":null,\"gender\":\"\",\"contact\":null,\"address\":null,\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVVNFUiIsImVtYWlsIjoiYmFla2pvbmd3b24zM0BuYXZlci5jb20iLCJzdWIiOiIxMDAiLCJpYXQiOjE3NDM3MzM3NzEsImV4cCI6MTc0MzczNDY3MX0.cX0l1lnpk-7JlNRkizLtTMUmVhyTDq77Kc0IN-17jZk\",\"created_at\":\"2025-04-04T11:19:55.298957\",\"updated_at\":\"2025-04-04T11:19:55.298957\",\"status\":1,\"meta\":[{\"meta_id\":9,\"meta_key\":\"picture\",\"meta_value\":\"http://www.google.com\"}]},{\"id\":99,\"email\":\"baekjongwon22111@naver.com\",\"name\":null,\"nickname\":\"baekjongwon22111\",\"birth\":null,\"gender\":\"\",\"contact\":null,\"address\":null,\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVVNFUiIsImVtYWlsIjoiYmFla2pvbmd3b24yMjExMUBuYXZlci5jb20iLCJzdWIiOiI5OSIsImlhdCI6MTc0MzczMzE1MywiZXhwIjoxNzQzNzM0MDUzfQ.SYISAFjNXZCiWlY_o4mLbunyGvE8Rqtt6N_0g1BuCwg\",\"created_at\":\"2025-04-04T11:08:40.071055\",\"updated_at\":\"2025-04-04T11:08:40.071055\",\"status\":1,\"meta\":[{\"meta_id\":8,\"meta_key\":\"picture\",\"meta_value\":null}]}}"
				)
			) 
		),
		@ApiResponse( 
			responseCode = "401-모든 회원 조회 실패",
			description = "FAIL", 
			content = @Content(					
				examples = @ExampleObject(
					value = "{\"success\":0,\"response\":{\"message\":\"접근권한이없습니다.\"}}"
				)
			) 
		),
		@ApiResponse( 
				responseCode = "403-모든 회원 조회 실패",
				description = "FAIL", 
				content = @Content(					
					examples = @ExampleObject(
						value = "{\"success\":0,\"response\":{\"message\":\"이작업을수행할권한이없습니다.\"}}"
					)
				) 
			)
	})
	@GetMapping("/members")
	public ResponseEntity<?> getAllMembers(@Parameter(description = "로그인한 회원 토큰값") @RequestHeader("Authorization") String token){
		
		MemberDto findedMember = Commons.findMemberByToken(token, memberService);
		
		//접근권한이 없는 경우
		if ( findedMember == null )
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail( messageSource.getMessage("user.invalid_token", null, new Locale("ko")) ));
		else if ( ! findedMember.getRole().equals(Commons.ROLE_ADMINISTRATOR) ) 
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResultUtil.fail( messageSource.getMessage("user.invalid_role", null, new Locale("ko")) ));
		
		//관리자인 경우
		List<MemberResponseDto> members = memberService.getAllMembers()
											.stream()
											.map(dto -> 
												MemberResponseDto.toDto( 
														dto, 
														memberMetaService.findAll(dto).stream().map( meta -> MemberMetaResponseDto.toResponseDto(meta) ).toList() ))
											.collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(members));
	}
	
	/**
	 * [본인 정보 조회]
	 * @param token 로그인한 회원 토큰값
	 * @return ResponseEntity
	 */
	@Operation( summary = "본인 정보 조회", description = "본인 정보 조회 API" )
	@ApiResponses({
		@ApiResponse( 
			responseCode = "200-본인 정보 조회 성공",
			description = "SUCCESS",
			content = @Content(	
				schema = @Schema(implementation = MemberResponseDto.class),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":{\"id\":101,\"email\":\"user01@gmail.com\",\"name\":\"박윤성\",\"nickname\":\"user01\",\"birth\":null,\"gender\":\"\",\"contact\":null,\"address\":null,\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVVNFUiIsImVtYWlsIjoidXNlcjAxQGdtYWlsLmNvbSIsInN1YiI6IjEwMSIsImlhdCI6MTc0MzczNDY5MSwiZXhwIjoxNzQzNzM1NTkxfQ.zDpPYNEFxg-is-i5Kd3-ygDCgziP8Lsusnyp1bmb7nw\",\"created_at\":\"2025-04-04T11:29:54.933109\",\"updated_at\":\"2025-04-04T11:29:54.933109\",\"status\":1,\"meta\":[{\"meta_id\":10,\"meta_key\":\"picture\",\"meta_value\":\"https://lh3.googleusercontent.com/a/ACg8ocLFpG2QtJNS19tdH4FEvVPUUm8Fd0BV8uh4pIPh1m4uZAuynA=s96-c\"}]}}}"
				)
			) 
		),
		@ApiResponse( 
			responseCode = "401-본인 정보 조회 실패",
			description = "FAIL", 
			content = @Content(					
				examples = @ExampleObject(
					value = "{\"success\":0,\"response\":{\"message\":\"접근권한이없습니다.\"}}"
				)
			) 
		)
	})
	@GetMapping("/members/me")
	public ResponseEntity<?> getMyInfo(@Parameter(description = "로그인한 회원 토큰값") @RequestHeader("Authorization") String token){
		
		MemberDto findedMember = Commons.findMemberByToken(token, memberService);
				
		//접근권한이 없는 경우
		if ( findedMember == null )
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail( messageSource.getMessage("user.invalid_token", null, new Locale("ko")) ));
		
		List <MemberMetaResponseDto> metaList = memberMetaService.findAll(findedMember).stream().map(meta -> MemberMetaResponseDto.toResponseDto(meta)).toList();
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(ResultUtil.success( 
						MemberResponseDto.toDto(findedMember, metaList ) 
						));
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
						schema = @Schema(implementation = MemberResponseDto.class)
				),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":[{\"id\":99,\"email\":\"baekjongwon22111@naver.com\",\"name\":null,\"nickname\":\"baekjongwon22111\",\"birth\":null,\"gender\":\"\",\"contact\":null,\"address\":null,\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVVNFUiIsImVtYWlsIjoiYmFla2pvbmd3b24yMjExMUBuYXZlci5jb20iLCJzdWIiOiI5OSIsImlhdCI6MTc0MzczMzE1MywiZXhwIjoxNzQzNzM0MDUzfQ.SYISAFjNXZCiWlY_o4mLbunyGvE8Rqtt6N_0g1BuCwg\",\"created_at\":\"2025-04-04T11:08:40.071055\",\"updated_at\":\"2025-04-04T11:08:40.071055\",\"status\":1,\"meta\":[{\"meta_id\":8,\"meta_key\":\"picture\",\"meta_value\":null}]},{\"id\":99,\"email\":\"baekjongwon22111@naver.com\",\"name\":null,\"nickname\":\"baekjongwon22111\",\"birth\":null,\"gender\":\"\",\"contact\":null,\"address\":null,\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVVNFUiIsImVtYWlsIjoiYmFla2pvbmd3b24yMjExMUBuYXZlci5jb20iLCJzdWIiOiI5OSIsImlhdCI6MTc0MzczMzE1MywiZXhwIjoxNzQzNzM0MDUzfQ.SYISAFjNXZCiWlY_o4mLbunyGvE8Rqtt6N_0g1BuCwg\",\"created_at\":\"2025-04-04T11:08:40.071055\",\"updated_at\":\"2025-04-04T11:08:40.071055\",\"status\":1,\"meta\":[{\"meta_id\":8,\"meta_key\":\"picture\",\"meta_value\":null}]},{\"id\":102,\"email\":\"wowwow@naver.com\",\"name\":null,\"nickname\":\"wowwow\",\"birth\":null,\"gender\":\"\",\"contact\":null,\"address\":null,\"token\":null,\"created_at\":\"2025-04-04T11:40:32.438718\",\"updated_at\":\"2025-04-04T11:40:32.436711\",\"status\":9,\"meta\":[]}]}}"
				)
			) 
		),
		@ApiResponse( 
			responseCode = "401-모든 일반회원(USER) 조회 실패",
			description = "FAIL", 
			content = @Content(					
				examples = @ExampleObject(
					value = "{\"success\":0,\"response\":{\"message\":\"접근권한이없습니다.\"}}"
				)
			) 
		),
		@ApiResponse( 
				responseCode = "403-모든 일반회원(USER) 조회 실패",
				description = "FAIL", 
				content = @Content(					
					examples = @ExampleObject(
						value = "{\"success\":0,\"response\":{\"message\":\"이작업을수행할권한이없습니다.\"}}"
					)
				) 
			)
	})
	@GetMapping("/members/user")
	public ResponseEntity<?> getUserMembers(@Parameter(description = "로그인한 회원 토큰값") @RequestHeader("Authorization") String token){
		
		MemberDto findedMember = Commons.findMemberByToken(token, memberService);
				
		//접근권한이 없는 경우
		if ( findedMember == null )
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail( messageSource.getMessage("user.invalid_token", null, new Locale("ko")) ));
		else if ( ! findedMember.getRole().equals(Commons.ROLE_ADMINISTRATOR) ) 
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResultUtil.fail( messageSource.getMessage("user.invalid_role", null, new Locale("ko")) ));
		
		//관리자인 경우
		List<MemberResponseDto> members = memberService.findMemberByRole(Commons.ROLE_USER)
											.stream()
											.map(user -> 
												MemberResponseDto.toDto(
														user, 
														memberMetaService.findAll(user).stream()
															.map(meta -> MemberMetaResponseDto.toResponseDto(meta)).toList() ) )
											.collect(Collectors.toList() );
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(members));
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
						schema = @Schema(implementation = MemberResponseDto.class)
				),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":[{\"id\":99,\"email\":\"baekjongwon22111@naver.com\",\"name\":null,\"nickname\":\"baekjongwon22111\",\"birth\":null,\"gender\":\"\",\"contact\":null,\"address\":null,\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVVNFUiIsImVtYWlsIjoiYmFla2pvbmd3b24yMjExMUBuYXZlci5jb20iLCJzdWIiOiI5OSIsImlhdCI6MTc0MzczMzE1MywiZXhwIjoxNzQzNzM0MDUzfQ.SYISAFjNXZCiWlY_o4mLbunyGvE8Rqtt6N_0g1BuCwg\",\"created_at\":\"2025-04-04T11:08:40.071055\",\"updated_at\":\"2025-04-04T11:08:40.071055\",\"status\":1,\"meta\":[{\"meta_id\":8,\"meta_key\":\"picture\",\"meta_value\":null}]},{\"id\":101,\"email\":\"user01@gmail.com\",\"name\":\"박윤성\",\"nickname\":\"user01\",\"birth\":null,\"gender\":\"\",\"contact\":null,\"address\":null,\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVVNFUiIsImVtYWlsIjoidXNlcjAxQGdtYWlsLmNvbSIsInN1YiI6IjEwMSIsImlhdCI6MTc0MzczNDY5MSwiZXhwIjoxNzQzNzM1NTkxfQ.zDpPYNEFxg-is-i5Kd3-ygDCgziP8Lsusnyp1bmb7nw\",\"created_at\":\"2025-04-04T11:29:54.933109\",\"updated_at\":\"2025-04-04T11:29:54.933109\",\"status\":1,\"meta\":[{\"meta_id\":10,\"meta_key\":\"picture\",\"meta_value\":\"https://lh3.googleusercontent.com/a/ACg8ocLFpG2QtJNS19tdH4FEvVPUUm8Fd0BV8uh4pIPh1m4uZAuynA=s96-c\"}]}]}}"
				)
			) 
		),
		@ApiResponse( 
			responseCode = "401-상태별 회원 조회 실패",
			description = "FAIL", 
			content = @Content(					
				examples = @ExampleObject(
					value = "{\"success\":0,\"response\":{\"message\":\"접근권한이없습니다.\"}}"
				)
			) 
		),
		@ApiResponse( 
				responseCode = "403-상태별 회원 조회 실패",
				description = "FAIL", 
				content = @Content(					
					examples = @ExampleObject(
						value = "{\"success\":0,\"response\":{\"message\":\"이작업을수행할권한이없습니다.\"}}"
					)
				) 
			)
	})
	@GetMapping("/members/status/{status}")
	public ResponseEntity<?> getMembersByStatus(
			@Parameter(description = "로그인한 회원 토큰값") @RequestHeader("Authorization") String token,
			@Parameter(description = "회원의 status") @PathVariable("status") Long status){
			
		MemberDto findedMember = Commons.findMemberByToken(token, memberService);
		
		//접근권한이 없는 경우
		if ( findedMember == null )
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail( messageSource.getMessage("user.invalid_token", null, new Locale("ko")) ));
		else if ( ! findedMember.getRole().equals(Commons.ROLE_ADMINISTRATOR) ) 
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResultUtil.fail( messageSource.getMessage("user.invalid_role", null, new Locale("ko")) ));
		
		
		//관리자인 경우
		List<MemberResponseDto> findedMembers = memberService.findMemberByStatus(status)
												.stream()
												.map(mem -> 
													MemberResponseDto.toDto(
															mem, 
															memberMetaService.findAll(mem)
																.stream()
																.map(meta -> MemberMetaResponseDto.toResponseDto(meta))
																.toList() ) )
												.collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(findedMembers));
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
						schema = @Schema(implementation = MemberResponseDto.class)
				),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":[{\"id\":72,\"email\":\"ghkdwlgh123@naver.com\",\"name\":null,\"nickname\":\"ghkdwlgh123\",\"birth\":null,\"gender\":\"\",\"contact\":null,\"address\":null,\"token\":null,\"created_at\":\"2025-03-27T19:03:12.562981\",\"updated_at\":\"2025-03-27T19:03:12.559982\",\"status\":9,\"meta\":[]},{\"id\":100,\"email\":\"baekjongwon33@naver.com\",\"name\":\"박구글\",\"nickname\":\"baekjongwon33\",\"birth\":null,\"gender\":\"\",\"contact\":null,\"address\":null,\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVVNFUiIsImVtYWlsIjoiYmFla2pvbmd3b24zM0BuYXZlci5jb20iLCJzdWIiOiIxMDAiLCJpYXQiOjE3NDM3MzM3NzEsImV4cCI6MTc0MzczNDY3MX0.cX0l1lnpk-7JlNRkizLtTMUmVhyTDq77Kc0IN-17jZk\",\"created_at\":\"2025-04-04T11:19:55.298957\",\"updated_at\":\"2025-04-04T11:19:55.298957\",\"status\":1,\"meta\":[{\"meta_id\":9,\"meta_key\":\"picture\",\"meta_value\":\"http://www.google.com\"}]},{\"id\":99,\"email\":\"baekjongwon22111@naver.com\",\"name\":null,\"nickname\":\"baekjongwon22111\",\"birth\":null,\"gender\":\"\",\"contact\":null,\"address\":null,\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVVNFUiIsImVtYWlsIjoiYmFla2pvbmd3b24yMjExMUBuYXZlci5jb20iLCJzdWIiOiI5OSIsImlhdCI6MTc0MzczMzE1MywiZXhwIjoxNzQzNzM0MDUzfQ.SYISAFjNXZCiWlY_o4mLbunyGvE8Rqtt6N_0g1BuCwg\",\"created_at\":\"2025-04-04T11:08:40.071055\",\"updated_at\":\"2025-04-04T11:08:40.071055\",\"status\":1,\"meta\":[{\"meta_id\":8,\"meta_key\":\"picture\",\"meta_value\":null}]}]}}"
				) 
			)
		),
		@ApiResponse( 
			responseCode = "401-역할별 회원 조회 실패",
			description = "FAIL", 
			content = @Content(					
				examples = @ExampleObject(
					value = "{\"success\":0,\"response\":{\"message\":\"접근권한이없습니다.\"}}"
				)
			) 
		),
		@ApiResponse( 
				responseCode = "403-역할별 회원 조회 실패",
				description = "FAIL", 
				content = @Content(					
					examples = @ExampleObject(
						value = "{\"success\":0,\"response\":{\"message\":\"이작업을수행할권한이없습니다.\"}}"
					)
				) 
			)
	})
	@GetMapping("/members/role/{role}")
	public ResponseEntity<?> getMembersByRole(
			@Parameter(description = "로그인한 회원 토큰값") @RequestHeader("Authorization") String token,
			@Parameter(description = "회원의 role") @PathVariable("role") String role){
		
		MemberDto findedMember = Commons.findMemberByToken(token, memberService);
		
		//접근권한이 없는 경우
		if ( findedMember == null )
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail( messageSource.getMessage("user.invalid_token", null, new Locale("ko")) ));
		else if ( ! findedMember.getRole().equals(Commons.ROLE_ADMINISTRATOR) ) 
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResultUtil.fail( messageSource.getMessage("user.invalid_role", null, new Locale("ko")) ));
		
		//관리자인 경우
		List<MemberResponseDto> findedMembers = memberService.findMemberByRole(role)
												.stream()
												.map(mem -> 
													MemberResponseDto.toDto(mem, memberMetaService.findAll(mem).stream().map(meta -> MemberMetaResponseDto.toResponseDto(meta)).toList() ))
												.collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(findedMembers));
	}
	
	
	/**
	 * [임시 회원가입] - 임시 회원가입시 회원의 status는 9(대기)
	 * @param tempJoinDto 임시 회원가입용 DTO
	 * @return ResponseEntity
	 */
	/*
	@Operation( summary = "임시 회원가입", description = "임시 회원가입 API" )
	@ApiResponses({
		@ApiResponse( 
			responseCode = "201-임시 회원가입 성공",
			description = "SUCCESS",
			content = @Content(	
				schema = @Schema(implementation = MemberResponseDto.class),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":{\"id\":48,\"email\":\"baekjongwon222@naver.com\",\"name\":\"박종원\",\"nickname\":\"빽다방\",\"birth\":\"1950-03-17\",\"gender\":\"M\",\"contact\":null,\"address\":null,\"created_at\":\"2025-03-17T08:48:32.6060574\",\"updated_at\":\"2025-03-17T08:48:32.6060574\",\"status\":9}}}"
				)
			) 
		),
		@ApiResponse( 
			responseCode = "401-임시 회원가입 실패",
			description = "FAIL", 
			content = @Content(					
				examples = @ExampleObject(
					value = "{\"success\":0,\"response\":{\"message\":\"잘못된이메일입니다.\"}}"
				)
			) 
		),
		@ApiResponse( 
				responseCode = "409-임시 회원가입 실패(이미 사용중인 이메일인 경우)",
				description = "FAIL", 
				content = @Content(					
					examples = @ExampleObject(
						value = "{\"success\":0,\"response\":{\"message\":\"사용중인이메일입니다.\"}}"
					)
				) 
			),
		@ApiResponse( 
				responseCode = "500-임시 회원가입 실패",
				description = "FAIL", 
				content = @Content(					
					examples = @ExampleObject(
						value = "{\"success\":0,\"response\":{\"message\":\"회원가입에실패했습니다.\"}}"
					)
				) 
			)
	})
	@PostMapping("/temp_members")
	public ResponseEntity<?> tempJoin(@Parameter(description = "임시 회원가입 요청 객체") @RequestBody MemberTempJoinDto tempJoinDto){
		//<회원이 이메일을 입력하지 않은 경우>
		if(Commons.isNull(tempJoinDto.getEmail())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail(messageSource.getMessage("user.invalid_value_mail", null, new Locale("ko")) ));
		}
		
		//<회원이 이메일을 입력한 경우>
		//임시 회원가입 실패 - 이미 사용중인 아이디 입력시
		if(memberService.isExistsEmail(tempJoinDto.getEmail())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(ResultUtil.fail(messageSource.getMessage("user.invalid_value_mail_exist", null, new Locale("ko")))); 
		}

		tempJoinDto.setRole(Commons.ROLE_USER);
		
		//임시 회원가입 처리								
		MemberDto tempJoinedMember = memberService.tempJoin(tempJoinDto);
		
		return tempJoinedMember != null ? ResponseEntity.status(HttpStatus.CREATED).body(ResultUtil.success(MemberResponseDto.toDto(tempJoinedMember))) :
				ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.fail(messageSource.getMessage("user.join_fail", null, new Locale("ko"))));
	} */
	
	/**
	 * [direct 회원가입] - 최종 회원가입 처리 -> 최종 회원가입시 회원의 status는 1(일반)
	 * @param joinDto(회원가입 요청 DTO) 
	 * @param bindingResult joinDto의 유효성 검증 실패시 에러가 담기는 객체(앞의 DTO객체의 유효성 검증 실패시 담기는 에러)
	 * @return ResponseEntity(사용자의 HttpRequest에 대한 HTTP 응답을 감싸는 클래스로 HttpStatus, HttpHeaders, HttpBody를 포함)
	 */
	@Operation( summary = "direct 회원가입", description = "direct 회원가입 API" )
	@ApiResponses({
		@ApiResponse( 
			responseCode = "201-회원가입 성공",
			description = "SUCCESS(사용중인 이메일이 아니면서 전화번호 미입력시)",
			content = @Content(	
				schema = @Schema(implementation = MemberResponseDto.class),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":{\"id\":102,\"email\":\"wowwow@naver.com\",\"name\":\"우와앙\",\"nickname\":\"wowwow\",\"birth\":\"2020-03-26\",\"gender\":\"F\",\"contact\":null,\"address\":null,\"token\":null,\"created_at\":\"2025-04-04T12:06:00.0706942\",\"updated_at\":\"2025-04-04T12:06:00.0706942\",\"status\":1,\"meta\":[]}}}"
				)
			) 
		),
		@ApiResponse( 
			responseCode = "400-회원가입 실패",
			description = "FAIL(유효성 검증 실패)", 
			content = @Content(					
				examples = @ExampleObject(
					value = "{\"success\":0,\"response\":{\"message\":\"email:이메일을입력하세요\\r\\n\"}}"
				)
			) 
		),
		@ApiResponse( 
				responseCode = "400-회원가입 실패",
				description = "FAIL(회원 미존재)", 
				content = @Content(					
					examples = @ExampleObject(
						value = "{\"success\":0,\"response\":{\"message\":\"회원을찾을수없습니다.\"}}"
					)
				) 
			),
		@ApiResponse( 
				responseCode = "401-회원가입 실패",
				description = "FAIL(이메일 인증 안 된 경우)", 
				content = @Content(					
					examples = @ExampleObject(
						value = "{\"success\":0,\"response\":{\"message\":\"이메일인증이필요합니다.\"}}"
					)
				) 
			),
		@ApiResponse( 
				responseCode = "409-회원가입 실패",
				description = "FAIL(이미 가입된 회원인 경우)", 
				content = @Content(					
					examples = @ExampleObject(
						value = "{\"success\":0,\"response\":{\"message\":\"이미가입된회원입니다.\"}}" 
					)
				) 
			),
		@ApiResponse( 
				responseCode = "409-회원가입 실패",
				description = "FAIL(이미 사용중인 전화번호인 경우)", 
				content = @Content(					
					examples = @ExampleObject(
						value = "{\"success\":0,\"response\":{\"message\":\"사용중인전화번호입니다.\"}}" 
					)
				) 
			),
		@ApiResponse( 
				responseCode = "500-회원가입 실패",
				description = "FAIL", 
				content = @Content(					
					examples = @ExampleObject(
						value = "{\"success\":0,\"response\":{\"message\":\"회원가입에실패했습니다.\"}}" 
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
		
		//회원이 존재하지 않는 경우
		if(findedMember == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultUtil.fail( messageSource.getMessage("member.notfound", null, new Locale("ko")) ));
		}
		
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
		findedMember.setPassword(joinDto.getPassword());
		findedMember.setRole(Commons.ROLE_USER);
		findedMember.setName(joinDto.getName());
		findedMember.setNickname(joinDto.getNickname());
		findedMember.setBirth(joinDto.getBirth());
		findedMember.setGender(joinDto.getGender());
		findedMember.setContact(joinDto.getContact());
		findedMember.setAddress(joinDto.getAddress());
		
		MemberDto memberDto = memberService.join(findedMember);
		
		return memberDto != null ? 
				ResponseEntity
					.status(HttpStatus.CREATED)
					.body(ResultUtil.success(
							MemberResponseDto.toDto(
									memberDto, 
									memberMetaService.findAll(memberDto)
										.stream()
										.map(meta -> MemberMetaResponseDto.toResponseDto(meta))
										.toList() ))) :
				ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ResultUtil.fail(messageSource.getMessage("user.join_fail", null, new Locale("ko"))));
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
				schema = @Schema(implementation = Map.class),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":{\"id\":102,\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVVNFUiIsImVtYWlsIjoid293d293QG5hdmVyLmNvbSIsInN1YiI6IjEwMiIsImlhdCI6MTc0MzczNjAyNywiZXhwIjoxNzQzNzM2OTI3fQ.7DOwGdCSfFBjcUQp87jxb4qH3bFriRHrO897EMvTdzk\"}}}"
				)
			) 
		),
		@ApiResponse( 
			responseCode = "401-로그인 실패",
			description = "FAIL", 
			content = @Content(					
				examples = @ExampleObject(
					value = "{ \"success\": 0, \"response\": { \"message\": \"이메일또는비밀번호가일치하지않습니다.\"  } }" 
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
		if(findedMember == null || ( findedMember != null && findedMember.getStatus() != 1 ) ) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail(messageSource.getMessage("user.login_fail", null, new Locale("ko")) ));
		}
	
		/*
		 * <회원인 경우>
		 * 회원의 token필드(JWT) 설정
		 * id, email -> JWT token 생성 및 업데이트 
		 * - jwtutil.ParseToken(String token) 을 사용해 Map<String, Object> 반환 
		 * - 예 { sub=1, iat=1741051044, exp=1741051944 } 
		 */
		
		String token = null;
		
		try {
			Map<String, Object> claims = new HashMap<>();
			claims.put( "email" , findedMember.getEmail());
			claims.put( "role" , findedMember.getRole());
			
			token = jwtutil.CreateToken(findedMember.getId().toString(), claims);			
			findedMember.setToken(token); 
		}
		catch( Exception ex ) {
			return ResponseEntity
					.status(HttpStatus.UNAUTHORIZED)
					.body(ResultUtil.fail(messageSource.getMessage("user.login_fail_token", null, new Locale("ko"))));
		}
		
		//회원 정보 수정
		MemberDto updatedMember = memberService.updateMember(findedMember);
		
		Long id = updatedMember.getId();
		
		Map<String, Object> result = new HashMap<>();
		result.put("id", id);
		result.put("token", token);
		
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(result));
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
		MemberDto findedMember = Commons.findMemberByToken(token, memberService);
		
		if(findedMember != null) {
			//토큰을 null로
			findedMember.setToken(null);
			MemberDto updatedMember = memberService.updateMember(findedMember);
			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(null));
		}
		//<찾은 회원이 존재하지 않는 경우>
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(null));
		
	}
	
	/**
	 * [회원정보 수정]
	 * @param updateDto 수정 정보가 들어있는 수정 요청 객체
	 * @param token 로그인한 회원의 토큰값
	 * @return ResponseEntity
	 */
	@Operation( summary = "회원정보 수정", description = "회원정보 수정 API" )
	@ApiResponses({
		@ApiResponse( 
			responseCode = "200-수정 성공",
			description = "SUCCESS", 
			content = @Content(	
				schema = @Schema(implementation = MemberResponseDto.class),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":{\"id\":101,\"email\":\"user01@gmail.com\",\"name\":\"박윤성아재\",\"nickname\":\"퉁퉁이발\",\"birth\":null,\"gender\":\"M\",\"contact\":\"\",\"address\":\"서울사당\",\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVVNFUiIsImVtYWlsIjoidXNlcjAxQGdtYWlsLmNvbSIsInN1YiI6IjEwMSIsImlhdCI6MTc0MzczNDY5MSwiZXhwIjoxNzQzNzM1NTkxfQ.zDpPYNEFxg-is-i5Kd3-ygDCgziP8Lsusnyp1bmb7nw\",\"created_at\":\"2025-04-04T11:29:54.933109\",\"updated_at\":\"2025-04-04T12:12:44.0507804\",\"status\":1,\"meta\":[{\"meta_id\":10,\"meta_key\":\"picture\",\"meta_value\":\"https://lh3.googleusercontent.com/a/ACg8ocLFpG2QtJNS19tdH4FEvVPUUm8Fd0BV8uh4pIPh1m4uZAuynA=s96-c\"}]}}}"
				)
			) 
		),
		@ApiResponse( 
			responseCode = "401-수정 실패",
			description = "FAIL", 
			content = @Content(					
				examples = @ExampleObject(
					value = "{\"success\":0,\"response\":{\"message\":\"토큰이생성되지않았습니다.\"}}"
				)
			) 
		)
	})
	@PutMapping("/members")
	public ResponseEntity<?> update(
			@Parameter(description = "수정 요청 객체") @RequestBody MemberUpdateDto updateDto,
			@RequestHeader(name = "authorization") String token){
		
		//토큰값으로 회원 조회
		MemberDto findedMember = Commons.findMemberByToken(token, memberService);
		
		if (findedMember == null) {
			//<찾은 회원이 존재하지 않는 경우>
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail( messageSource.getMessage("user.login_fail_token", null, new Locale("ko")) ));
		}
		
		//<찾은 회원이 존재하는 경우>
		//변경한 회원정보로 기존 회원 dto 수정 (회원정보 수정은 비밀번호,이름,닉네임,전화번호,주소 만 수정 가능)
		findedMember.setName(updateDto.getName());
		findedMember.setNickname(updateDto.getNickname());
		findedMember.setContact(updateDto.getContact());
		findedMember.setAddress(updateDto.getAddress());
		findedMember.setUpdated_at(LocalDateTime.now());
		//변경된 회원 정보로 회원 수정
		MemberDto updatedMember = memberService.updateMember(findedMember);
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(ResultUtil.success(
						MemberResponseDto.toDto(
								updatedMember, 
								memberMetaService.findAll(updatedMember)
									.stream()
									.map(meta -> MemberMetaResponseDto.toResponseDto(meta))
									.toList() )
				));
		
	}
	
	/**
	 * [비밀번호 수정]
	 * @param newPwd 수정할 새 비밀번호
	 * @param token 로그인한 회원의 토큰값
	 * @return
	 */
	@Operation( summary = "비밀번호 수정", description = "비밀번호 수정 API" )
	@ApiResponses({
		@ApiResponse( 
			responseCode = "200-수정 성공",
			description = "SUCCESS", 
			content = @Content(	
				schema = @Schema(implementation = MemberResponseDto.class),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":{\"id\":101,\"email\":\"user01@gmail.com\",\"name\":\"박윤성아재\",\"nickname\":\"퉁퉁이발\",\"birth\":null,\"gender\":\"M\",\"contact\":null,\"address\":\"서울사당\",\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVVNFUiIsImVtYWlsIjoidXNlcjAxQGdtYWlsLmNvbSIsInN1YiI6IjEwMSIsImlhdCI6MTc0MzczNDY5MSwiZXhwIjoxNzQzNzM1NTkxfQ.zDpPYNEFxg-is-i5Kd3-ygDCgziP8Lsusnyp1bmb7nw\",\"created_at\":\"2025-04-04T11:29:54.933109\",\"updated_at\":\"2025-04-04T12:13:47.9818825\",\"status\":1,\"meta\":[{\"meta_id\":10,\"meta_key\":\"picture\",\"meta_value\":\"https://lh3.googleusercontent.com/a/ACg8ocLFpG2QtJNS19tdH4FEvVPUUm8Fd0BV8uh4pIPh1m4uZAuynA=s96-c\"}]}}}"
				)
			) 
		),
		@ApiResponse( 
			responseCode = "401-수정 실패",
			description = "FAIL", 
			content = @Content(					
				examples = @ExampleObject(
					value = "{\"success\":0,\"response\":{\"message\":\"토큰이생성되지않았습니다.\"}}"
				)
			) 
		),
		@ApiResponse( 
				responseCode = "500-수정 실패",
				description = "FAIL", 
				content = @Content(					
					examples = @ExampleObject(
						value = "{\"success\":0,\"response\":{\"message\":\"비밀번호변경을실패했습니다.\"}}"
					)
				) 
			)
	})
	@PatchMapping("/members")
	public ResponseEntity<?> updatePassword(
			@Parameter(description = "수정할 비밀번호") @RequestBody Map<String, String> newPwd,
			@Parameter(description = "회원의 토큰값") @RequestHeader(name = "authorization") String token){
		//토큰값으로 회원 조회
		MemberDto findedMember = Commons.findMemberByToken(token, memberService);
		
		if (findedMember == null) {
			//<찾은 회원이 존재하지 않는 경우>
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail( messageSource.getMessage("user.login_fail_token", null, new Locale("ko")) ));
		}
		
		//<찾은 회원이 존재하는 경우>
		System.out.println("비번 수정 전:"+findedMember.getPassword());
		//비밀번호 수정
		try {
			findedMember.setPassword(EncryptAES256.encrypt(newPwd.get("password")));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.fail(messageSource.getMessage("user.pwd_change_fail", null, new Locale("ko"))));
		}
		findedMember.setUpdated_at(LocalDateTime.now());
		
		MemberDto updatedMember = memberService.updateMember(findedMember);
		System.out.println("비번 수정 후:"+updatedMember.getPassword());
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(ResultUtil.success(
						MemberResponseDto.toDto(
								updatedMember, 
								memberMetaService.findAll(updatedMember)
									.stream()
									.map(meta -> MemberMetaResponseDto.toResponseDto(meta))
									.toList() )
				));
		
	}
	
	/**
	 * [회원탈퇴]
	 * @param token 회원 토큰값
	 * @param mid 탈퇴할 회원 id
	 * @return ResponseEntity
	 */
	@Operation( summary = "회원탈퇴", description = "회원탈퇴 API" )
	@ApiResponses({
		@ApiResponse( 
			responseCode = "200-회원탈퇴 성공",
			description = "SUCCESS", 
			content = @Content(	
				schema = @Schema(implementation = Map.class),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":{\"member\":{\"id\":56,\"email\":\"aaaaauth@naver.com\",\"name\":\"오인증\",\"nickname\":\"aaaaauth\",\"birth\":\"2020-03-26\",\"gender\":\"M\",\"contact\":null,\"address\":null,\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVVNFUiIsImVtYWlsIjoiYWFhYWF1dGhAbmF2ZXIuY29tIiwic3ViIjoiNTYiLCJpYXQiOjE3NDI5ODc2MjYsImV4cCI6MTc0Mjk4ODUyNn0.2AM3Ao2lKjwvR-lXLwWaz2CxDBamNcsK1s9zDjzdTlA\",\"created_at\":\"2025-03-26T19:46:34.129795\",\"updated_at\":\"2025-03-26T19:46:34.129795\",\"status\":0,\"meta\":[]},\"posts\":[]}}}"
				)
			) 
		),
		@ApiResponse( 
			responseCode = "401-회원탈퇴 실패",
			description = "FAIL", 
			content = @Content(					
				examples = @ExampleObject(
					value = "{\"success\":0,\"response\":{\"message\":\"접근권한이없습니다.\"}}"
				)
			) 
		),
		@ApiResponse( 
				responseCode = "403-회원탈퇴 실패",
				description = "FAIL", 
				content = @Content(					
					examples = @ExampleObject(
						value = "{\"success\":0,\"response\":{\"message\":\"이작업을수행할권한이없습니다.\"}}"
					)
				) 
			)
	})
	@PatchMapping("/members/withdraw/{mid}")
	public ResponseEntity<?> withdraw(
			@Parameter(description = "회원의 토큰값") @RequestHeader(name = "authorization") String token,
			@Parameter(description = "탈퇴할 회원 id") @PathVariable(name = "mid") Long mid){
		//회원 조회
		MemberDto loginMember = memberService.findMemberByToken(token); //로그인한 회원
		MemberDto findedMember = memberService.findMemberById(mid); //탈퇴하고자 하는 회원
		
		//회원 미존재시
		if(loginMember == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail(messageSource.getMessage("user.invalid_token", null, new Locale("ko"))));
		
		//탈퇴할 회원이 자신이 아니면서 관리자도 아닌 경우 - 탈퇴 불가
		if(loginMember.getId() != findedMember.getId() && !Commons.ROLE_ADMINISTRATOR.equals(loginMember.getRole())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResultUtil.fail(messageSource.getMessage("user.invalid_role", null, new Locale("ko"))));
		}
		
		//탈퇴할 회원이 없거나 이미 탈퇴한 경우
		if(findedMember == null || (findedMember != null && findedMember.getStatus() == 0) ) return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(null));
		
		//<<탈퇴 가능한 경우>>
		//<회원 정보 수정>
		MemberDto withdrawnMember = memberService.withdrawMember(mid);
		MemberResponseDto respMember = MemberResponseDto.toDto(
										withdrawnMember, 
										memberMetaService.findAll(withdrawnMember)
											.stream()
											.map(meta -> MemberMetaResponseDto.toResponseDto(meta))
											.toList() 
										);
		
		//회원이 작성한 모든 글 조회
		List <PostsDto> selectedPosts = postsService.getPostsByMember(withdrawnMember.getId());
		
		List<PostsResponseDto> deletedPosts = new Vector<>();
		//<회원이 쓴 글 삭제>
		for(PostsDto post : selectedPosts) {
			
			PostsDto deleted = postsService.deletePost(post.getId());
			
			List <PostCategoryRelationshipsDto> pcrList = pcrService.findAllByPostId(deleted.getId());
			List<TermsResponseDto> termsRespList = pcrList.stream().map(pcr -> TermsResponseDto.toDto( pcr.getTermCategoryDto() )).toList();
			
			List<PostMetaDto> metaList = postMetaService.findAll(post);
			List<PostMetaResponseDto> metaResponses = metaList.stream().map(entity -> PostMetaResponseDto.toResponseDto(entity)).toList();
			
			PostsResponseDto respPost = PostsResponseDto.toDto(deleted.toEntity(), termsRespList,metaResponses);
			deletedPosts.add(respPost);
		}
		
		Map<String, Object> result = new HashMap<>();
		result.put("member", respMember);
		result.put("posts", deletedPosts);
		
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(result));
		
	}
	
}
