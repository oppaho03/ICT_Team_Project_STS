package com.ict.vita.controller.chatanswer;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.chatanswer.ChatAnswerService;
import com.ict.vita.service.chatanswer.SearchRequestDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor()
@RequestMapping("/api")
public class ChatAnswerController {
	//서비스 주입
	private final ChatAnswerService chatanswerService;

	@PostMapping("/posts/search")
	public ResponseEntity<?> searchKeywords(@RequestBody SearchRequestDto searchRequest){
		System.out.println("===== 답변 검색 테스트중.... =====");
		System.out.println(searchRequest.getData().stream().map(keyword -> keyword.getKeyword()).collect(Collectors.joining(",")));
		return null;
	}
}
