package com.ict.vita.controller.chatsession;

import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.chatsession.ChatSessionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatSessionController {
	//서비스 주입
	private final ChatSessionService chatSessionService;
	
}
