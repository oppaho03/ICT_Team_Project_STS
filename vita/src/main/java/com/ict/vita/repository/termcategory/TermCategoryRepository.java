package com.ict.vita.repository.termcategory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
//[카테고리(텍소노미) Repository]
public interface TermCategoryRepository extends JpaRepository<TermCategoryEntity, Long>{

	@Query(value="""
		SELECT tc FROM TermCategoryEntity tc
		LEFT JOIN FETCH tc.termsEntity t 
		WHERE t.slug = :slug
		""", 
		nativeQuery = false )
	List<TermCategoryEntity> findTermCategoryBySlug( @Param("slug") String slug, @Param("taxonomy") String taxonomy );
	
	
}
