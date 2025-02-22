package com.ict.vita.controller.chatqna;

import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.chatqna.ChatQnaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatQnaController {
	//서비스 주입
	private final ChatQnaService chatQnaService;
	
}
