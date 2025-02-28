package com.ict.vita.controller.chatquestion;

import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.chatquestion.ChatQuestionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatQuestionController {
	//서비스 주입
	private final ChatQuestionService chatQuestionService;
	
}
