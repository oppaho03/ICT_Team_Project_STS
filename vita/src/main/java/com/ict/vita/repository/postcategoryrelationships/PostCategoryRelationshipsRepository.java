package com.ict.vita.repository.postcategoryrelationships;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ict.vita.repository.termcategory.TermCategoryEntity;


@Repository
public interface PostCategoryRelationshipsRepository extends JpaRepository<PostCategoryRelationshipsEntity, PostCategory> {

    @Query(value="""
		SELECT * FROM APP_POST_CATEGORY_RELATIONSHIPS m
		WHERE m.post_id = :post_id 
		""", 
		nativeQuery = true )
    // AND m.term_category_id = :term_category_id
    List<PostCategoryRelationshipsEntity> findByPostId( @Param("post_id") Long post_id );

    @Query(value="""
		SELECT * FROM APP_POST_CATEGORY_RELATIONSHIPS m
		WHERE m.term_category_id = :term_category_id 
		""", 
		nativeQuery = true )
    List<PostCategoryRelationshipsEntity> findByTermCategoryId( @Param("term_category_id") Long term_category_id );

	@Query(value="""
		SELECT * FROM APP_POST_CATEGORY_RELATIONSHIPS m
		WHERE m.term_category_id = :term_category_id  -- #pageable
		""", 
		countQuery = """
		SELECT COUNT(*) FROM APP_POST_CATEGORY_RELATIONSHIPS m 
		WHERE m.term_category_id = :term_category_id
		""",
		nativeQuery = true )
    Page<List<PostCategoryRelationshipsEntity>> findByTermCategoryId( @Param("term_category_id") Long term_category_id, @Param("pageable") Pageable pageable );

	Page<List<PostCategoryRelationshipsEntity>> findAllByTermCategoryEntity( @Param("term_category_id") TermCategoryEntity termCategoryEntity, Pageable pageable );


}