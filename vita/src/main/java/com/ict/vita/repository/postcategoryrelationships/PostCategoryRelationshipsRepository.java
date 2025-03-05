package com.ict.vita.repository.postcategoryrelationships;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface PostCategoryRelationshipsRepository extends JpaRepository<PostCategoryRelationshipsEntity, PostCategory> {

    // List<PostCategoryRelationshipsEntity> findAllByPostEntity(PostsEntity postsEntity);

    @Query(value="""
		SELECT * FROM APP_POST_CATEGORY_RELATIONSHIPS m
		WHERE m.post_id = :post_id 
		""", 
		nativeQuery = true )
    // AND m.term_category_id = :term_category_id
    List<PostCategoryRelationshipsEntity> findByPostId( @Param("post_id") Long post_id );


}
