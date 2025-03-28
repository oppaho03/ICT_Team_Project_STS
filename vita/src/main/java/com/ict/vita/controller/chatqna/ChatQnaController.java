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
import com.ict.vita.service.member.MemberService;
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
					value = "{\"success\":1,\"response\":{\"data\":[{\"id\":76,\"session\":{\"id\":50,\"member\":47,\"created_at\":\"2025-03-17T10:34:22.946939\",\"updated_at\":\"2025-03-17T10:34:22.930933\",\"status\":1,\"count\":0,\"lastQuestion\":\"심심한질문\"},\"question\":{\"id\":39,\"content\":\"심심한질문\",\"created_at\":\"2025-03-20T20:45:29.279478\"},\"answer\":{\"id\":67267,\"file_name\":\"HC-A-04035342007267\",\"intro\":\"심내막염은심장내의심내막에발생하는염증을의미합니다.\",\"body\":\"심내막염은일반적으로심부전을일으키는흔한질환으로,폐나심장을침범하는세균감염으로인해발생합니다.세균은심장판막을침범하여심장의기능을손상시키며,심장기능저하로인해심부전이발생할수있습니다.심내막염은치료하지않을경우심근염,판막손상등의합병증을일으킬수있습니다.이러한합병증이발생하면심장기능이저하되고,사망률이높아집니다.심근염으로인한사망률이낮기때문에많은사람들이치료를받지않고그냥방치하는경우가많습니다.그러나심근염은심장판막에염증을일으키는심장판막질환으로,치료를받지않으면심부전이발병할확률이더높아집니다.또한,치료하지않을경우심장판막손상이발생하여심근이파괴되어심부전이나판막역류등의합병증이발생할수있습니다.\",\"conclusion\":\"심내막염을예방하기위해정기적으로심장초음파,심초음파등의검사를받아야합니다.검사를받지않을경우심내막염의합병증이발생하며,특히심장판막의폐쇄부전은심부전을일으킬수있습니다.이러한경우심장초음파검사를통해판막의상태를파악하고,판막협착정도를모니터링하여적절한치료계획을수립할수있습니다.심내막염의치료를위해감염된부위를깨끗하게치료하고,염증을억제하기위해약물을사용합니다.항생제와항진균제를포함한적절한항생제치료를시행하며,정기적인심장초음파검사를받아야합니다.심근염이나판막역류등심각한합병증이발생할경우에는심장판막을교체해야합니다.\"},\"is_matched\":1},{\"id\":77,\"session\":{\"id\":50,\"member\":47,\"created_at\":\"2025-03-17T10:34:22.946939\",\"updated_at\":\"2025-03-17T10:34:22.930933\",\"status\":1,\"count\":0,\"lastQuestion\":\"심심한질문\"},\"question\":{\"id\":39,\"content\":\"심심한질문\",\"created_at\":\"2025-03-20T20:45:29.279478\"},\"answer\":{\"id\":67267,\"file_name\":\"HC-A-04035342007267\",\"intro\":\"심내막염은심장내의심내막에발생하는염증을의미합니다.\",\"body\":\"심내막염은일반적으로심부전을일으키는흔한질환으로,폐나심장을침범하는세균감염으로인해발생합니다.세균은심장판막을침범하여심장의기능을손상시키며,심장기능저하로인해심부전이발생할수있습니다.심내막염은치료하지않을경우심근염,판막손상등의합병증을일으킬수있습니다.이러한합병증이발생하면심장기능이저하되고,사망률이높아집니다.심근염으로인한사망률이낮기때문에많은사람들이치료를받지않고그냥방치하는경우가많습니다.그러나심근염은심장판막에염증을일으키는심장판막질환으로,치료를받지않으면심부전이발병할확률이더높아집니다.또한,치료하지않을경우심장판막손상이발생하여심근이파괴되어심부전이나판막역류등의합병증이발생할수있습니다.\",\"conclusion\":\"심내막염을예방하기위해정기적으로심장초음파,심초음파등의검사를받아야합니다.검사를받지않을경우심내막염의합병증이발생하며,특히심장판막의폐쇄부전은심부전을일으킬수있습니다.이러한경우심장초음파검사를통해판막의상태를파악하고,판막협착정도를모니터링하여적절한치료계획을수립할수있습니다.심내막염의치료를위해감염된부위를깨끗하게치료하고,염증을억제하기위해약물을사용합니다.항생제와항진균제를포함한적절한항생제치료를시행하며,정기적인심장초음파검사를받아야합니다.심근염이나판막역류등심각한합병증이발생할경우에는심장판막을교체해야합니다.\"},\"is_matched\":0},{\"id\":78,\"session\":{\"id\":50,\"member\":47,\"created_at\":\"2025-03-17T10:34:22.946939\",\"updated_at\":\"2025-03-17T10:34:22.930933\",\"status\":1,\"count\":0,\"lastQuestion\":\"심심한질문\"},\"question\":{\"id\":40,\"content\":\"심심한질문\",\"created_at\":\"2025-03-20T20:46:39.434809\"},\"answer\":{\"id\":72704,\"file_name\":\"HC-A-06055442012704\",\"intro\":\"후천성면역결핍증(HIV)은HIV에감염된후면역기능이저하되어다양한감염질환에걸릴가능성이높아집니다.이에따라HIV에대한증상및질병의조기발견이매우중요합니다.HIV감염여부를확인하기위해서는혈액검사를통해HIV항체(HighImmunodeficiencyVirus-associatedImmune-Cell-InactiveVirus)검사를수행합니다.HIV에감염되었을경우,혈액중HIV항체의농도가높을수록감염위험이높아집니다.또한,HIV감염후에는면역기능이저하되어다양한감염질환에걸리기쉬워집니다.따라서,HIV감염여부를조기에확인하기위해혈액검사를실시하는것이매우중요합니다.\",\"body\":\"HIV항체검사에서는양성또는음성이나온경우,HIV감염여부를확인할수있습니다.혈액검사결과가양성이면감염을의심할수있으며,필요한경우HIV감염여부를확인하기위해조직검사를실시할수있습니다.이러한검사는항체검사의결과를기반으로감염여부를확인하고,적절한치료와관리를통해합병증을예방하는데도움을줍니다.\",\"conclusion\":\"HIV감염을예방하기위해서는HIV감염자와의성적접촉을피하고,안전한성적접촉및예방백신접종을통해감염을예방하는것이중요합니다.HIV감염여부를조기에확인하기위해혈액검사와HIV감염확인을위한조직검사를받는것이필수적입니다.\"},\"is_matched\":0},{\"id\":79,\"session\":{\"id\":50,\"member\":47,\"created_at\":\"2025-03-17T10:34:22.946939\",\"updated_at\":\"2025-03-17T10:34:22.930933\",\"status\":1,\"count\":0,\"lastQuestion\":\"심심한질문\"},\"question\":{\"id\":40,\"content\":\"심심한질문\",\"created_at\":\"2025-03-20T20:46:39.434809\"},\"answer\":{\"id\":73027,\"file_name\":\"HC-A-02529874013027\",\"intro\":\"고막염은이가질환으로,고막에구멍이생기는염증이발생하는질환이다.이질환은일반적으로감기와같은상기도감염,바이러스성상기도감염,인플루엔자성상기도감염,그리고알레르기성비염,아데노이드비대증등의다양한원인으로발생할수있다.이중에서가장흔한감염경로는상기도감염인데,감기의바이러스에의해유발될수있다.또한,상기도감염과같이바이러스에의해이가감염되는경우에도고막염이발생할수있다.또한,인두나후두에감염이되어고막염이발생하기도한다.그외에도알레르기성비염이나아데노이드비대증의경우에도고막염이발생할수있다.\",\"body\":\"고막염은이가질환으로,고막에구멍이생기는염증이발생하는질환이다.이러한구멍은감기나상기도감염,바이러스성상기도감염,인플루엔자성상기도감염,그리고알레르기성비염등다양한원인에의해유발될수있다.가장흔한감염경로는상기도감염으로,이에의한바이러스감염이가장흔하다.또한,인두나후두에감염이되어고막염이발생할수도있다.그외에도알레르기성비염이나아데노이드비대증의경우에도고막염이나타날수있다.\",\"conclusion\":\"고막염은이가질환으로,고막에구멍이생기는염증이발생하는질환이다.주로감기와같은상기도감염,바이러스성상기도감염,인플루엔자성상기도감염,그리고알레르기성비염이나아데노이드비대증과같은바이러스감염이원인이다.이러한질환들의가장큰원인은바이러스감염이다.\"},\"is_matched\":0},{\"id\":80,\"session\":{\"id\":50,\"member\":47,\"created_at\":\"2025-03-17T10:34:22.946939\",\"updated_at\":\"2025-03-17T10:34:22.930933\",\"status\":1,\"count\":0,\"lastQuestion\":\"심심한질문\"},\"question\":{\"id\":41,\"content\":\"심심한질문\",\"created_at\":\"2025-03-20T20:46:56.816012\"},\"answer\":{\"id\":72637,\"file_name\":\"HC-A-04716576012637\",\"intro\":\"후천성면역결핍증(에이즈)환자의재활은개인별로상이한접근방식이필요합니다.\",\"body\":\"1.치료계획:후천성면역결핍증(에이즈)환자의치료계획은개인별상황과목표에따라다를수있습니다.이를위해전문의료진의도움을받아약물치료와보조치료를통해환자의면역체계를지원할수있습니다.2.상담및교육:후천성면역결핍증(에이즈)환자는전문적인상담과교육을받아야합니다.이를통해환자가질병에대해이해하고의사와협력하여치료방안을결정할수있도록도움을받을수있습니다.3.개인보호장구사용:후천성면역결핍증(에이즈)환자는개인보호장구를사용하여신체의건강을유지하고감염의위험을줄일수있습니다.이를통해합병증의발생을예방할수있습니다.\",\"conclusion\":\"후천성면역결핍증(에이즈)환자의재활은개개인의상황에맞게맞춤형으로진행되어야합니다.전문의료진의지원과개인보호장구의사용을통해후천성면역결핍증(에이즈)환자의면역체계를강화하고합병증의발생을예방할수있습니다.\"},\"is_matched\":0},{\"id\":81,\"session\":{\"id\":50,\"member\":47,\"created_at\":\"2025-03-17T10:34:22.946939\",\"updated_at\":\"2025-03-17T10:34:22.930933\",\"status\":1,\"count\":0,\"lastQuestion\":\"심심한질문\"},\"question\":{\"id\":41,\"content\":\"심심한질문\",\"created_at\":\"2025-03-20T20:46:56.816012\"},\"answer\":{\"id\":72614,\"file_name\":\"HC-A-04492216012614\",\"intro\":\"후천성면역결핍증(AIDS)은인체의면역시스템이약화되어면역결핍상태에도달하게되는질병입니다.이로인해다양한감염질환및암발병가능성이높아집니다.주요위험요인으로는HIV(인간면역결핍바이러스)감염과에이즈(후천성면역결핍증후군)감염이있습니다.HIV는인간면역체계를약화시켜면역력을저하시키는바이러스입니다.에이즈는주로면역체계를침범하는감염질환으로,주로만성질환형태로발전합니다.\",\"body\":\"후천성면역결핍증은HIV감염,HIV에의한면역력감소,AIDS관련질병및암의발병가능성이높은상황입니다.이러한이유로HIV감염자는후천성면역결핍증으로인해면역결핍이초래되고다양한감염질환과암이발생할수있습니다.HIV감염은주로혈액전파를통해전파되며,환자의혈액이나체액을통해인간면역결핍바이러스를전파하는것이일반적입니다.또한,HIV감염자의수혈로인해감염이일어날수있고,수직감염으로인해질병이전파될수도있습니다.또한,장기기증자와헌혈자는감염의위험이있습니다.HIV감염자는AIDS관련질병및암발병가능성도높아지므로주의가필요합니다.\",\"conclusion\":\"후천성면역결핍증은인체면역시스템이약화되어면역력이저하되는질병입니다.이러한이유로다양한감염질환과암발생위험이높아지게됩니다.HIV감염자는후천성면역결핍증으로인해면역력이저하되고다양한감염질환과암이발생할수있습니다.또한,수혈로인해감염이전파될수있으며,수직감염으로인해질병이전파될수도있습니다.AIDS관련질병및암발병가능성도높아지므로주의가필요합니다.\"},\"is_matched\":0}]}}"
				)
			) 
		),
		@ApiResponse( 
				responseCode = "400-조회 실패",
				description = "FAIL", 
				content = @Content(					
						examples = @ExampleObject(
							value = ""
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
		)
	})
	@GetMapping("/qna")
	public ResponseEntity<?> getQna(
			@Parameter(description = "회원의 토큰값") @RequestHeader(name = "Authorization") String token,
			@Parameter(description = "세션 아이디") @RequestParam(name = "sid") Long sid,
			@Parameter(description = "페이지") @RequestParam(required = false, defaultValue = "0") int p, 
			@Parameter(description = "출력 개수 제한") @RequestParam(required = false, defaultValue = "50") int ol) {
		//<찾은 회원이 존재하지 않는 경우>
		if(Commons.findMemberByToken(token, memberService) == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail(messageSource.getMessage("user.invalid_token", null, new Locale("ko"))));
		}
		
		//<찾은 회원이 존재하는 경우>
		ChatSessionDto session = chatSessionService.findById(sid);
		//세션 존재하지 않을때
		if(session == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultUtil.fail( "세션이 존재하지 않습니다." ));
		
		//세션id에 해당하는 qna
		List<ChatQnaDto> findedQna;
		
		//페이징 
		if(p > 0) findedQna = chatQnaService.findAllBySession(sid,p,ol);
		else findedQna = chatQnaService.findAllBySession(sid);		
		
		List<ChatQnaResponseDto> result = new Vector<>(); //반환값
		
		for(ChatQnaDto dto: findedQna) {
			
			//세션의 마지막 질문내용 조회 로직
			ChatQnaDto qna = chatQnaService.findLastQuestionOfSession(session.getId());
			ChatQuestionDto question = qna != null ? chatQuestionService.getQuestion(qna.getChatQuestionDto().getId()) : null;
			String qContent = question != null ? question.getContent() : null;
			
			//카테고리 정보
			List<TermsResponseDto> categories = new Vector<>();
			List <TermCategoryDto> categoryDtos = ancService.findAllByAnswerId(dto.getChatAnswerDto().getId()).stream().map(anc -> anc.getTermCategoryDto()).toList();
			categories = categoryDtos.stream().map(cate -> TermsResponseDto.toDto(cate)).toList();

			ChatQnaResponseDto responseSession = ChatQnaResponseDto.toDto(dto,session,qContent,categories);

			result.add(responseSession);	
			
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(result));
	}
	
}
