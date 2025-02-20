package com.ict.vita.service.chatanswer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.vita.repository.chatanswer.ChatAnswerRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatAnswerService {
	//리포지토리 주입
	private final ChatAnswerRepository chatAnswerRepository;
	
	
}
