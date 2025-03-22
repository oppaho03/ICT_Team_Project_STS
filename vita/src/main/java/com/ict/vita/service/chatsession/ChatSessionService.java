package com.ict.vita.service.chatsession;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
	@Transactional(readOnly = true)
	public boolean existsById(Long sid) {
		return chatSessionRepository.existsById(sid);
	}
	
	/**
	 * [전체 세션 조회 - 페이징 적용]
	 * @param p : 페이지
	 * @param ol : 출력 개수 제한
	 * @return List<ChatSessionDto>
	 */
	@Transactional(readOnly = true)
	public List<ChatSessionDto> findAll(int p, int ol){
		//Pageable객체 생성
		Pageable pageable = PageRequest.of( p-1, ol, Sort.by(Sort.Order.asc("id")) );
		
		Page page = chatSessionRepository.findAll(pageable);
		
		List<ChatSessionEntity> list = page.getContent();
		return list.stream().map(entity -> ChatSessionDto.toDto(entity)).toList();
	}
	
	/**
	 * [전체 세션 조회 - 페이징 미적용]
	 * @return List<ChatSessionDto>
	 */
	@Transactional(readOnly = true)
	public List<ChatSessionDto> findAll(){
		List <ChatSessionEntity> list = chatSessionRepository.findAll( Sort.by(Sort.Order.asc("id")) );
		return list.stream().map(entity -> ChatSessionDto.toDto(entity)).toList();
	}
	
	/**
	 * [세션 id(PK)로 검색]
	 * @param id 세션id(PK)
	 * @return ChatSessionDto 찾은 세션 DTO 객체
	 */
	@Transactional(readOnly = true)
	public ChatSessionDto findById(Long id) {
		ChatSessionEntity sessionEntity = chatSessionRepository.findById(id).orElse(null);
		//세션을 찾은 경우
		if(sessionEntity != null) {
			return ChatSessionDto.toDto(sessionEntity);
		}
		//세션을 못 찾은 경우
		return null;
	}
	
	/**
	 * [회원id로 검색] - 페이징 적용
	 * @param mid 회원 id
	 * @param p : 페이지
	 * @param ol : 출력 개수 제한
	 * @return ChatSessionDto
	 */
	@Transactional(readOnly = true)
	public List<ChatSessionDto> findByMember(Long mid,int p, int ol) {
		Pageable pageable = PageRequest.of(p-1, ol, Sort.by(Sort.Order.asc("id")) );
		List <ChatSessionEntity> sessions = chatSessionRepository.findAllByMember(mid,pageable);
		return sessions.stream().map(entity -> ChatSessionDto.toDto(entity)).toList();
	}
	
	/**
	 * [회원id로 검색] - 페이징 미적용
	 * @param mid 회원 id
	 * @return ChatSessionDto
	 */
	@Transactional(readOnly = true)
	public List<ChatSessionDto> findByMember(Long mid) {
		List <ChatSessionEntity> sessions = chatSessionRepository.findAllByMember( mid, Sort.by(Sort.Order.asc("id")));
		return sessions.stream().map(entity -> ChatSessionDto.toDto(entity)).toList();
	}
	
	/**
	 * [세션id로 공개된 세션 조회]
	 * @param sid 세션id(PK)
	 * @return ChatSessionDto
	 */
	@Transactional(readOnly = true)
	public ChatSessionDto getPublicById(Long sid) {
		ChatSessionEntity session = chatSessionRepository.findPublicById(sid).orElse(null);
		return session != null ? ChatSessionDto.toDto(session) : null;
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
	
}
