package com.ict.vita.service.chatqna;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.vita.repository.chatqna.ChatQnaEntity;
import com.ict.vita.repository.chatqna.ChatQnaRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatQnaService {
	//리포지토리 주입
	private final ChatQnaRepository chatQnaRepository;
	
	/**
	 * [QNA 저장] - 질문과 답변을 세션으로 묶어줌
	 * @param qnaDto QNA요청 DTO
	 * @return ChatQnaDto 저장한 QNA DTO
	 */
	public ChatQnaDto save(ChatQnaDto qnaDto) {
		ChatQnaEntity qnaEntity = chatQnaRepository.save(qnaDto.toEntity());
		return ChatQnaDto.toDto(qnaEntity);
	}
	
	/**
	 * [세션 아이디로 QNA테이블 조회] - 세션으로 질문-답변 쌍 조회
	 * @param sid 세션아이디
	 * @return List<ChatQnaDto> 조회한 ChatQnaDto리스트
	 */
	@Transactional(readOnly = true)
	public List<ChatQnaDto> findAllBySession(Long sid){
		List<ChatQnaEntity> qnaEntities = chatQnaRepository.findAllByChatSessionEntity_id(sid);
		List<ChatQnaDto> qnaDtoes = qnaEntities.stream().map(entity -> ChatQnaDto.toDto(entity)).collect(Collectors.toList());
		return qnaDtoes;
	}
}
