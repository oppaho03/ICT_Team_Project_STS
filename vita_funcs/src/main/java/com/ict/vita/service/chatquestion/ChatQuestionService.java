package com.ict.vita.service.chatquestion;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.vita.repository.chatquestion.ChatQuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatQuestionService {
	//리포지토리 주입
	private final ChatQuestionRepository chatQuestionRepository;
	
	
}
