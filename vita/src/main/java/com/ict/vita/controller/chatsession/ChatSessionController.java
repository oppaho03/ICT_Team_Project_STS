package com.ict.vita.controller.chatsession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.chatsession.ChatSessionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatSessionController {
	//서비스 주입
	private final ChatSessionService chatSessionService;
	
	/**
	 * [대화 세션 생성] - 요청헤더로 받는 세션id가 없는 경우에만 세션 생성
	 * @param sid 세션id(PK) - 세션id 존재유무로 세션 생성여부 결정
	 * @return ResponseEntity
	 */
	@PostMapping("/sessions")
	public ResponseEntity<?> createChatSession(@RequestBody Long sid){
		//<이미 세션이 존재하는 경우>
		if(chatSessionService.existsById(sid)) {
			return ResponseEntity.status(HttpStatus.OK).body(null);
		}
		//<세션이 존재하지 않는 경우> - 세션 생성
		
		return null;
	}
}
