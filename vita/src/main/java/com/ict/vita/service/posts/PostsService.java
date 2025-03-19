package com.ict.vita.service.posts;

import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.vita.repository.posts.PostsEntity;
import com.ict.vita.repository.posts.PostsRepository;
import com.ict.vita.util.Commons;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PostsService {
	//리포지토리 주입
	private final PostsRepository postsRepository;
	
	private final EntityManager em;

	/**
	 * 포스트 가져오기 
	 * @param id 글 id(PK)
	 */
	@Transactional(readOnly = true)
	public PostsDto findById( Long id ) { 
		return postsRepository.findById(id).orElse(null) != null ? PostsDto.toDto( postsRepository.findById(id).get()) : null;
	}
	
	/**
	 * [모든 회원의 공개글 조회]
	 * @param cid 카테고리 id
	 * @return List<PostsDto>
	 */
	@Transactional(readOnly = true)
	public List<PostsDto> getAllPublicPosts(List<Long> cids,int size){
		System.out.println("카테고리 ids(cids):"+cids.toString());
		
		//JPQL문 작성
		StringBuilder jpql = new StringBuilder("SELECT p.id "
				+ " FROM PostsEntity p"
				+ " JOIN PostCategoryRelationshipsEntity r ON p.id = r.postsEntity.id "
				+ " WHERE p.postStatus = 'PUBLISH' "
				);
		
		if(cids.size() != 0) {
			for(int i=0;i<cids.size();i++) {
				if(i==0) jpql.append(String.format(" AND r.termCategoryEntity.id = :id%s", i));
				else jpql.append(String.format(" OR r.termCategoryEntity.id = :id%s", i));
			}
		}

		jpql.append(" GROUP BY p.id ");
		jpql.append(" HAVING COUNT(DISTINCT r.termCategoryEntity.id) = :size ");

		System.out.println("jpql:"+jpql);

		//createQuery() 메서드 이용해 JPQL로 Query 생성
		TypedQuery<Long> query = em.createQuery(jpql.toString(), Long.class);
		
		//파라미터 바인딩
		for(int i=0;i<cids.size();i++) {
			System.out.println(String.format("id%s:%s", i,cids.get(i)));
			query.setParameter(String.format("id%s", i), cids.get(i));
		}
		query.setParameter("size", size);
		
		//JPQL 결과 리스트를 반환
		List<Long> results = query.getResultList(); //조회한 글 목록 id값들
		System.out.println("post ids:" + results.toString());
		
		//글id로 글 목록 조회
		List<PostsEntity> entities = new Vector<>();
		for(Long pid:results) {
			PostsDto dto = findById(pid);
			if(dto != null) { 
				entities.add(dto.toEntity());
				System.out.println("post:"+dto.toEntity().getId());
			}
		}
		
		return entities.stream().map(entity -> PostsDto.toDto(entity)).collect(Collectors.toList());

	}
	
	/**
	 * [특정 회원의 모든 게시글을 조회]
	 * @param memberId 조회하고 싶은 회원id
	 * @return List<PostsDto>
	 */
	@Transactional(readOnly = true)
	public List<PostsDto> getPostsByMember(Long cid,Long uid){
		List<PostsEntity> entityList = postsRepository.findByMember(cid,uid);
		return entityList.stream().map(entity -> PostsDto.toDto(entity)).collect(Collectors.toList());
	}
	
	/**
	 * [특정 회원이 쓴 게시글들 중 특정 상태의 글들 조회]
	 * @param cid 조회하려는 카테고리id
	 * @param uid 조회하려는 회원id
	 * @param status 글의 status
	 * @return List<PostsDto>
	 */
	@Transactional(readOnly = true)
	public List<PostsDto> getPostsByMemberAndStatus(Long cid,Long uid,String status){
		List<PostsEntity> entityList = postsRepository.findByMemberAndStatus(cid,uid,status);
		return entityList.stream().map(entity -> PostsDto.toDto(entity)).collect(Collectors.toList());
	}

	/**
	 * [글 제목으로 글 검색]
	 * @param cid 카테고리id
	 * @param title 글 제목
	 * @return List<PostsDto>
	 */
	@Transactional(readOnly = true)
	public List<PostsDto> getPostsByTitle(Long cid, String title) {
		List<PostsEntity> postsList = postsRepository.findByTitle(cid,title);
		return postsList.stream().map(post -> PostsDto.toDto(post)).collect(Collectors.toList());
	}
	
	/**
	 * [닉네임으로 글 검색]
	 * @param cid 카테고리id
	 * @param nickname 회원 닉네임
	 * @return List<PostsDto>
	 */
	@Transactional(readOnly = true)
	public List<PostsDto> getPostsByNickname(Long cid, String nickname){
		List<PostsEntity> postsList = postsRepository.findByNickname(cid, nickname);
		return postsList.stream().map(post -> PostsDto.toDto(post)).collect(Collectors.toList());
	}
	
	/**
	 * [글 저장]
	 * @param postRequestDto 저장하려는 글 정보
	 * @return PostsDto
	 */
	public PostsDto savePost(PostsDto postDto) {
		
		PostsEntity entity = postsRepository.save(postDto.toEntity());
		return entity != null ? PostsDto.toDto(entity) : null;
	}

	/**
	 * [글 삭제]
	 * @param id 삭제할 게시글 id
	 */
	public boolean deletePost(Long id) {
		PostsEntity findedPost = postsRepository.findById(id).orElse(null);
		if(findedPost != null) {
			findedPost.setPostStatus(Commons.POST_STATUS_DELETE);
			postsRepository.save(findedPost);
			return true;
		}
		
		return false;
	}
	
}
