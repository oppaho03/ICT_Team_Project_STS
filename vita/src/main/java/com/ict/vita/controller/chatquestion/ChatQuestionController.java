package com.ict.vita.controller.chatquestion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.anc.AncDto;
import com.ict.vita.service.anc.AncService;
import com.ict.vita.service.chatanswer.ChatAnswerDto;
import com.ict.vita.service.chatanswer.ChatAnswerResponseDto;
import com.ict.vita.service.chatanswer.ChatAnswerService;
import com.ict.vita.service.chatqna.ChatQnaDto;
import com.ict.vita.service.chatqna.ChatQnaService;
import com.ict.vita.service.chatquestion.ChatQuestionDto;
import com.ict.vita.service.chatquestion.ChatQuestionService;
import com.ict.vita.service.chatquestion.ChatQuestionWithSessionRequestDto;
import com.ict.vita.service.chatquestion.ChatQuestionWithSessionResponseDto;
import com.ict.vita.service.chatsession.ChatSessionDto;
import com.ict.vita.service.chatsession.ChatSessionService;
import com.ict.vita.service.member.MemberDto;
import com.ict.vita.service.member.MemberService;
import com.ict.vita.service.termcategory.TermCategoryDto;
import com.ict.vita.service.terms.TermsResponseDto;
import com.ict.vita.util.Commons;
import com.ict.vita.util.ResultUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
public class ChatQuestionController {
	//서비스 주입
	private final ChatQuestionService chatQuestionService;
	private final ChatSessionService chatSessionService;
	private final ChatAnswerService chatAnswerService;
	private final MemberService memberService;
	private final ChatQnaService chatQnaService;
	private final AncService ancService;
	
	private final MessageSource messageSource;
	
	/**
	 * [질문과 세션 생성]
	 * @param qwsDto 질문과 세션에 대한 정보를 담고있는 DTO 객체(키워드 정보 포함돼 있음)
	 * @param token 로그인한 회원의 토큰값
	 * @return ResponseEntity
	 */
	@Operation( summary = "질문&세션 생성", description = "질문&세션 생성 API" )
	@ApiResponses({
		@ApiResponse( 
			responseCode = "200-답변 검색 결과가 없음",
			description = "SUCCESS",
			content = @Content(	
				schema = @Schema(implementation = MemberDto.class),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":null}}"
				)
			) 
		),
		@ApiResponse( 
				responseCode = "201-질문&세션 생성 성공",
				description = "SUCCESS",
				content = @Content(	
					schema = @Schema(implementation = ChatQuestionWithSessionResponseDto.class),
					examples = @ExampleObject(
						value = "{\"success\":1,\"response\":{\"data\":{\"sid\":43,\"answers\":[{\"id\":394,\"file_name\":\"HC-A-06137307000394\",\"intro\":\"에이즈는~~\",\"body\":\"HIV는~~\",\"conclusion\":\"HIV감염을~~\"}]}}}"
					)
				) 
			),
		@ApiResponse( 
			responseCode = "401-질문&세션 생성 실패",
			description = "FAIL", 
			content = @Content(					
				examples = @ExampleObject(
					value = "{ \"success\": 0, \"response\": { \"message\": \"유효하지 않은 토큰입니다\"  } }" 
				)
			) 
		)
	})
	@PostMapping("/chatquestions")
	public ResponseEntity<?> createQuestionWithSession(
			@Parameter(description = "질문과 세션에 대한 정보") @RequestBody ChatQuestionWithSessionRequestDto qwsDto,
			@Parameter(description = "회원의 토큰값") @RequestHeader(name = "Authorization") String token){
		//회원의 토큰값 조회
		MemberDto findedMember = Commons.findMemberByToken(token, memberService);
		//<회원 토큰값이 존재하지 않는 경우>
		if(findedMember == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultUtil.fail( messageSource.getMessage("user.invalid_token", null, new Locale("ko"))));
		}
		
		//<회원 토큰값이 존재하는 경우>
		Long sid;
		ChatSessionDto sessionDto;
		//세션의 존재여부 확인
		//세션id가 안 넘어온 경우
		if(qwsDto.getSid() == null) { 
			//세션 생성
			ChatSessionDto sDto = ChatSessionDto.builder()
									.memberDto(findedMember)
									.created_at(LocalDateTime.now())
									.updated_at(LocalDateTime.now())
									.status(1)
									.count(0)
									.build();
			sessionDto = chatSessionService.createSession(sDto);
			sid = sessionDto.getId();
		}
		//세션id가 넘어온 경우
		else {
			sid = qwsDto.getSid();
			sessionDto = chatSessionService.findById(sid);
		}	
		
		
		if ( sessionDto == null || (sessionDto.getMemberDto().getId() != findedMember.getId() && sessionDto.getStatus() == 1 ) )
			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(null));
		
		//질문의 키워드로 답변 검색
		List <String> keywords = qwsDto.getKeywords();
		List<ChatAnswerDto> answers = chatAnswerService.findAnswerByKeywords(keywords.stream().collect(Collectors.joining(" OR ")));
		
		//<답변 검색 결과가 없는 경우>
		if(answers.size() == 0) {
			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(null));
		}
		
		//<답변 검색 결과가 있는 경우>
		
		//질문 DTO 객체 생성
		ChatQuestionDto questionDto = ChatQuestionDto.builder().content(qwsDto.getContents()).created_at(LocalDateTime.now()).build();
		
		//질문 저장
		ChatQuestionDto createdQDto = chatQuestionService.save(questionDto);
		System.out.println("===== 질문:"+createdQDto.getId());
		
		List<ChatAnswerResponseDto> answerResponses = new Vector<>();
		List<TermCategoryDto> categories = new Vector<>();
		
		for(ChatAnswerDto answerDto : answers) {
			List<AncDto> ancs = ancService.findAllByAnswerId(answerDto.getId());
			categories.addAll( ancs.stream().map(anc -> anc.getTermCategoryDto()).collect(Collectors.toList()) );
			List<TermsResponseDto> termsResponses = categories.stream().map(cate -> TermsResponseDto.toDto(cate)).collect(Collectors.toList());
			answerResponses.add(ChatAnswerResponseDto.toDto(answerDto, termsResponses));
		}

		//질문에 대한 키워드 검색한 답변 DTO객체
		ChatQuestionWithSessionResponseDto qwsResponseDto = ChatQuestionWithSessionResponseDto.builder().sid(sid).answers(answerResponses).build();
		
		
		//<ChatQna에 저장>
		//답변이 1개인 경우
		if(answers.size() == 1) {
			ChatAnswerResponseDto answerDto = qwsResponseDto.getAnswers().get(0);
			ChatQnaDto qnaDto = ChatQnaDto.builder()
										.chatSessionDto(sessionDto)
										.chatQuestionDto(createdQDto)
										.chatAnswerDto(answerDto.toAnswerDto())
										.is_matched(1) //매칭여부를 1로(매칭됨)
										.build();
			//ChatQnaDto객체 저장
			ChatQnaDto savedQnaDto = chatQnaService.save(qnaDto);
			System.out.println("+++++ qna 1개:"+savedQnaDto.getId());
		} 
		else { //답변이 2개인 경우
			for(ChatAnswerDto aDto:answers) {
				ChatQnaDto qnaDto = ChatQnaDto.builder()
											.chatSessionDto(sessionDto)
											.chatQuestionDto(createdQDto)
											.chatAnswerDto(aDto)
											.is_matched(0) //매칭여부를 0로(매칭X)
											.build();
				//ChatQnaDto객체 저장
				ChatQnaDto savedQnaDto = chatQnaService.save(qnaDto);
				System.out.println("+++++ qna 2개:"+savedQnaDto.getId());
				
			} // end for
		}
		
		//세션id와 검색한 답변 리스트 반환
		return ResponseEntity.status(HttpStatus.CREATED).body(ResultUtil.success(qwsResponseDto));

	}
	
	
}
