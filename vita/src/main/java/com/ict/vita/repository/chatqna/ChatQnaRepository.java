package com.ict.vita.repository.chatqna;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatQnaRepository extends JpaRepository<ChatQnaEntity, Long> {
	@Query(value = "SELECT * FROM APP_CHAT_QNA WHERE s_id = :sessionId ORDER BY id ASC",nativeQuery = true)
	List<ChatQnaEntity> findAllByChatSessionEntity_Id(@Param("sessionId") Long sessionId); //세션 아이디로 조회
}
