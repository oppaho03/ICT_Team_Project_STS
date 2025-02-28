package com.ict.vita.controller.chatanswer;

import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.chatanswer.ChatAnswerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatAnswerController {
	//서비스 주입
	private final ChatAnswerService chatanswerService;

}
