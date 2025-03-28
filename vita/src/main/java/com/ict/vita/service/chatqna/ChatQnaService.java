package com.ict.vita.service.chatqna;

import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.vita.repository.chatqna.ChatQnaEntity;
import com.ict.vita.repository.chatqna.ChatQnaRepository;
import com.ict.vita.util.Commons;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatQnaService {
	//리포지토리 주입
	private final ChatQnaRepository chatQnaRepository;
	
	private final DataSource dataSource;
	private final EntityManager em;
	
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
	 * [세션 아이디로 QNA테이블 조회] - 세션으로 질문-답변 쌍 조회 => 페이징 미적용
	 * @param sid 세션아이디
	 * @return List<ChatQnaDto> 조회한 ChatQnaDto리스트
	 */
	@Transactional(readOnly = true)
	public List<ChatQnaDto> findAllBySession(Long sid){
		List<ChatQnaEntity> qnaEntities = chatQnaRepository.findAllByChatSessionEntity_id(sid);
		List<ChatQnaDto> qnaDtoes = qnaEntities.stream().map(entity -> ChatQnaDto.toDto(entity)).collect(Collectors.toList());
		return qnaDtoes;
	}
	
	/**
	 * [세션 아이디로 QNA테이블 조회] - 세션으로 질문-답변 쌍 조회 & 페이징 적용
	 * @param sid 세션아이디
	 * @param p 페이지
	 * @param ol 출력갯수
	 * @return List<ChatQnaDto> 조회한 ChatQnaDto리스트
	 */
	@Transactional(readOnly = true)
	public List<ChatQnaDto> findAllBySession(Long sid, int p, int ol) {
		// 현재 사용 중인 DB 종류 확인
	    String databaseProductName = Commons.getDatabaseProductName(dataSource);
	    
	    List<ChatQnaEntity> qnas = new Vector<>();
	    
	    String sql = null;
	    if (databaseProductName.equalsIgnoreCase("PostgreSQL")) {
	    	// PostgreSQL에서의 페이징 처리
	    	sql = "SELECT * FROM APP_CHAT_QNA WHERE s_id = :sid ORDER BY id DESC LIMIT :limit OFFSET :offset";
	    	Query query = em.createNativeQuery(sql, ChatQnaEntity.class);
	    	query.setParameter("sid", sid);
	    	query.setParameter("limit", ol);
	    	query.setParameter("offset", (p-1)*ol );
	    	
	    	qnas = query.getResultList();
	    }else if (databaseProductName.equalsIgnoreCase("Oracle")) {
	    	// Oracle에서의 페이징 처리 (ROWNUM)
	    	int startRow = (p - 1) * ol + 1;
	        int endRow = p * ol;
	        
	        System.out.println(String.valueOf(startRow) + " & " + String.valueOf(endRow));
	        
	        sql = "SELECT * "
	        		+ " FROM ( "
	        		+ " SELECT sub.*,ROWNUM as rnum FROM "
	        		+ " 	(SELECT * FROM APP_CHAT_QNA WHERE s_id = :sid ORDER BY id DESC) sub "
	        		+ " 	WHERE ROWNUM <= (SELECT COUNT(*) FROM APP_CHAT_QNA WHERE s_id = :sid) "
	        		+ " )where rnum BETWEEN :startRow AND :endRow ";
	        Query query = em.createNativeQuery(sql,ChatQnaEntity.class);
	        query.setParameter("sid", sid);
	        query.setParameter("startRow", startRow);
	        query.setParameter("endRow", endRow);
	        
	        qnas = query.getResultList();
	    }
	    
	    return qnas.stream().map(entity -> ChatQnaDto.toDto(entity)).toList();
	}
	
//	@Transactional(readOnly = true)
//	public List<ChatQnaDto> findAllBySession(Long sid){
//		List<ChatQnaEntity> qnaEntities = chatQnaRepository.findAllByChatSessionEntity_id(sid);
//		List<ChatQnaDto> qnaDtoes = qnaEntities.stream().map(entity -> ChatQnaDto.toDto(entity)).collect(Collectors.toList());
//		return qnaDtoes;
//	}
	
	/**
	 * [해당 세션의 마지막 질문 조회]
	 * @param sid 세션아이디
	 * @return ChatQnaDto
	 */
	@Transactional(readOnly = true)
	public ChatQnaDto findLastQuestionOfSession(Long sid) {
		ChatQnaEntity findedQna = chatQnaRepository.findLastQuestionBySession(sid);
		return findedQna != null ? ChatQnaDto.toDto(findedQna) : null;
	}
	
}
