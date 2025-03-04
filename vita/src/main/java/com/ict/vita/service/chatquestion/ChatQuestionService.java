package com.ict.vita.service.chatquestion;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.vita.repository.chatquestion.ChatQuestionEntity;
import com.ict.vita.repository.chatquestion.ChatQuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatQuestionService {
	//리포지토리 주입
	private final ChatQuestionRepository chatQuestionRepository;
	
	/**
	 * [질문 저장]
	 * @param questionDto 요청한 질문 DTO
	 * @return 저장된 질문 DTO
	 */
	public ChatQuestionDto save(ChatQuestionDto questionDto) {
		ChatQuestionEntity savedEntity = chatQuestionRepository.save(questionDto.toEntity());
		return ChatQuestionDto.toDto(savedEntity);
	}
}
