package com.ict.vita.controller.chatsession;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.chatqna.ChatQnaDto;
import com.ict.vita.service.chatqna.ChatQnaService;
import com.ict.vita.service.chatquestion.ChatQuestionDto;
import com.ict.vita.service.chatquestion.ChatQuestionService;
import com.ict.vita.service.chatsession.ChatSessionDto;
import com.ict.vita.service.chatsession.ChatSessionResponseDto;
import com.ict.vita.service.chatsession.ChatSessionService;
import com.ict.vita.service.member.MemberDto;
import com.ict.vita.service.member.MemberResponseDto;
import com.ict.vita.service.member.MemberService;
import com.ict.vita.service.membermeta.MemberMetaResponseDto;
import com.ict.vita.service.membermeta.MemberMetaService;
import com.ict.vita.util.Commons;
import com.ict.vita.util.ResultUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin
public class ChatSessionController {
	//서비스 주입
	private final ChatSessionService chatSessionService;
	private final MemberService memberService;
	private final MemberMetaService memberMetaService;
	private final ChatQnaService chatQnaService;
	private final ChatQuestionService chatQuestionService;
	
	private final MessageSource messageSource;
	
	/**
	 * [전체 세션 조회] - 관리자만 전체 세션 조회 가능 & 페이징 적용
	 * @param token 로그인한 회원 토큰
	 * @param p 페이지
	 * @param ol 출력갯수
	 * @return ResponseEntity
	 */
	@Operation( summary = "전체 세션 조회", description = "전체 세션 조회 API" )
	@ApiResponses({
		@ApiResponse( 
			responseCode = "200-전체 세션 조회 성공",
			description = "SUCCESS",
			content = @Content(	
				array = @ArraySchema(
						schema = @Schema(implementation = ChatSessionResponseDto.class)
				),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":[{\"id\":58,\"member\":{\"id\":73,\"email\":\"admin@gmail.com\",\"name\":\"관리자\",\"nickname\":\"admin\",\"birth\":\"2025-03-29\",\"gender\":\"F\",\"contact\":\"01000000000\",\"address\":\"서울시강남구\",\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU5JU1RSQVRPUiIsImVtYWlsIjoiYWRtaW5AZ21haWwuY29tIiwic3ViIjoiNzMiLCJpYXQiOjE3NDM1OTEyNDAsImV4cCI6MTc0MzU5MjE0MH0.lp4Lho1Ozm8oN-HfLqoLrsh_zdWbx2_nL8w3A3WoAK8\",\"created_at\":\"2025-03-29T18:41:15.915\",\"updated_at\":\"2025-03-29T18:41:15.915\",\"status\":1,\"meta\":[]},\"created_at\":\"2025-03-29T18:46:25.290857\",\"updated_at\":\"2025-04-02T19:57:19.607347\",\"status\":1,\"count\":0,\"lastQuestion\":\"감염성질병\"},{\"id\":60,\"member\":{\"id\":73,\"email\":\"admin@gmail.com\",\"name\":\"관리자\",\"nickname\":\"admin\",\"birth\":\"2025-03-29\",\"gender\":\"F\",\"contact\":\"01000000000\",\"address\":\"서울시강남구\",\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU5JU1RSQVRPUiIsImVtYWlsIjoiYWRtaW5AZ21haWwuY29tIiwic3ViIjoiNzMiLCJpYXQiOjE3NDM1OTEyNDAsImV4cCI6MTc0MzU5MjE0MH0.lp4Lho1Ozm8oN-HfLqoLrsh_zdWbx2_nL8w3A3WoAK8\",\"created_at\":\"2025-03-29T18:41:15.915\",\"updated_at\":\"2025-03-29T18:41:15.915\",\"status\":1,\"meta\":[]},\"created_at\":\"2025-04-02T19:54:34.724171\",\"updated_at\":\"2025-04-02T19:55:14.60605\",\"status\":1,\"count\":0,\"lastQuestion\":\"눈이아파요\"},{\"id\":59,\"member\":{\"id\":73,\"email\":\"admin@gmail.com\",\"name\":\"관리자\",\"nickname\":\"admin\",\"birth\":\"2025-03-29\",\"gender\":\"F\",\"contact\":\"01000000000\",\"address\":\"서울시강남구\",\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU5JU1RSQVRPUiIsImVtYWlsIjoiYWRtaW5AZ21haWwuY29tIiwic3ViIjoiNzMiLCJpYXQiOjE3NDM1OTEyNDAsImV4cCI6MTc0MzU5MjE0MH0.lp4Lho1Ozm8oN-HfLqoLrsh_zdWbx2_nL8w3A3WoAK8\",\"created_at\":\"2025-03-29T18:41:15.915\",\"updated_at\":\"2025-03-29T18:41:15.915\",\"status\":1,\"meta\":[]},\"created_at\":\"2025-03-29T19:00:54.790221\",\"updated_at\":\"2025-03-29T19:00:54.787217\",\"status\":0,\"count\":0,\"lastQuestion\":null}]}}"
				)
			) 
		),
		@ApiResponse( 
			responseCode = "401-전체 세션 조회 실패",
			description = "FAIL", 
			content = @Content(					
				examples = @ExampleObject(
					value = "{\"success\":0,\"response\":{\"message\":\"접근권한이없습니다.\"}}"
				)
			) 
		),
		@ApiResponse( 
				responseCode = "403-전체 세션 조회 실패",
				description = "FAIL", 
				content = @Content(					
					examples = @ExampleObject(
						value = "{\"success\":0,\"response\":{\"message\":\"이작업을수행할권한이없습니다.\"}}"
					)
				) 
			)
	})
	@GetMapping("/sessions")
	public ResponseEntity<?> getAllSessions(
			@Parameter(description = "로그인한 회원 토큰") @RequestHeader("Authorization") String token,
			@Parameter(description = "페이지") @RequestParam(required = false, defaultValue = "0") int p, 
			@Parameter(description = "출력 개수 제한") @RequestParam(required = false, defaultValue = "50") int ol){
		//로그인한 회원 조회
		MemberDto loginMember = Commons.findMemberByToken(token, memberService);
		
		//회원이 존재하지 않는 경우
		if(loginMember == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail( messageSource.getMessage("user.invalid_token", null, new Locale("ko")) ));
		}
		
		//<관리자가 아닌 경우>
		if( !Commons.ROLE_ADMINISTRATOR.equals(loginMember.getRole()) ) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResultUtil.fail( messageSource.getMessage("user.invalid_role", null, new Locale("ko")) ));
		}
		
		//<관리자인 경우>	
		List<ChatSessionDto> sessions;
		//전체 세션 조회
		if(p > 0)
			sessions = chatSessionService.findAll(p,ol);
		else 
			sessions = chatSessionService.findAll();
			
		return ResponseEntity.status(HttpStatus.OK)
				.body(ResultUtil.success(sessions.stream()
						.map(session -> {
								//세션 주인 메타정보
								MemberDto owner = session.getMemberDto();
								List<MemberMetaResponseDto> metas = memberMetaService.findAll(owner)
																	.stream()
																	.map(meta -> MemberMetaResponseDto.toResponseDto(meta))
																	.toList();
							
								ChatSessionResponseDto responseSession = ChatSessionResponseDto.toDto(session, metas );
								
								//세션의 마지막 질문내용 조회 로직
								ChatQnaDto qna = chatQnaService.findLastQuestionOfSession(session.getId());
								ChatQuestionDto question = qna != null ? chatQuestionService.getQuestion(qna.getChatQuestionDto().getId()) : null;
								String qContent = question != null ? question.getContent() : null;
								responseSession.setLastQuestion( qContent );
								
								return responseSession;
							})
						.toList() ));
		
	}
	
	/**
	 * [본인 세션 조회] - 페이징 적용
	 * @param token 로그인한 회원 토큰
	 * @param p 페이지
	 * @param ol 출력갯수
	 * @return ResponseEntity
	 */
	@Operation( summary = "본인 세션 조회", description = "본인 세션 조회 API" )
	@ApiResponses({
		@ApiResponse( 
			responseCode = "200-본인 세션 조회 성공",
			description = "SUCCESS",
			content = @Content(	
				array = @ArraySchema(
						schema = @Schema(implementation = ChatSessionResponseDto.class)
				),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":[{\"id\":57,\"member\":{\"id\":46,\"email\":\"admin@naver.com\",\"name\":\"관리자1\",\"nickname\":\"관리자nick\",\"birth\":null,\"gender\":\"M\",\"contact\":null,\"address\":null,\"token\":\"adminToken\",\"created_at\":\"2025-03-10T18:55:57.835\",\"updated_at\":\"2025-03-10T18:55:57.835\",\"status\":1,\"meta\":[]},\"created_at\":\"2025-03-28T09:36:36.909724\",\"updated_at\":\"2025-03-28T09:36:36.906725\",\"status\":1,\"count\":0,\"lastQuestion\":null},{\"id\":56,\"member\":{\"id\":46,\"email\":\"admin@naver.com\",\"name\":\"관리자1\",\"nickname\":\"관리자nick\",\"birth\":null,\"gender\":\"M\",\"contact\":null,\"address\":null,\"token\":\"adminToken\",\"created_at\":\"2025-03-10T18:55:57.835\",\"updated_at\":\"2025-03-10T18:55:57.835\",\"status\":1,\"meta\":[]},\"created_at\":\"2025-03-28T09:31:53.662648\",\"updated_at\":\"2025-03-28T09:31:53.890544\",\"status\":1,\"count\":0,\"lastQuestion\":\"질문내용\"}]}}"
				)
			) 
		),
		@ApiResponse( 
			responseCode = "401-본인 세션 조회 실패",
			description = "FAIL", 
			content = @Content(					
				examples = @ExampleObject(
					value = "{\"success\":0,\"response\":{\"message\":\"접근권한이없습니다.\"}}"
				)
			) 
		)
	})
	@GetMapping("/sessions/me")
	public ResponseEntity<?> getMySessions(
			@Parameter(description = "로그인한 회원 토큰") @RequestHeader("Authorization") String token,
			@Parameter(description = "페이지") @RequestParam(required = false, defaultValue = "0") int p, 
			@Parameter(description = "출력 개수 제한") @RequestParam(required = false, defaultValue = "50") int ol){
		//로그인한 회원 조회
		MemberDto loginMember = Commons.findMemberByToken(token, memberService);
		
		//회원이 존재하지 않는 경우
		if(loginMember == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail( messageSource.getMessage("user.invalid_token", null, new Locale("ko")) ));
		}
		
		List<ChatSessionDto> sessions;
		
		//페이징
		if(p > 0) sessions = chatSessionService.findAllByMember(loginMember.getId(), p, ol); //페이징 적용
		else sessions = chatSessionService.findAllByMember(loginMember.getId()); //페이징 미적용
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(ResultUtil.success(sessions.stream()
						.map(session -> {
								//세션 주인 메타정보
								MemberDto owner = session.getMemberDto();
								List<MemberMetaResponseDto> metas = memberMetaService.findAll(owner)
																	.stream()
																	.map(meta -> MemberMetaResponseDto.toResponseDto(meta))
																	.toList();
							
								ChatSessionResponseDto responseSession = ChatSessionResponseDto.toDto(session, metas);
								
								//세션의 마지막 질문내용 조회 로직
								ChatQnaDto qna = chatQnaService.findLastQuestionOfSession(session.getId());
								ChatQuestionDto question = qna != null ? chatQuestionService.getQuestion(qna.getChatQuestionDto().getId()) : null;
								String qContent = question != null ? question.getContent() : null;
								responseSession.setLastQuestion( qContent );
								
								return responseSession;
							})
						.toList() ));
	}
	
	/**
	 * [세션id로 세션 조회] - 본인 세션은 다 조회,남의 세션은 공개만 조회,관리자는 다 조회
	 * @param token 로그인한 회원 토큰
	 * @param id 세션id
	 * @return ResponseEntity
	 */
	@Operation( summary = "세션id로 세션 조회", description = "세션id로 세션 조회 API" )
	@ApiResponses({
		@ApiResponse( 
			responseCode = "200-세션id로 세션 조회 성공",
			description = "SUCCESS",
			content = @Content(	
				schema = @Schema(implementation = ChatSessionResponseDto.class),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":{\"id\":43,\"member\":{\"id\":31,\"email\":\"hahaha12@naver.com\",\"name\":\"개명홍길동2\",\"nickname\":\"홍홍2\",\"birth\":\"2025-02-27\",\"gender\":\"M\",\"contact\":\"020211111\",\"address\":null,\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVVNFUiIsImVtYWlsIjoiaGFoYWhhMTJAbmF2ZXIuY29tIiwic3ViIjoiMzEiLCJpYXQiOjE3NDEyMzc0NTcsImV4cCI6MTc0MTIzODM1N30.jjf9dHooubsSH0GbYa0C6Ed7q0pemn-rtrAbUyzPQEA\",\"created_at\":\"2025-02-28T20:16:05.570502\",\"updated_at\":\"2025-02-28T20:16:05.549038\",\"status\":9,\"meta\":[]},\"created_at\":\"2025-03-06T15:23:27.484277\",\"updated_at\":\"2025-03-27T17:53:40.439132\",\"status\":1,\"count\":0,\"lastQuestion\":\"하하하하하핳\"}}}"
				)
			) 
		),
		@ApiResponse( 
			responseCode = "401-세션id로 세션 조회 실패",
			description = "FAIL", 
			content = @Content(					
				examples = @ExampleObject(
					value = "{\"success\":0,\"response\":{\"message\":\"접근권한이없습니다.\"}}"
				)
			) 
		)
	})
	@GetMapping("/sessions/{id}")
	public ResponseEntity<?> getSessionById(
			@Parameter(description = "로그인한 회원 토큰") @RequestHeader("Authorization") String token,
			@Parameter(description = "세션 id") @PathVariable("id") Long sid){
		//로그인한 회원 조회
		MemberDto loginMember = Commons.findMemberByToken(token, memberService);
		
		//회원이 존재하지 않는 경우
		if(loginMember == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail( messageSource.getMessage("user.invalid_token", null, new Locale("ko")) ));
		}
		
		//세션 조회
		ChatSessionDto sessionDto = chatSessionService.findById(sid);
		//세션 미존재시
		if(sessionDto == null) return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(null));
		
		//세션 주인 메타정보
		MemberDto owner = sessionDto.getMemberDto();
		List<MemberMetaResponseDto> metas = memberMetaService.findAll(owner)
											.stream()
											.map(meta -> MemberMetaResponseDto.toResponseDto(meta))
											.toList();
		
		//<관리자거나 자신의 세션인 경우> - 공개/비공개 세션 조회 가능
		if(loginMember.getRole().equals(Commons.ROLE_ADMINISTRATOR) || loginMember.getId() == sessionDto.getMemberDto().getId() ) {
			
			ChatSessionResponseDto response = null;
			
			response = ChatSessionResponseDto.toDto(sessionDto, metas);
			
			//세션의 마지막 질문내용 조회 로직
			ChatQnaDto qna = chatQnaService.findLastQuestionOfSession(sessionDto.getId());
			ChatQuestionDto question = qna != null ? chatQuestionService.getQuestion(qna.getChatQuestionDto().getId()) : null;
			String qContent = question != null ? question.getContent() : null;
			
			response.setLastQuestion( qContent );
			
			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( response ));
		}
		
		//<자신의 세션이 아닌 경우> - 공개된 세션인 경우만 조회 가능
		ChatSessionDto findedSession = chatSessionService.getPublicById(sid);
		//공개 세션이 아닌 경우
		if(findedSession == null)
			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(null));
		
		//공개 세션인 경우
		return ResponseEntity.status(HttpStatus.OK).body( ResultUtil.success( ChatSessionResponseDto.toDto(findedSession,metas) ) );

	}
	
	/**
	 * [회원id로 세션 조회] - 본인 세션은 다 조회,남의 세션은 공개만 조회,관리자는 다 조회 & 페이징 적용
	 * @param token 로그인한 회원 토큰
	 * @param mid 회원id
	 * @param p 페이지
	 * @param ol 출력갯수
	 * @return ResponseEntity
	 */
	@Operation( summary = "회원id로 세션 조회", description = "회원id로 세션 조회 API" )
	@ApiResponses({
		@ApiResponse( 
			responseCode = "200-회원id로 세션 조회 성공",
			description = "SUCCESS",
			content = @Content(
				array = @ArraySchema(
						schema = @Schema(implementation = ChatSessionResponseDto.class)
				),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":[{\"id\":45,\"member\":{\"id\":37,\"email\":\"abab@naver.com\",\"name\":\"홍길동\",\"nickname\":\"TEMPORARY\",\"birth\":\"2025-03-01\",\"gender\":\"F\",\"contact\":null,\"address\":null,\"token\":\"testtoken\",\"created_at\":\"2025-03-07T18:50:04.665174\",\"updated_at\":\"2025-03-07T18:50:04.665174\",\"status\":1,\"meta\":[]},\"created_at\":\"2025-03-13T09:32:27.156683\",\"updated_at\":\"2025-03-27T15:50:18.658097\",\"status\":0,\"count\":0,\"lastQuestion\":\"질문이지렁\"},{\"id\":54,\"member\":{\"id\":37,\"email\":\"abab@naver.com\",\"name\":\"홍길동\",\"nickname\":\"TEMPORARY\",\"birth\":\"2025-03-01\",\"gender\":\"F\",\"contact\":null,\"address\":null,\"token\":\"testtoken\",\"created_at\":\"2025-03-07T18:50:04.665174\",\"updated_at\":\"2025-03-07T18:50:04.665174\",\"status\":1,\"meta\":[]},\"created_at\":\"2025-03-25T09:29:41.567352\",\"updated_at\":\"2025-03-25T09:29:41.566353\",\"status\":1,\"count\":0,\"lastQuestion\":\"질문내용\"}]}}"
				)
			) 
		),
		@ApiResponse( 
			responseCode = "401-회원id로 세션 조회 실패",
			description = "FAIL", 
			content = @Content(					
				examples = @ExampleObject(
					value = "{\"success\":0,\"response\":{\"message\":\"접근권한이없습니다.\"}}"
				)
			) 
		)
	})
	@GetMapping("/sessions/member/{id}")
	public ResponseEntity<?> getSessionsByMember(
			@Parameter(description = "로그인한 회원 토큰") @RequestHeader("Authorization") String token,
			@Parameter(description = "회원 id") @PathVariable("id") Long mid,
			@Parameter(description = "페이지") @RequestParam(required = false, defaultValue = "0") int p, 
			@Parameter(description = "출력 개수 제한") @RequestParam(required = false, defaultValue = "50") int ol){
		
		//로그인한 회원 조회
		MemberDto loginMember = Commons.findMemberByToken(token, memberService);
		
		//회원이 존재하지 않는 경우
		if(loginMember == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail( messageSource.getMessage("user.invalid_token", null, new Locale("ko")) ));
		}
		
		//<관리자거나 자신의 세션인 경우> - 공개/비공개 세션 조회 가능 & 페이징 적용
		List<ChatSessionDto> sessions = null;
		if(loginMember.getRole().equals(Commons.ROLE_ADMINISTRATOR) || loginMember.getId() == mid ) {
			if(p > 0) sessions = chatSessionService.findAllByMember(mid,p,ol);
			else sessions = chatSessionService.findAllByMember(mid);		
		}	
		else { //<자신의 세션이 아닌 경우> - 공개된 세션인 경우만 조회 가능 & 페이징 적용
			if(p > 0) sessions = chatSessionService.findPublicsByMember(mid,p,ol);
			else sessions = chatSessionService.findPublicsByMember(mid);
		}
		
		return ResponseEntity.status(HttpStatus.OK)
				.body( ResultUtil.success( sessions.stream()
						.map(session -> {
							//세션 주인 메타정보
							MemberDto owner = session.getMemberDto();
							List<MemberMetaResponseDto> metas = memberMetaService.findAll(owner)
																.stream()
																.map(meta -> MemberMetaResponseDto.toResponseDto(meta))
																.toList();
							
							ChatSessionResponseDto responseSession = ChatSessionResponseDto.toDto(session, metas);
							
							//세션의 마지막 질문내용 조회 로직
							ChatQnaDto qna = chatQnaService.findLastQuestionOfSession(session.getId());
							ChatQuestionDto question = qna != null ? chatQuestionService.getQuestion(qna.getChatQuestionDto().getId()) : null;
							String qContent = question != null ? question.getContent() : null;
							responseSession.setLastQuestion( qContent );
							
							return responseSession;
						}).toList() ) );
	}
	
	/**
	 * [공개 세션 조회] & 페이징 적용
	 * @param token 로그인한 회원 토큰
	 * @param p 페이지
	 * @param ol 출력갯수
	 * @return
	 */
	@Operation( summary = "공개 세션 조회", description = "공개 세션 조회 API" )
	@ApiResponses({
		@ApiResponse( 
			responseCode = "200-공개 세션 조회 성공",
			description = "SUCCESS",
			content = @Content(
				array = @ArraySchema(
						schema = @Schema(implementation = ChatSessionResponseDto.class)
				),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":[{\"id\":47,\"member\":{\"id\":37,\"email\":\"abab@naver.com\",\"name\":\"홍길동\",\"nickname\":\"TEMPORARY\",\"birth\":\"2025-03-01\",\"gender\":\"F\",\"contact\":null,\"address\":null,\"token\":\"testtoken\",\"created_at\":\"2025-03-07T18:50:04.665174\",\"updated_at\":\"2025-03-07T18:50:04.665174\",\"status\":1,\"meta\":[]},\"created_at\":\"2025-03-13T14:09:06.679719\",\"updated_at\":\"2025-03-13T14:09:06.665218\",\"status\":0,\"count\":0,\"lastQuestion\":null},{\"id\":46,\"member\":{\"id\":37,\"email\":\"abab@naver.com\",\"name\":\"홍길동\",\"nickname\":\"TEMPORARY\",\"birth\":\"2025-03-01\",\"gender\":\"F\",\"contact\":null,\"address\":null,\"token\":\"testtoken\",\"created_at\":\"2025-03-07T18:50:04.665174\",\"updated_at\":\"2025-03-07T18:50:04.665174\",\"status\":1,\"meta\":[]},\"created_at\":\"2025-03-13T09:32:52.674851\",\"updated_at\":\"2025-03-13T09:32:52.671372\",\"status\":0,\"count\":0,\"lastQuestion\":null}]}}"
				)
			) 
		)
	})
	@GetMapping("/sessions/public")
	public ResponseEntity<?> getPublicSessions(
			@Parameter(description = "페이지") @RequestParam(required = false, defaultValue = "0") int p, 
			@Parameter(description = "출력 개수 제한") @RequestParam(required = false, defaultValue = "50") int ol){
		
		List<ChatSessionDto> publicSessions = null;
		
		//공개 세션 조회
		if(p > 0)
			publicSessions = chatSessionService.findPublics(p,ol);
		else 
			publicSessions = chatSessionService.findPublics();

		List<ChatSessionResponseDto> sessionResponses = new Vector<>();
		
		for(ChatSessionDto session : publicSessions) {

			//세션의 마지막 질문내용 조회 로직
			ChatQnaDto qna = chatQnaService.findLastQuestionOfSession(session.getId());
			ChatQuestionDto question = qna != null ? chatQuestionService.getQuestion(qna.getChatQuestionDto().getId()) : null;
			String qContent = question != null ? question.getContent() : null;
			
			//세션 주인 메타정보
			MemberDto owner = session.getMemberDto();
			List<MemberMetaResponseDto> metas = memberMetaService.findAll(owner)
												.stream()
												.map(meta -> MemberMetaResponseDto.toResponseDto(meta))
												.toList();
			
			ChatSessionResponseDto responseSession = ChatSessionResponseDto.toDto(session,metas);
			
			//마지막 질문 설정
			responseSession.setLastQuestion( qContent );
			
			sessionResponses.add(responseSession);
		}
			
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(ResultUtil.success(sessionResponses));
	}
	
	/**
	 * [세션 status 변경] - 관리자 또는 본인의 세션만 상태 변경 가능
	 * @param token 로그인한 회원 토큰
	 * @param params 세션 status 변경 요청 객체(id,status)
	 * @return
	 */
	@Operation( summary = "세션 status 변경", description = "세션 status 변경 API" )
	@ApiResponses({
		@ApiResponse( 
			responseCode = "200-세션 status 변경 성공",
			description = "SUCCESS",
			content = @Content(
				schema = @Schema(implementation = ChatSessionResponseDto.class),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":{\"id\":59,\"member\":{\"id\":73,\"email\":\"admin@gmail.com\",\"name\":\"관리자\",\"nickname\":\"admin\",\"birth\":\"2025-03-29\",\"gender\":\"F\",\"contact\":\"01000000000\",\"address\":\"서울시강남구\",\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU5JU1RSQVRPUiIsImVtYWlsIjoiYWRtaW5AZ21haWwuY29tIiwic3ViIjoiNzMiLCJpYXQiOjE3NDM1OTEyNDAsImV4cCI6MTc0MzU5MjE0MH0.lp4Lho1Ozm8oN-HfLqoLrsh_zdWbx2_nL8w3A3WoAK8\",\"created_at\":\"2025-03-29T18:41:15.915\",\"updated_at\":\"2025-03-29T18:41:15.915\",\"status\":1,\"meta\":[]},\"created_at\":\"2025-03-29T19:00:54.790221\",\"updated_at\":\"2025-03-29T19:00:54.787217\",\"status\":0,\"count\":0,\"lastQuestion\":null}}}"
				)
			) 
		),
		@ApiResponse( 
				responseCode = "400-세션 status 변경 실패",
				description = "FAIL", 
				content = @Content(					
					examples = @ExampleObject(
						value = "{\"success\":0,\"response\":{\"message\":\"세션을찾을수없습니다.\"}}"
					)
				) 
			),
		@ApiResponse( 
			responseCode = "401-세션 status 변경 실패",
			description = "FAIL", 
			content = @Content(					
				examples = @ExampleObject(
					value = "{\"success\":0,\"response\":{\"message\":\"접근권한이없습니다.\"}}"
				)
			) 
		),
		@ApiResponse( 
				responseCode = "403-세션 status 변경 실패",
				description = "FAIL", 
				content = @Content(					
					examples = @ExampleObject(
						value = "{\"success\":0,\"response\":{\"message\":\"이작업을수행할권한이없습니다.\"}}"
					)
				) 
			)
	})
	@PatchMapping("/sessions")
	public ResponseEntity<?> changeSessionStatus(
			@Parameter(description = "로그인한 회원 토큰") @RequestHeader("Authorization") String token,
			@Parameter(description = "세션 status 변경 요청 객체") @RequestBody Map<String, Long> params){
		//로그인한 회원 조회
		MemberDto loginMember = Commons.findMemberByToken(token, memberService);
		
		//회원이 존재하지 않는 경우
		if(loginMember == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail( messageSource.getMessage("user.invalid_token", null, new Locale("ko")) ));
		}
		
		Long sid = params.get("id"); //세션id
		long status = params.get("status"); //요청하는 세션status
		
		ChatSessionDto findedSession = chatSessionService.findById(sid);
		//세션 미존재
		if(findedSession == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultUtil.fail( messageSource.getMessage("session.notfound", null,new Locale("ko")) ));
		}
		//관리자나 본인 세션이 아닌 경우
		if( ( !loginMember.getRole().equals(Commons.ROLE_ADMINISTRATOR) && findedSession.getMemberDto().getId() != loginMember.getId() ) || !loginMember.getRole().equals(Commons.ROLE_ADMINISTRATOR) ) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResultUtil.fail( messageSource.getMessage("user.invalid_role", null,new Locale("ko")) ));
		}
		
		findedSession.setStatus(status);
		ChatSessionDto updatedSession = chatSessionService.updateSession(findedSession);
		
		//세션 주인 메타정보
		List<MemberMetaResponseDto> metas = memberMetaService.findAll(updatedSession.getMemberDto())
											.stream()
											.map(meta -> MemberMetaResponseDto.toResponseDto(meta))
											.toList();
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(
						ResultUtil.success(ChatSessionResponseDto.toDto(updatedSession,metas)));
	}
	
}
