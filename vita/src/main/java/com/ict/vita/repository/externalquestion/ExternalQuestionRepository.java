package com.ict.vita.repository.externalquestion;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalQuestionRepository extends JpaRepository<ExternalQuestionEntity, Long> {
	
	@Query(value = """
			SELECT age_group,category,question_count FROM (
			    SELECT 
			        CASE 
			            WHEN INSTR(age, '10') > 0 THEN '10대'
			            WHEN INSTR(age, '20') > 0 THEN '20대'
			            WHEN INSTR(age, '30') > 0 THEN '30대'
			            WHEN INSTR(age, '40') > 0 THEN '40대'
			            WHEN INSTR(age, '50') > 0 THEN '50대'
			            WHEN INSTR(age, '60') > 0 THEN '60대'
			            ELSE '기타'
			        END AS age_group,  
			        disease_category as category, 
			        COUNT(*) AS question_count
			    FROM APP_EXTERNAL_QUESTION
			    GROUP BY 
			        case 
			            WHEN INSTR(age, '10') > 0 THEN '10대'
			            WHEN INSTR(age, '20') > 0 THEN '20대'
			            WHEN INSTR(age, '30') > 0 THEN '30대'
			            WHEN INSTR(age, '40') > 0 THEN '40대'
			            WHEN INSTR(age, '50') > 0 THEN '50대'
			            WHEN INSTR(age, '60') > 0 THEN '60대'
			            ELSE '기타'
			        END,
			        disease_category
			    ORDER BY question_count DESC  
			) sub
			WHERE ROWNUM <= 5 AND age_group = :age
			""",
			nativeQuery = true)
	//나이대별 많이 질문한 외부질문 조회
	List<Object> findTopQuestionsByAgeGroup(@Param(value = "age") String age);
	
	@Query(value = """
			SELECT gender,category,question_count FROM (
			    SELECT 
			        gender,
			        disease_category as category, 
			        COUNT(*) AS question_count
			    FROM APP_EXTERNAL_QUESTION
			    GROUP BY 
			        gender,
			        disease_category
			    ORDER BY question_count DESC  
			) sub
			WHERE ROWNUM <= 5 AND gender = :gender
			""",
			nativeQuery = true)
	//성별별 많이 질문한 외부질문 조회
	List<Object> findTopQuestionsByGenderGroup(@Param(value = "gender") char gender);
	
	@Query(value = """
			SELECT age_group,gender,category,question_count FROM (
			    SELECT 
			        CASE 
			            WHEN INSTR(age, '10') > 0 THEN '10대'
			            WHEN INSTR(age, '20') > 0 THEN '20대'
			            WHEN INSTR(age, '30') > 0 THEN '30대'
			            WHEN INSTR(age, '40') > 0 THEN '40대'
			            WHEN INSTR(age, '50') > 0 THEN '50대'
			            WHEN INSTR(age, '60') > 0 THEN '60대'
			            ELSE '기타'
			        END AS age_group,  
			        gender,
			        disease_category as category, 
			        COUNT(*) AS question_count
			    FROM APP_EXTERNAL_QUESTION
			    GROUP BY 
			        case 
			            WHEN INSTR(age, '10') > 0 THEN '10대'
			            WHEN INSTR(age, '20') > 0 THEN '20대'
			            WHEN INSTR(age, '30') > 0 THEN '30대'
			            WHEN INSTR(age, '40') > 0 THEN '40대'
			            WHEN INSTR(age, '50') > 0 THEN '50대'
			            WHEN INSTR(age, '60') > 0 THEN '60대'
			            ELSE '기타'
			        END,
			        gender,
			        disease_category
			    ORDER BY question_count DESC  
			)sub
			WHERE ROWNUM <= 5 AND age_group = :age AND gender = :gender
			""",
			nativeQuery = true)
	//나이+성별 별 많이 질문한 외부질문 조회
	List<Object> findTopQuestionsByAgeAndGenderGroup(@Param(value = "age") String age,@Param(value = "gender") char gender);
	
	@Query(value = """
			SELECT occupation,category,question_count FROM (
			    SELECT 
			        occupation,
			        disease_category as category, 
			        COUNT(*) AS question_count
			    FROM APP_EXTERNAL_QUESTION
			    GROUP BY 
			        occupation,
			        disease_category
			    ORDER BY question_count DESC  
			) sub
			WHERE ROWNUM <= 5 AND occupation = :occupation
			""",
			nativeQuery = true)
	//직업별 많이 질문한 외부질문 조회
	List<Object> findTopQuestionsByOccupationGroup(@Param(value = "occupation") String occupation);
}
