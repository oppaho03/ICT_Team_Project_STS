package com.ict.vita.controller.chatanswer;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.chatanswer.ChatAnswerDto;
import com.ict.vita.service.chatanswer.ChatAnswerService;
import com.ict.vita.service.chatanswer.SearchRequestDto;
import com.ict.vita.service.chatqna.ChatQnaDto;
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
public class ChatAnswerController {
	//서비스 주입
	private final ChatAnswerService chatanswerService;

	/**
	 * 키워드로 검색
	 * @param searchRequest 사용자가 입력한 키워드(JSON형식)
	 * @return ResponseEntity
	 */
	@Operation( summary = "키워드 검색", description = "답변에서 키워드로 검색 API" )
	@ApiResponses({
		@ApiResponse( 
			responseCode = "200-검색 결과 조회",
			description = "SUCCESS", 
			content = @Content(	
				schema = @Schema(implementation = ChatAnswerDto.class),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":[{\"id\":394,\"file_name\":\"HC-A-06137307000394\",\"intro\":\"에이즈는~~\",\"body\":\"HIV는주로~~\",\"conclusion\":\"HIV감염을예방하기위해서는~~\"},{\"id\":17,\"file_name\":\"HC-A-06128457000017\",\"intro\":\"HIV감염검진은~~\",\"body\":\"HIV감염검진은~~~\",\"conclusion\":\"HIV감염검진은~~~\"}]}}"
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
	public ResponseEntity<?> searchKeywords(@Parameter(description = "사용자가 입력한 키워드") @RequestBody SearchRequestDto searchRequest){
		System.out.println("===== 답변 검색 테스트 =====");
		//사용자가 입력한 키워드들
		String keywords = searchRequest.getKeywords().stream().map(keyword -> keyword.toString()).collect(Collectors.joining(" OR "));
		System.out.println("사용자 입력 키워드들:"+keywords);
		//키워드로 검색한 답변 결과들
		List<ChatAnswerDto> answerList = chatanswerService.findAnswerByKeywords(keywords);
		//<검색 결과가 없는 경우>
		if(answerList.size() == 0) {
			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(null));
		}
		
		//<검색 결과가 존재하는 경우>
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(answerList));
	}
}
