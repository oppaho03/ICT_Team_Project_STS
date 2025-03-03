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
import com.ict.vita.util.ResultUtil;

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
	@PostMapping("/posts/search")
	public ResponseEntity<?> searchKeywords(@RequestBody SearchRequestDto searchRequest){
		System.out.println("===== 답변 검색 테스트 =====");
		//사용자가 입력한 키워드들
		String keywords = searchRequest.getKeywords().stream().map(keyword -> keyword.toString()).collect(Collectors.joining(" OR "));
//		String keywords = searchRequest.getData().stream().map(keyword -> keyword.getKeyword()).collect(Collectors.joining(" OR "));
		System.out.println("사용자 입력 키워드들:"+keywords);
		//키워드로 검색한 답변 결과들
		List<ChatAnswerDto> answerList = chatanswerService.findAnswerByKeywords(keywords);
		//<검색 결과가 없는 경우>
		if(answerList.size() == 0) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ResultUtil.success(answerList));
		}
		
		//<검색 결과가 존재하는 경우>
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(answerList));
	}
}
