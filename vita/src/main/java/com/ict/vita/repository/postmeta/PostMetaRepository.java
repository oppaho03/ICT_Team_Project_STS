package com.ict.vita.repository.postmeta;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ict.vita.repository.posts.PostsEntity;

@Repository
public interface PostMetaRepository extends JpaRepository<PostMetaEntity, Long>{

    List<PostMetaEntity> findAllByPostsEntity(PostsEntity postsEntity);
    PostMetaEntity findByPostsEntityAndMetaKey(PostsEntity postsEntity, String metakey);

}
