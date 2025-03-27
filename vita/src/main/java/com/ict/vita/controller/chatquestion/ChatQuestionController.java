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
						value = "{\"success\":1,\"response\":{\"data\":{\"sid\":45,\"question\":\"2222질문!!!!\",\"answers\":[{\"id\":60394,\"file_name\":\"HC-A-06137307000394\",\"intro\":\"에이즈는인체면역결핍바이러스인HIV에의해인체내에서일어나는감염성질환을의미합니다.HIV는주로성접촉을통해전파되며,일부경우에는혈액을통한전파가가능합니다.HIV는HIV-1,HIV-2,HIV-3세가지유형으로나뉘며,각각다른바이러스로분류됩니다.HIV는인체간에감염이이루어지며,이로인해심각한합병증이발생할수있습니다.\",\"body\":\"HIV는주로감염된사람의정액또는혈액을통해전파됩니다.이바이러스는감염된사람이기침,재채기,말하기등을통해다른사람에게도전파될수있습니다.이로인해감염된사람은심각한바이러스질환과감염된물질에노출될수있습니다.HIV는후천성면역결핍증을일으키는원인바이러스이며,이를제거하기위해HIV의유전자를포함한혈액이나체액을제거하는치료가시행됩니다.HIV는HIV-1과HIV-2유형의바이러스로나뉘며,HIV-1유형은HIV-1a바이러스를포함하고있으며,HIV-2유형은HIV-2b바이러스를포함하고있습니다.치료방법은약물치료와외과수술로나뉩니다.감염된조직의사용은혈액내바이러스에대한면역을강화시키기위해사용될수있으며,이러한치료법은HIV바이러스의지속적인복제를방지하기위해시행됩니다.\",\"conclusion\":\"HIV감염을예방하기위해서는HIV-1유형의바이러스에대한백신접종이중요합니다.HIV-1a감염은성적접촉을통해감염될수있으며,HIV-1b,HIV-2,HIV-3,HIV-4유형으로나뉘어집니다.HIV-1b유형에감염된사람은생식기를통해HIV바이러스를전파하여심각한합병증을유발할수있습니다.HIV-1b유형감염자는적절한치료를받지않을경우생명을위협받을수있습니다.또한,HIV-1a감염자는면역기능이심각하게손상되어감염으로부터의회복이어려울수있으며,심각한사망률이발생할수있습니다.따라서HIV-1b유형에감염되지않도록정기적인검사와조치가필요합니다.\",\"categories\":[{\"id\":1630,\"name\":\"답변\",\"slug\":\"answer\",\"group_number\":0,\"category\":\"chat\",\"description\":null,\"count\":0,\"parent\":0},{\"id\":825,\"name\":\"감염성질환\",\"slug\":\"%EA%B0%90%EC%97%BC%EC%84%B1%EC%A7%88%ED%99%98\",\"group_number\":0,\"category\":\"disease\",\"description\":null,\"count\":0,\"parent\":0},{\"id\":826,\"name\":\"HIV감염\",\"slug\":\"hiv_infection\",\"group_number\":0,\"category\":\"disease\",\"description\":null,\"count\":0,\"parent\":825},{\"id\":1547,\"name\":\"피부과\",\"slug\":\"%ED%94%BC%EB%B6%80%EA%B3%BC\",\"group_number\":0,\"category\":\"department\",\"description\":null,\"count\":0,\"parent\":0},{\"id\":1625,\"name\":\"정의\",\"slug\":\"%EC%A0%95%EC%9D%98\",\"group_number\":0,\"category\":\"intention\",\"description\":null,\"count\":0,\"parent\":0}]}]}}}"
					)
				) 
			),
		@ApiResponse( 
				responseCode = "400-질문&세션 생성 실패",
				description = "FAIL",
				content = @Content(	
					examples = @ExampleObject(
						value = "{\"success\":0,\"response\":{\"message\":\"요청파라메터가올바르지않습니다.\"}}"
					)
				) 
			),
		@ApiResponse( 
			responseCode = "401-질문&세션 생성 실패",
			description = "FAIL(회원 미존재)", 
			content = @Content(					
				examples = @ExampleObject(
					value = "{\"success\":0,\"response\":{\"message\":\"접근권한이없습니다.\"}}"
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
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail( messageSource.getMessage("user.invalid_token", null, new Locale("ko"))));
		}
		
		//질문내용과 질문 키워드가 전달 안 된 경우
		if(qwsDto.getContents() == null || qwsDto.getKeywords() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultUtil.fail( messageSource.getMessage("request.invalid_parameters", null, new Locale("ko")) ));
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
		ChatQuestionWithSessionResponseDto qwsResponseDto = ChatQuestionWithSessionResponseDto.builder().sid(sid).question(qwsDto.getContents()).answers(answerResponses).build();
		
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
		
		//세션 수정일 변경
		sessionDto.setUpdated_at(LocalDateTime.now());
		chatSessionService.updateSession(sessionDto);
		
		//세션id와 검색한 답변 리스트 반환
		return ResponseEntity.status(HttpStatus.CREATED).body(ResultUtil.success(qwsResponseDto));

	}
	
	
}
