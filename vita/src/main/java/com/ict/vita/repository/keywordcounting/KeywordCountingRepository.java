package com.ict.vita.repository.keywordcounting;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ict.vita.repository.termcategory.TermCategoryEntity;
import com.ict.vita.repository.terms.TermsEntity;

@Repository
public interface KeywordCountingRepository extends JpaRepository<KeywordCountingEntity, Long> {

	KeywordCountingEntity findByTermsEntityAndSearchedAt(TermsEntity entity, String serachedDate);

	@Query("SELECT k.termsEntity.name, SUM(k.count) FROM KeywordCountingEntity k " +
		       "WHERE k.termsEntity = :termsEntity " +
		       "AND k.searchedAt BETWEEN :startDate AND :endDate " +
		       "GROUP BY k.termsEntity.name " +
		       "ORDER BY SUM(k.count) DESC")
		List<Object[]> findKeywordCountBetweenDates(@Param("termsEntity") TermsEntity termsEntity,
		                                            @Param("startDate") String startDate,
		                                            @Param("endDate") String endDate);


}
