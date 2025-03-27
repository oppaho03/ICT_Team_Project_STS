package com.ict.vita.repository.chatqna;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatQnaRepository extends JpaRepository<ChatQnaEntity, Long> {
	
	@Query(value = "SELECT * FROM APP_CHAT_QNA WHERE s_id = :sessionId ORDER BY id ASC",nativeQuery = true)
	List<ChatQnaEntity> findAllByChatSessionEntity_id(@Param("sessionId") Long sessionId); //세션 아이디로 조회

	@Query(value = """
			select sub.id, sub.s_id, sub.q_id, sub.a_id, sub.is_matched
			from (
			    select r.id, r.s_id, r.q_id, r.a_id, r.is_matched
			    from APP_CHAT_QNA r
			    join APP_CHAT_SESSION s on s.id = r.s_id
			    where r.s_id = :sid
			    order by r.id desc
			) sub
			where rownum = 1
			""",
			nativeQuery = true)
	ChatQnaEntity findLastQuestionBySession(@Param("sid") Long sid); //해당 세션의 마지막 질문 조회
}
