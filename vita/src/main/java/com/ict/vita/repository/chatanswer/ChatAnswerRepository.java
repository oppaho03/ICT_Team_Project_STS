package com.ict.vita.repository.chatanswer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatAnswerRepository extends JpaRepository<ChatAnswerEntity, Long>{
	
	@Query(value = """
		    SELECT scores,id
		    FROM (
		        SELECT a.*, SCORE(1) + SCORE(2) + SCORE(3) AS scores
		        FROM APP_CHAT_ANSWER a
		        WHERE 
		            CONTAINS(a.intro, :keywords, 1) > 0
		            OR CONTAINS(a.body, :keywords, 2) > 0
		            OR CONTAINS(a.conclusion, :keywords, 3) > 0
		        ORDER BY scores DESC
		    ) 
		    WHERE ROWNUM <= 5
		    """, nativeQuery = true)
	List<ChatAnswerEntity> findAnswerByKeywords(@Param("keywords") String keywords); //키워드로 답변 검색

}
