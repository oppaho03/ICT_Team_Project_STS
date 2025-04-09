package com.ict.vita.repository.chatanswer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatAnswerRepository extends JpaRepository<ChatAnswerEntity, Long>{
	
	@Query(value = """
            SELECT aa.id, aa.file_name, aa.intro, aa.body, aa.conclusion
            FROM (
                SELECT a.id, a.file_name, a.intro, a.body, a.conclusion, SCORE(1) + SCORE(2) + SCORE(3)  AS scores
                FROM APP_CHAT_ANSWER a
                WHERE
                    CONTAINS(a.intro, :keywords, 1) > 25
                    OR CONTAINS(a.body, :keywords, 2) > 25
                    OR CONTAINS(a.conclusion, :keywords, 3) > 25
                ORDER BY scores DESC
            ) aa
            WHERE ROWNUM <= 1
            """, nativeQuery = true)
	List<ChatAnswerEntity> findAnswerByKeywords(@Param("keywords") String keywords); //키워드로 답변 검색

}
