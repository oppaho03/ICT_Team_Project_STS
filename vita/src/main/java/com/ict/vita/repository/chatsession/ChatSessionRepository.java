package com.ict.vita.repository.chatsession;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSessionEntity, Long> {

	Page<ChatSessionEntity> findAll(Pageable pageable); //페이징 적용해 전체 세션 조회
	
	List<ChatSessionEntity> findAll(Sort sort); //정렬 적용해 전체 세션 조회(페이징 미적용)
	
	@Query(value = """
			select * from APP_CHAT_SESSION where id = :id and status = 0
			""",
			nativeQuery = true)
	Optional<ChatSessionEntity> findPublicById(@Param(value = "id") Long sid); //세션id로 공개 세션 조회
	
//	@Query(value = """
//			select * from APP_CHAT_SESSION s
//			where s.member_id = :mid \n-- #pageable\n
//			""",
//			countQuery = """
//			select count(*) from APP_CHAT_SESSION s
//			where s.member_id = :mid
//			""",
//			nativeQuery = true)
//	Page<ChatSessionEntity> findAllByMember(@Param(value = "mid") Long mid, Pageable pageable); //회원id로 세션 조회(페이징 적용)
	
//	Page<ChatSessionEntity> findAllByMemberEntity_id(@Param(value = "mid") Long mid, Pageable pageable); //회원id로 세션 조회(페이징 적용)
	
//	@Query(value = """
//			SELECT * FROM APP_CHAT_SESSION s WHERE s.member_id = :mid
//			""",
//			nativeQuery = true)
//	List<ChatSessionEntity> findAllByMember(@Param("mid") Long mid, Sort sort); //회원id로 세션 조회(페이징 미적용)
	
	
//	@Query(value = """
//			select * from APP_CHAT_SESSION s
//			where s.member_id = :mid and s.status = 0 \n-- #pageable\n
//			""",
//			countQuery = """
//			select count(*) from APP_CHAT_SESSION s
//			where s.member_id = :mid and  s.status = 0
//			""",
//			nativeQuery = true)
//	Page<ChatSessionEntity> findAllByMemberAndStatus(@Param("mid") Long mid, Pageable pageable); //회원id로 공개 세션 조회(페이징 적용)
	
	/*
	@Query(value = """
			SELECT * FROM APP_CHAT_SESSION s WHERE s.member_id = :mid and s.status = 0
			""",
			nativeQuery = true)
	List<ChatSessionEntity> findAllByMemberAndStatus(@Param("mid") Long mid, Sort sort); //회원id로 공개 세션 조회(페이징 미적용) */

	@Query(value = """
			SELECT * FROM APP_CHAT_SESSION s WHERE s.status = :status ORDER BY s.updated_at DESC
			""",
			nativeQuery = true)
	List<ChatSessionEntity> findAllByStatus(@Param("status") int status); //공개(0) 세션 조회용 (페이징 미적용)

}
