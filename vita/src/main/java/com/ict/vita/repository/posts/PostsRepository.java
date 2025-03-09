package com.ict.vita.repository.posts;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostsRepository extends JpaRepository<PostsEntity, Long>{
	
	@Query(value = """
				select p.* 
				from app_posts p
				join app_member m on p.post_author = m.id
				where m.status = 1 AND p.post_status = 'PUBLISH'
			"""
			,nativeQuery = true)
	List<PostsEntity> getAllPublicPosts(); //모든 회원의 공개글 조회용
	
	List<PostsEntity> findByMemberEntity_Id(Long memberId); //해당 회원의 모든 글 조회용
	
	@Query(value = """
				select * from app_posts
				where post_author = :uid and post_status = :status;
			"""
			,nativeQuery = true)
	List<PostsEntity> findByMemberAndStatus(@Param("uid") Long memberId,@Param("status") String status); //해당 회원의 특정 조건에 해당하는 모든 글 조회용
}
