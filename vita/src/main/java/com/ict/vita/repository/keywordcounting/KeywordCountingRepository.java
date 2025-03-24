package com.ict.vita.repository.keywordcounting;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ict.vita.repository.termcategory.TermCategoryEntity;
import com.ict.vita.repository.terms.TermsEntity;

@Repository
public interface KeywordCountingRepository extends JpaRepository<KeywordCountingEntity, Long> {

	KeywordCountingEntity findByTermsEntityAndSearchedAt(TermsEntity entity, String serachedDate);

	//날짜 지정 키워드 카운트 수
	@Query("SELECT k.termsEntity.name, SUM(k.count) FROM KeywordCountingEntity k " +
		       "WHERE k.termsEntity = :termsEntity " +
		       "AND k.searchedAt BETWEEN :startDate AND :endDate " +
		       "GROUP BY k.termsEntity.name " +
		       "ORDER BY SUM(k.count) DESC")
	List<Object[]> findKeywordCountBetweenDates(@Param("termsEntity") TermsEntity termsEntity,
		                                        @Param("startDate") String startDate,
		                                        @Param("endDate") String endDate);
	
	

	
	//날짜지정 검색어 랭킹
	@Query("SELECT k.termsEntity.name, SUM(k.count) "+ 
				"FROM KeywordCountingEntity k " + 
				"WHERE k.searchedAt BETWEEN :startDate AND :endDate " +
				"GROUP BY k.termsEntity.name " +
				"ORDER BY SUM(k.count) DESC")
	List<Object[]> findKeywordRankingTop5BetweenDates(@Param("startDate") String startDate,
												  @Param("endDate") String endDate,
												  Pageable pageable);
	
	
	//실시간 탑 10
	@Query("SELECT k.termsEntity.name, SUM(k.count) " +
		       "FROM KeywordCountingEntity k " +
		       "WHERE k.searchedAt = :today " +
		       "GROUP BY k.termsEntity.name " +
		       "ORDER BY SUM(k.count) DESC")	
	List<Object[]> findRealtimeRankingTop10(
					@Param("today") String today,
					PageRequest pageRequest);

	
	//월별 카운트 수
	@Query("SELECT k.termsEntity.name, SUM(k.count) " +
		       "FROM KeywordCountingEntity k " +
		       "WHERE k.searchedAt BETWEEN :startOfMonth AND :endOfMonth " +
		       "GROUP BY k.termsEntity.name " +
		       "ORDER BY SUM(k.count) DESC")
	List<Object[]> getMonthRanking(@Param("startOfMonth") String startOfMonth,
								   @Param("endOfMonth") String endOfMonth,
								   Pageable pageable);
	


}
