package com.ict.vita.repository.posts;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostsRepository extends JpaRepository<PostsEntity, Long>{
	
//	@Query(value = """
//				select p.* 
//				from APP_POSTS p
//				join APP_POST_CATEGORY_RELATIONSHIPS r on p.id = r.post_id
//				where r.term_category_id = :cid and p.post_status = 'PUBLISH'
//			"""
//			,nativeQuery = true)
//	List<PostsEntity> getAllPublicPosts(@Param(value = "cid") Long cid); //모든 회원의 공개글 조회
	
	@Query(value = """
				select p.*
				from APP_POSTS p
				join APP_POST_CATEGORY_RELATIONSHIPS r on p.id = r.post_id
				where r.term_category_id = :cid and p.post_author = :uid
				order by p.post_created_at DESC
			"""
			,nativeQuery = true)
	List<PostsEntity> findByMember(@Param("cid") Long cid,@Param("uid") Long uid); //해당 회원의 모든 글 조회용(카테고리id 존재)

	@Query(value = """
			select p.*
			from APP_POSTS p
			join APP_POST_CATEGORY_RELATIONSHIPS r on p.id = r.post_id
			where p.post_author = :uid
		"""
		,nativeQuery = true)
	List<PostsEntity> findByMember(@Param("uid") Long uid); //해당 회원의 모든 글 조회용(카테고리id 미존재)
	
	@Query(value = """
			select p.*
			from APP_POSTS p
			join APP_POST_CATEGORY_RELATIONSHIPS r on p.id = r.post_id
			where p.post_author = :uid
			order by p.id ASC
		"""
		,nativeQuery = true)
	List<PostsEntity> findByMemberForSar(@Param("uid") Long uid); //해당 회원의 모든 글 조회용(카테고리id 미존재)
	
	@Query(value = """
				select p.* 
				from APP_POSTS p
				join APP_POST_CATEGORY_RELATIONSHIPS r on p.id = r.post_id
				where r.term_category_id = :cid and p.post_status = :status and p.post_author = :uid
			"""
			,nativeQuery = true)
	List<PostsEntity> findByMemberAndStatus(@Param("cid") Long cid,@Param("uid") Long uid,@Param("status") String status); //해당 회원의 특정 조건에 해당하는 모든 글 조회용(페이징 미적용)
	
	@Query(value = """
			select p.* 
			from APP_POSTS p
			join APP_POST_CATEGORY_RELATIONSHIPS r on p.id = r.post_id
			where r.term_category_id = :cid and DBMS_LOB.INSTR(p.post_title, :title) > 0 and p.post_status = 'PUBLISH'
			order by p.post_created_at DESC
			"""
			,nativeQuery = true)
	List<PostsEntity> findByTitle(@Param("cid") Long cid,@Param("title") String title); //글 제목으로 글 검색
	
	@Query(value = """
			select p.*
			from APP_POSTS p
			join APP_POST_CATEGORY_RELATIONSHIPS r on p.id = r.post_id
			join APP_MEMBER m on m.id = p.post_author
			where r.term_category_id = :cid and m.nickname = :nickname and p.post_status = 'PUBLISH'
			order by p.post_created_at DESC
			"""
			,nativeQuery = true)
	List<PostsEntity> findByNickname(@Param("cid") Long cid,@Param("nickname") String nickname); //닉네임으로 글 검색
	
	@Query(value = """
			SELECT p.* 
			FROM APP_POSTS p 
			WHERE p.post_mime_type LIKE '%' || :type || '%'
			AND p.post_created_at BETWEEN :start AND :end 
			ORDER BY p.post_created_at DESC 
			""",
			nativeQuery = true)
	List<PostsEntity> findAllByPeriod(@Param("start") LocalDateTime start,@Param("end") LocalDateTime end,@Param("type") String type); //월별 검색

}
