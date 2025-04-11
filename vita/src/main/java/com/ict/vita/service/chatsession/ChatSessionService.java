package com.ict.vita.service.chatsession;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import javax.sql.DataSource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.vita.repository.chatsession.ChatSessionEntity;
import com.ict.vita.repository.chatsession.ChatSessionRepository;
import com.ict.vita.util.Commons;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatSessionService {
	//리포지토리 주입
	private final ChatSessionRepository chatSessionRepository;
	
	private final DataSource dataSource;
	private final EntityManager em;
	
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
		Pageable pageable = PageRequest.of( p-1, ol, Sort.by(Sort.Order.desc("updatedAt")) );
		
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
		List <ChatSessionEntity> list = chatSessionRepository.findAll( Sort.by(Sort.Order.desc("updatedAt")) );
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
	public List<ChatSessionDto> findAllByMember(Long mid,int p, int ol) {
		
		// 현재 사용 중인 DB 종류 확인
	    String databaseProductName = Commons.getDatabaseProductName(dataSource);
	    
	    List<ChatSessionEntity> sessions = new Vector<>();
	    
	    if (databaseProductName.equalsIgnoreCase("PostgreSQL")) {
	        // PostgreSQL에서의 페이징 처리
	        String sql = "SELECT * FROM APP_CHAT_SESSION s WHERE s.member_id = :mid ORDER BY s.updated_at desc LIMIT :limit OFFSET :offset";
	        Query query = em.createNativeQuery(sql, ChatSessionEntity.class);
	        query.setParameter("mid", mid);
	        query.setParameter("limit", ol);
	        query.setParameter("offset", (p - 1) * ol);
	        sessions = query.getResultList();
	    } else if (databaseProductName.equalsIgnoreCase("Oracle")) {
	        // Oracle에서의 페이징 처리 (ROWNUM)
	        int startRow = (p - 1) * ol + 1;
	        int endRow = p * ol;

	        String sql = "SELECT * "
	        		+ "FROM ( "
	        		+ "    SELECT sub.*, ROWNUM AS rn "
	        		+ "    FROM ( "
	        		+ "        SELECT s.* "
	        		+ "        FROM APP_CHAT_SESSION s "
	        		+ "        WHERE s.member_id = :mid "
	        		+ "        ORDER BY s.updated_at DESC "
	        		+ "    ) sub "
	        		+ " ) sub2 "
	        		+ " WHERE sub2.rn BETWEEN :startRow AND :endRow";

	        Query query = em.createNativeQuery(sql, ChatSessionEntity.class);
	        query.setParameter("mid", mid);
	        query.setParameter("startRow", startRow);
	        query.setParameter("endRow", endRow);
	        sessions = query.getResultList();
	    }
	    
	    return sessions.stream().map(entity -> ChatSessionDto.toDto(entity)).toList();
		
	}
	
	/**
	 * [회원id로 검색] - 페이징 미적용
	 * @param mid 회원 id
	 * @return ChatSessionDto
	 */
	@Transactional(readOnly = true)
	public List<ChatSessionDto> findAllByMember(Long mid) {
		
		List <ChatSessionEntity> sessions = null;
		String sql = "SELECT * FROM APP_CHAT_SESSION s WHERE s.member_id = :mid ORDER BY s.updated_at desc";
		Query query = em.createNativeQuery(sql, ChatSessionEntity.class);
		query.setParameter("mid", mid);
		sessions = query.getResultList();
		
		return sessions.stream().map(entity -> ChatSessionDto.toDto(entity)).toList();
	}
	
	/**
	 * [회원id로 공개 세션 검색] - 페이징 적용
	 * @param mid 회원 id
	 * @param p : 페이지
	 * @param ol : 출력 개수 제한
	 * @return List<ChatSessionDto>
	 */
	@Transactional(readOnly = true)
	public List<ChatSessionDto> findPublicsByMember(Long mid, int p, int ol) {
		
		// 현재 사용 중인 DB 종류 확인
	    String databaseProductName = Commons.getDatabaseProductName(dataSource);
	    
	    List<ChatSessionEntity> sessions = new Vector<>();
	    
	    if (databaseProductName.equalsIgnoreCase("PostgreSQL")) {
	        // PostgreSQL에서의 페이징 처리
	        String sql = "SELECT * FROM APP_CHAT_SESSION s WHERE s.member_id = :mid and s.status = 0 ORDER BY s.updated_at desc LIMIT :limit OFFSET :offset";
	        Query query = em.createNativeQuery(sql, ChatSessionEntity.class);
	        query.setParameter("mid", mid);
	        query.setParameter("limit", ol);
	        query.setParameter("offset", (p - 1) * ol);
	        sessions = query.getResultList();
	    } else if (databaseProductName.equalsIgnoreCase("Oracle")) {
	        // Oracle에서의 페이징 처리 (ROWNUM)
	        int startRow = (p - 1) * ol + 1;
	        int endRow = p * ol;
	        
	        String sql = "SELECT * "
	        		+ " FROM ( "
	        		+ "    select sub.*, rownum as rn "
	        		+ "    from "
	        		+ "    ( "
	        		+ "        SELECT s.* "
	        		+ "        FROM APP_CHAT_SESSION s "
	        		+ "        WHERE s.member_id = :mid and s.status = 0 "
	        		+ "        ORDER BY s.updated_at desc "
	        		+ "     ) sub "
	        		+ " ) sub2 "
	        		+ " WHERE sub2.rn BETWEEN :startRow AND :endRow";

	        Query query = em.createNativeQuery(sql, ChatSessionEntity.class);
	        query.setParameter("mid", mid);
	        query.setParameter("startRow", startRow);
	        query.setParameter("endRow", endRow);
	        sessions = query.getResultList();
	    }
		
		return sessions.stream().map(entity -> ChatSessionDto.toDto(entity)).toList();
	}
	
	/**
	 * [회원id로 공개 세션 검색] - 페이징 미적용
	 * @param mid 회원 id
	 * @return List<ChatSessionDto>
	 */
	@Transactional(readOnly = true)
	public List<ChatSessionDto> findPublicsByMember(Long mid) {
		
		List<ChatSessionEntity> list = null;
		
		String sql = "SELECT * FROM APP_CHAT_SESSION s WHERE s.status = 0 ORDER BY s.updated_at DESC ";
		Query query = em.createNativeQuery(sql, ChatSessionEntity.class);
		list = query.getResultList();
		
		return list.stream().map(entity -> ChatSessionDto.toDto(entity)).toList();
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
	 * [공개 세션 조회] & 페이징 적용
	 * @param p : 페이지
	 * @param ol : 출력 개수 제한
	 * @return List<ChatSessionDto>
	 */
	public List<ChatSessionDto> findPublics(int p, int ol) {
		// 현재 사용 중인 DB 종류 확인
	    String databaseProductName = Commons.getDatabaseProductName(dataSource);
	    
	    List<ChatSessionEntity> sessions = new Vector<>();
	    
	    String sql = "";
	    
	    if (databaseProductName.equalsIgnoreCase("PostgreSQL")) {
	    	sql = "SELECT * FROM APP_CHAT_SESSION s WHERE s.status = 0 ORDER BY s.updated_at desc LIMIT :limit OFFSET :offset";
	    	Query query = em.createNativeQuery(sql, ChatSessionEntity.class);
	    	query.setParameter("limit", ol);
	    	query.setParameter("offset", (p-1)*ol);
	    	sessions = query.getResultList();
	    }
	    else if (databaseProductName.equalsIgnoreCase("Oracle")) {
	    	// Oracle에서의 페이징 처리 (ROWNUM)
	        int startRow = (p - 1) * ol + 1;
	        int endRow = p * ol;
	        
	    	sql = "select sub2.* "
	    			+ " from "
	    			+ " ("
	    			+ "    select sub.*,ROWNUM as rn "
	    			+ "    from"
	    			+ "    ("
	    			+ "        select s.* "
	    			+ "        from app_chat_session s"
	    			+ "        where s.status = 0"
	    			+ "        order by s.updated_at desc"
	    			+ "    ) sub"
	    			+ " ) sub2"
	    			+ " where sub2.rn between :startRow and :endRow ";
	    	Query query = em.createNativeQuery(sql, ChatSessionEntity.class);
	    	query.setParameter("startRow", startRow);
	    	query.setParameter("endRow", endRow);
	    	sessions = query.getResultList();
	    }
	    
	    return sessions.stream().map(entity -> ChatSessionDto.toDto(entity)).toList();
		
	}
	
	/**
	 * [공개 세션 조회] & 페이징 미적용
	 * @return List<ChatSessionDto>
	 */
	public List<ChatSessionDto> findPublics() {
		//공개(0)인 세션 조회
		List<ChatSessionEntity> sessions = chatSessionRepository.findAllByStatus(0);
		return sessions.stream().map(entity -> ChatSessionDto.toDto(entity)).toList();
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
	 * [세션 수정]
	 * @param sessionDto 수정할 세션 DTO객체
	 * @return ChatSessionDto 수정한 세션 DTO객체
	 */
	public ChatSessionDto updateSession(ChatSessionDto sessionDto) {
		ChatSessionEntity updatedSession = chatSessionRepository.save(sessionDto.toEntity());
		return ChatSessionDto.toDto(updatedSession);
	}
	
}
