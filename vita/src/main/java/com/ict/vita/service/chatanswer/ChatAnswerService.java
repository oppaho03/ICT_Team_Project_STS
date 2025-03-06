package com.ict.vita.service.chatanswer;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.vita.repository.chatanswer.ChatAnswerEntity;
import com.ict.vita.repository.chatanswer.ChatAnswerRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatAnswerService {
	//리포지토리 주입
	private final ChatAnswerRepository chatAnswerRepository;
	
	/**
	 * 답변 테이블 ID 검색
	 * @param keywords
	 * @return
	 */
	public ChatAnswerDto findById(Long id) { return ChatAnswerDto.toDto(chatAnswerRepository.findById(id).get()); }
	

	/**
	 * 답변 테이블에서 키워드로 검색하기
	 * @param keywords 사용자가 입력한 검색어들
	 * @return List<ChatAnswerDto> 키워드로 검색한 답변 결과값들
	 */
	public List<ChatAnswerDto> findAnswerByKeywords(String keywords) {
		List<ChatAnswerEntity> answerEntityList = chatAnswerRepository.findAnswerByKeywords(keywords); //결과 없을시 빈 리스트 반환
		return answerEntityList.stream().map(entity -> ChatAnswerDto.toDto(entity)).collect(Collectors.toList());
	}
}
