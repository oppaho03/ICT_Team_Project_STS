package com.ict.vita.service.chatsession;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.vita.repository.chatsession.ChatSessionEntity;
import com.ict.vita.repository.chatsession.ChatSessionRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatSessionService {
	//리포지토리 주입
	private final ChatSessionRepository chatSessionRepository;
	
	/**
	 * [세션 존재유무 판단]
	 * @param sid 세션id(PK)
	 * @return boolean(존재시 true, 미존재시 false 반환)
	 */
	public boolean existsById(Long sid) {
		return chatSessionRepository.existsById(sid);
	}
	
	/**
	 * [세션 생성]
	 * @param sessionDto 세션 정보를 담아 요청하는 DTO객체
	 * @return ChatSessionDto 생성된 세션 DTO 객체
	 */
	public ChatSessionDto createSession(ChatSessionDto sessionDto) {
		ChatSessionEntity sessionEntity = chatSessionRepository.save(sessionDto.toEntity());
		return ChatSessionDto.toDto(sessionEntity);
	}
	
	/**
	 * [세션 id(PK)로 검색]
	 * @param id 세션id(PK)
	 * @return ChatSessionDto 찾은 세션 DTO 객체
	 */
	public ChatSessionDto findById(Long id) {
		ChatSessionEntity sessionEntity = chatSessionRepository.findById(id).orElse(null);
		//세션을 찾은 경우
		if(sessionEntity != null) {
			return ChatSessionDto.toDto(sessionEntity);
		}
		//세션을 못 찾은 경우
		return null;
	}
	
}
