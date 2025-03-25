package com.ict.vita.controller.chatanswer;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.anc.AncDto;
import com.ict.vita.service.anc.AncService;
import com.ict.vita.service.chatanswer.ChatAnswerDto;
import com.ict.vita.service.chatanswer.ChatAnswerResponseDto;
import com.ict.vita.service.chatanswer.ChatAnswerService;
import com.ict.vita.service.chatanswer.SearchRequestDto;
import com.ict.vita.service.chatqna.ChatQnaDto;
import com.ict.vita.service.terms.TermsDto;
import com.ict.vita.service.terms.TermsRequestDto;
import com.ict.vita.service.terms.TermsResponseDto;
import com.ict.vita.service.terms.TermsService;
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
public class ChatAnswerController {
	//서비스 주입
	private final ChatAnswerService chatanswerService;
	private final AncService ancService;
	private final TermsService termsService;

	/**
	 * [키워드로 검색]
	 * @param searchRequest 사용자가 입력한 키워드(JSON형식)
	 * @return ResponseEntity
	 * @throws UnsupportedEncodingException 
	 */
	@Operation( summary = "키워드 검색", description = "답변에서 키워드로 검색 API" )
	@ApiResponses({
		@ApiResponse( 
			responseCode = "200-검색 결과 조회",
			description = "SUCCESS", 
			content = @Content(	
				schema = @Schema(implementation = Map.class),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":{\"keywords\":[\"감염\",\"여부\",\"확인\",\"HIV\",\"항체\",\"검사\",\"항원\",\"결합\",\"실시\"],\"answers\":[{\"id\":60394,\"file_name\":\"HC-A-06137307000394\",\"intro\":\"에이즈는인체면역결핍바이러스인HIV에의해인체내에서일어나는감염성질환을의미합니다.HIV는주로성접촉을통해전파되며,일부경우에는혈액을통한전파가가능합니다.HIV는HIV-1,HIV-2,HIV-3세가지유형으로나뉘며,각각다른바이러스로분류됩니다.HIV는인체간에감염이이루어지며,이로인해심각한합병증이발생할수있습니다.\",\"body\":\"HIV는주로감염된사람의정액또는혈액을통해전파됩니다.이바이러스는감염된사람이기침,재채기,말하기등을통해다른사람에게도전파될수있습니다.이로인해감염된사람은심각한바이러스질환과감염된물질에노출될수있습니다.HIV는후천성면역결핍증을일으키는원인바이러스이며,이를제거하기위해HIV의유전자를포함한혈액이나체액을제거하는치료가시행됩니다.HIV는HIV-1과HIV-2유형의바이러스로나뉘며,HIV-1유형은HIV-1a바이러스를포함하고있으며,HIV-2유형은HIV-2b바이러스를포함하고있습니다.치료방법은약물치료와외과수술로나뉩니다.감염된조직의사용은혈액내바이러스에대한면역을강화시키기위해사용될수있으며,이러한치료법은HIV바이러스의지속적인복제를방지하기위해시행됩니다.\",\"conclusion\":\"HIV감염을예방하기위해서는HIV-1유형의바이러스에대한백신접종이중요합니다.HIV-1a감염은성적접촉을통해감염될수있으며,HIV-1b,HIV-2,HIV-3,HIV-4유형으로나뉘어집니다.HIV-1b유형에감염된사람은생식기를통해HIV바이러스를전파하여심각한합병증을유발할수있습니다.HIV-1b유형감염자는적절한치료를받지않을경우생명을위협받을수있습니다.또한,HIV-1a감염자는면역기능이심각하게손상되어감염으로부터의회복이어려울수있으며,심각한사망률이발생할수있습니다.따라서HIV-1b유형에감염되지않도록정기적인검사와조치가필요합니다.\",\"categories\":[{\"id\":1630,\"name\":\"답변\",\"slug\":\"answer\",\"group_number\":0,\"category\":\"chat\",\"description\":null,\"count\":0,\"parent\":0},{\"id\":825,\"name\":\"감염성질환\",\"slug\":\"%EA%B0%90%EC%97%BC%EC%84%B1%EC%A7%88%ED%99%98\",\"group_number\":0,\"category\":\"disease\",\"description\":null,\"count\":0,\"parent\":0},{\"id\":826,\"name\":\"HIV감염\",\"slug\":\"hiv_infection\",\"group_number\":0,\"category\":\"disease\",\"description\":null,\"count\":0,\"parent\":825},{\"id\":1547,\"name\":\"피부과\",\"slug\":\"%ED%94%BC%EB%B6%80%EA%B3%BC\",\"group_number\":0,\"category\":\"department\",\"description\":null,\"count\":0,\"parent\":0},{\"id\":1625,\"name\":\"정의\",\"slug\":\"%EC%A0%95%EC%9D%98\",\"group_number\":0,\"category\":\"intention\",\"description\":null,\"count\":0,\"parent\":0}]}]}}}"
				)
			) 
		),
		@ApiResponse( 
			responseCode = "200-검색 결과 없음",
			description = "SUCCESS", 
			content = @Content(					
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":null}}"
				)
			) 
		)
	})
	@PostMapping("/answers/search")
	public ResponseEntity<?> searchKeywords(@Parameter(description = "사용자가 입력한 키워드") @RequestBody SearchRequestDto searchRequest) throws UnsupportedEncodingException{
		System.out.println("===== 답변 검색 테스트 =====");
		//사용자가 입력한 키워드들
		String keywords = searchRequest.getKeywords().stream().map(keyword -> keyword.toString()).collect(Collectors.joining(" OR "));
		System.out.println("사용자 입력 키워드들:"+keywords);
		
		//키워드를 용어 테이블에 저장
		for(String kwd : searchRequest.getKeywords()) {
			termsService.save( 
					TermsRequestDto.builder()
					.name(kwd)
					.slug(URLEncoder.encode(kwd, "UTF-8")) //슬러그는 URL인코딩해서 저장
					.group_number(0L)
					.category("keywords")
					.count(0L)
					.parent(0L)
					.build()
			);
		}
		
		//키워드로 검색한 답변 결과들
		List<ChatAnswerDto> answerList = chatanswerService.findAnswerByKeywords(keywords);
		//<검색 결과가 없는 경우>
		if(answerList.size() == 0) {
			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(null));
		}
		
		//<검색 결과가 존재하는 경우>	
		List<ChatAnswerResponseDto> responses = new Vector<>(); //카테고리 목록이 포함된 답변 응답 객체 목록
		//키워드와 검색결과 반환 객체 생성
		Map<String, Object> result = new HashMap<>();
		
		for ( ChatAnswerDto answerDto : answerList ) {
			//ANC관계테이블에서 답변id로 관계객체 조회
			List<AncDto> ancDtos = ancService.findAllByAnswerId(answerDto.getId());
			//ANC관계객체 존재시
			if ( ancDtos != null && !ancDtos.isEmpty() ) {		
				List<TermsResponseDto> termResponseList = ancDtos.stream().map( anc -> TermsResponseDto.toDto(anc.getTermCategoryDto().toEntity()) ).toList();
				responses.add(ChatAnswerResponseDto.toDto(answerDto.toEntity(), termResponseList));
			}
		}
		
		result.put("keywords", searchRequest.getKeywords());  
		result.put("answers", responses);
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(ResultUtil.success(result));
	}
}
