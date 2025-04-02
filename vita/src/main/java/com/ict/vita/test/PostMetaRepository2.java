package com.ict.vita.test;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ict.vita.repository.postmeta.PostMetaEntity;
import com.ict.vita.repository.posts.PostsEntity;

@Repository
public interface PostMetaRepository2 extends JpaRepository<PostMetaEntity, Long> {
	List<PostMetaEntity> findAllByPostsEntity(PostsEntity postsEntity);
    PostMetaEntity findByPostsEntityAndMetaKey(PostsEntity postsEntity, String metakey);
}
