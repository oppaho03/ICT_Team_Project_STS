package com.ict.vita.repository.termcategory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ict.vita.repository.terms.TermsEntity;


@Repository
//[카테고리(텍소노미) Repository]
public interface TermCategoryRepository extends JpaRepository<TermCategoryEntity, Long>{

	List<TermCategoryEntity> findByCategory(String category, Sort sort);
	Optional<TermCategoryEntity> findByTermsEntity( TermsEntity termsEntity );

	@Query(value="""
		SELECT tc FROM TermCategoryEntity tc
		LEFT JOIN FETCH tc.termsEntity t 
		WHERE t.slug = :slug AND tc.category = :taxonomy
		""", 
		nativeQuery = false )
	Optional<TermCategoryEntity> findBySlugByCategory( @Param("slug") String slug, @Param("taxonomy") String taxonomy );
	
	@Query("SELECT m FROM TermCategoryEntity m WHERE m.id IN :ids")
    List<TermCategoryEntity> findById(@Param("ids") List<Long> ids);

    List<TermCategoryEntity> findByParent(Long id);
    
    @Query(value="""
		SELECT tc FROM TermCategoryEntity tc
		LEFT JOIN FETCH tc.termsEntity t 
		WHERE t.name = :termName AND tc.category = :categoryName
		""", 
		nativeQuery = false )
	Optional<TermCategoryEntity> findByNameAndCategory(@Param("termName") String termName,@Param("categoryName") String categoryName); //카테고리명에 속하는 용어명 조회
    
}
