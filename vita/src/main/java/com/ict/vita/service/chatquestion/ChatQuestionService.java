package com.ict.vita.service.chatquestion;

import java.util.Optional;

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
	
	/**
	 * [아이디로 질문 조회]
	 * @param id 질문 아이디
	 * @return
	 */
	@Transactional(readOnly = true)
	public ChatQuestionDto getQuestion(Long id) {
		ChatQuestionEntity finded = chatQuestionRepository.findById(id).orElse(null);
		return finded != null ? ChatQuestionDto.toDto(finded) : null;
	}
}
