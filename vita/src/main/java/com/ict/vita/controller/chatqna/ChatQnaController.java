package com.ict.vita.controller.chatqna;

import java.util.List;
import java.util.Locale;
import java.util.Vector;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.anc.AncDto;
import com.ict.vita.service.anc.AncService;
import com.ict.vita.service.chatanswer.ChatAnswerResponseDto;
import com.ict.vita.service.chatqna.ChatQnaDto;
import com.ict.vita.service.chatqna.ChatQnaResponseDto;
import com.ict.vita.service.chatqna.ChatQnaService;
import com.ict.vita.service.chatquestion.ChatQuestionDto;
import com.ict.vita.service.chatquestion.ChatQuestionService;
import com.ict.vita.service.chatsession.ChatSessionDto;
import com.ict.vita.service.chatsession.ChatSessionService;
import com.ict.vita.service.member.MemberDto;
import com.ict.vita.service.member.MemberService;
import com.ict.vita.service.membermeta.MemberMetaDto;
import com.ict.vita.service.membermeta.MemberMetaResponseDto;
import com.ict.vita.service.membermeta.MemberMetaService;
import com.ict.vita.service.termcategory.TermCategoryDto;
import com.ict.vita.service.terms.TermsResponseDto;
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
public class ChatQnaController {
	//서비스 주입
	private final ChatQnaService chatQnaService;
	private final MemberService memberService;
	private final ChatSessionService chatSessionService;
	private final ChatQuestionService chatQuestionService;
	private final AncService ancService;
	private final MemberMetaService memberMetaService;
	
	private final MessageSource messageSource;
	
	/**
	 * [세션id(PK)로 QNA테이블에서 질문-답변 쌍 조회 (페이징 적용)]
	 * @param token 로그인한 회원의 토큰값
	 * @param sid 세션id(PK)로 쿼리파라미터로 넘어옴
	 * @param p 페이지
	 * @param ol 출력갯수
	 * @return
	 */
	@Operation( summary = "질문-답변 쌍 조회", description = "질문-답변 쌍 조회 API" )
	@ApiResponses({
		@ApiResponse( 
			responseCode = "200-조회 성공",
			description = "SUCCESS", 
			content = @Content(	
				array = @ArraySchema(
						schema = @Schema(implementation = ChatQnaResponseDto.class)
				),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":[{\"id\":79,\"session\":{\"id\":50,\"member\":{\"id\":47,\"email\":\"baekjongwon1@naver.com\",\"name\":\"박종원원\",\"nickname\":\"빽다방\",\"birth\":\"1916-03-15\",\"gender\":\"M\",\"contact\":null,\"address\":null,\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVVNFUiIsImVtYWlsIjoiYmFla2pvbmd3b24xQG5hdmVyLmNvbSIsInN1YiI6IjQ3IiwiaWF0IjoxNzQyMTcwNzM1LCJleHAiOjE3NDIxNzE2MzV9.evCR4S-62afBMxdsoJ_QxqwIfwm4tqCbJXtCHuQzkFI\",\"created_at\":\"2025-03-15T13:58:21.560432\",\"updated_at\":\"2025-03-17T09:32:32.773729\",\"status\":1,\"meta\":[]},\"created_at\":\"2025-03-17T10:34:22.946939\",\"updated_at\":\"2025-03-17T10:34:22.930933\",\"status\":1,\"count\":0,\"lastQuestion\":\"심심한질문\"},\"question\":{\"id\":40,\"content\":\"심심한질문\",\"created_at\":\"2025-03-20T20:46:39.434809\"},\"answer\":{\"id\":73027,\"file_name\":\"HC-A-02529874013027\",\"intro\":\"고막염은이가질환으로,고막에구멍이생기는염증이발생하는질환이다.이질환은일반적으로감기와같은상기도감염,바이러스성상기도감염,인플루엔자성상기도감염,그리고알레르기성비염,아데노이드비대증등의다양한원인으로발생할수있다.이중에서가장흔한감염경로는상기도감염인데,감기의바이러스에의해유발될수있다.또한,상기도감염과같이바이러스에의해이가감염되는경우에도고막염이발생할수있다.또한,인두나후두에감염이되어고막염이발생하기도한다.그외에도알레르기성비염이나아데노이드비대증의경우에도고막염이발생할수있다.\",\"body\":\"고막염은이가질환으로,고막에구멍이생기는염증이발생하는질환이다.이러한구멍은감기나상기도감염,바이러스성상기도감염,인플루엔자성상기도감염,그리고알레르기성비염등다양한원인에의해유발될수있다.가장흔한감염경로는상기도감염으로,이에의한바이러스감염이가장흔하다.또한,인두나후두에감염이되어고막염이발생할수도있다.그외에도알레르기성비염이나아데노이드비대증의경우에도고막염이나타날수있다.\",\"conclusion\":\"고막염은이가질환으로,고막에구멍이생기는염증이발생하는질환이다.주로감기와같은상기도감염,바이러스성상기도감염,인플루엔자성상기도감염,그리고알레르기성비염이나아데노이드비대증과같은바이러스감염이원인이다.이러한질환들의가장큰원인은바이러스감염이다.\",\"categories\":[{\"id\":1630,\"name\":\"답변\",\"slug\":\"answer\",\"group_number\":0,\"category\":\"chat\",\"description\":null,\"count\":0,\"parent\":0},{\"id\":861,\"name\":\"귀코목질환\",\"slug\":\"%EA%B7%80%EC%BD%94%EB%AA%A9%EC%A7%88%ED%99%98\",\"group_number\":0,\"category\":\"disease\",\"description\":null,\"count\":0,\"parent\":0},{\"id\":862,\"name\":\"고막염\",\"slug\":\"myringitis\",\"group_number\":0,\"category\":\"disease\",\"description\":null,\"count\":0,\"parent\":861},{\"id\":1559,\"name\":\"이비인후과\",\"slug\":\"%EC%9D%B4%EB%B9%84%EC%9D%B8%ED%9B%84%EA%B3%BC\",\"group_number\":0,\"category\":\"department\",\"description\":null,\"count\":0,\"parent\":0},{\"id\":1623,\"name\":\"원인\",\"slug\":\"%EC%9B%90%EC%9D%B8\",\"group_number\":0,\"category\":\"intention\",\"description\":null,\"count\":0,\"parent\":0}]},\"is_matched\":0},{\"id\":78,\"session\":{\"id\":50,\"member\":{\"id\":47,\"email\":\"baekjongwon1@naver.com\",\"name\":\"박종원원\",\"nickname\":\"빽다방\",\"birth\":\"1916-03-15\",\"gender\":\"M\",\"contact\":null,\"address\":null,\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVVNFUiIsImVtYWlsIjoiYmFla2pvbmd3b24xQG5hdmVyLmNvbSIsInN1YiI6IjQ3IiwiaWF0IjoxNzQyMTcwNzM1LCJleHAiOjE3NDIxNzE2MzV9.evCR4S-62afBMxdsoJ_QxqwIfwm4tqCbJXtCHuQzkFI\",\"created_at\":\"2025-03-15T13:58:21.560432\",\"updated_at\":\"2025-03-17T09:32:32.773729\",\"status\":1,\"meta\":[]},\"created_at\":\"2025-03-17T10:34:22.946939\",\"updated_at\":\"2025-03-17T10:34:22.930933\",\"status\":1,\"count\":0,\"lastQuestion\":\"심심한질문\"},\"question\":{\"id\":40,\"content\":\"심심한질문\",\"created_at\":\"2025-03-20T20:46:39.434809\"},\"answer\":{\"id\":72704,\"file_name\":\"HC-A-06055442012704\",\"intro\":\"후천성면역결핍증(HIV)은HIV에감염된후면역기능이저하되어다양한감염질환에걸릴가능성이높아집니다.이에따라HIV에대한증상및질병의조기발견이매우중요합니다.HIV감염여부를확인하기위해서는혈액검사를통해HIV항체(HighImmunodeficiencyVirus-associatedImmune-Cell-InactiveVirus)검사를수행합니다.HIV에감염되었을경우,혈액중HIV항체의농도가높을수록감염위험이높아집니다.또한,HIV감염후에는면역기능이저하되어다양한감염질환에걸리기쉬워집니다.따라서,HIV감염여부를조기에확인하기위해혈액검사를실시하는것이매우중요합니다.\",\"body\":\"HIV항체검사에서는양성또는음성이나온경우,HIV감염여부를확인할수있습니다.혈액검사결과가양성이면감염을의심할수있으며,필요한경우HIV감염여부를확인하기위해조직검사를실시할수있습니다.이러한검사는항체검사의결과를기반으로감염여부를확인하고,적절한치료와관리를통해합병증을예방하는데도움을줍니다.\",\"conclusion\":\"HIV감염을예방하기위해서는HIV감염자와의성적접촉을피하고,안전한성적접촉및예방백신접종을통해감염을예방하는것이중요합니다.HIV감염여부를조기에확인하기위해혈액검사와HIV감염확인을위한조직검사를받는것이필수적입니다.\",\"categories\":[{\"id\":1630,\"name\":\"답변\",\"slug\":\"answer\",\"group_number\":0,\"category\":\"chat\",\"description\":null,\"count\":0,\"parent\":0},{\"id\":825,\"name\":\"감염성질환\",\"slug\":\"%EA%B0%90%EC%97%BC%EC%84%B1%EC%A7%88%ED%99%98\",\"group_number\":0,\"category\":\"disease\",\"description\":null,\"count\":0,\"parent\":0},{\"id\":860,\"name\":\"후천성면역결핍증\",\"slug\":\"acquired_immunodeficiency_syndrome\",\"group_number\":0,\"category\":\"disease\",\"description\":null,\"count\":0,\"parent\":825},{\"id\":1547,\"name\":\"피부과\",\"slug\":\"%ED%94%BC%EB%B6%80%EA%B3%BC\",\"group_number\":0,\"category\":\"department\",\"description\":null,\"count\":0,\"parent\":0},{\"id\":1627,\"name\":\"진단\",\"slug\":\"%EC%A7%84%EB%8B%A8\",\"group_number\":0,\"category\":\"intention\",\"description\":null,\"count\":0,\"parent\":0}]},\"is_matched\":0}]}}"
				)
			) 
		),
		@ApiResponse( 
				responseCode = "400-조회 실패",
				description = "FAIL", 
				content = @Content(					
						examples = @ExampleObject(
							value = "{\"success\":0,\"response\":{\"message\":\"세션을찾을수없습니다.\"}}"
						)
				) 
			),
		@ApiResponse( 
			responseCode = "401-조회 실패",
			description = "FAIL", 
			content = @Content(					
				examples = @ExampleObject(
					value = "{\"success\":0,\"response\":{\"message\":\"접근권한이없습니다.\"}}"
				)
			) 
		),
		@ApiResponse( 
				responseCode = "403-조회 실패",
				description = "FAIL", 
				content = @Content(					
					examples = @ExampleObject(
						value = "{\"success\":0,\"response\":{\"message\":\"이작업을수행할권한이없습니다.\"}}"
					)
				) 
			)
	})
	@GetMapping("/qna")
	public ResponseEntity<?> getQna(
			@Parameter(description = "회원의 토큰값") @RequestHeader(name = "Authorization") String token,
			@Parameter(description = "세션 아이디") @RequestParam(name = "sid") Long sid,
			@Parameter(description = "페이지") @RequestParam(required = false, defaultValue = "0") int p, 
			@Parameter(description = "출력 개수 제한") @RequestParam(required = false, defaultValue = "50") int ol) {
		
		MemberDto loginMember = Commons.findMemberByToken(token, memberService);
		
		//<찾은 회원이 존재하지 않는 경우>
		if(loginMember == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail( messageSource.getMessage("user.invalid_token", null, new Locale("ko")) ));
		}
		
		//<찾은 회원이 존재하는 경우>		
		ChatSessionDto session = chatSessionService.findById(sid);
		
		//세션 존재하지 않을때
		if(session == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultUtil.fail( messageSource.getMessage("session.notfound", null, new Locale("ko")) ));
		
		MemberDto sessionMember = session.getMemberDto(); //세션 주인
		
		//본인 세션이 아니거나 관리자가 아니거나 비공개 세션인 경우
		if( ( !loginMember.getRole().equals(Commons.ROLE_ADMINISTRATOR) && loginMember.getId() != sessionMember.getId() ) && (session.getStatus() == 1) ) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResultUtil.fail( messageSource.getMessage("user.invalid_role", null, new Locale("ko")) ));
		}
		
		if(p < 2) {
			//세션의 count 증가
			session.setCount(session.getCount() + 1);
			session = chatSessionService.updateSession(session);
		}
		
		//세션id에 해당하는 qna
		List<ChatQnaDto> findedQna;
		
		//페이징 
		if(p > 0) findedQna = chatQnaService.findAllBySession(sid,p,ol);
		else findedQna = chatQnaService.findAllBySession(sid);		
		
		List<ChatQnaResponseDto> result = new Vector<>(); //반환값
		
		//세션 주인의 메타정보
		List <MemberMetaDto> metas = memberMetaService.findAll(sessionMember);
		List<MemberMetaResponseDto> metaResponses = metas.stream().map(meta -> MemberMetaResponseDto.toResponseDto(meta)).toList();
		
		for(ChatQnaDto dto: findedQna) {
			
			//세션의 마지막 질문내용 조회 로직
			ChatQnaDto qna = chatQnaService.findLastQuestionOfSession(session.getId());
			ChatQuestionDto question = qna != null ? chatQuestionService.getQuestion(qna.getChatQuestionDto().getId()) : null;
			String qContent = question != null ? question.getContent() : null;
			
			//카테고리 정보
			List<TermsResponseDto> categories = new Vector<>();
			List <TermCategoryDto> categoryDtos = ancService.findAllByAnswerId(dto.getChatAnswerDto().getId()).stream().map(anc -> anc.getTermCategoryDto()).toList();
			categories = categoryDtos.stream().map(cate -> TermsResponseDto.toDto(cate)).toList();

			ChatQnaResponseDto responseSession = ChatQnaResponseDto.toDto(dto,session,qContent,categories, metaResponses);

			result.add(responseSession);	
			
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(result));
	}
	
}
