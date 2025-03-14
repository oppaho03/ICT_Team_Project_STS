package com.ict.vita.service.posts;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.vita.repository.posts.PostsEntity;
import com.ict.vita.repository.posts.PostsRepository;
import com.ict.vita.util.Commons;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PostsService {
	//리포지토리 주입
	private final PostsRepository postsRepository;

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
	public List<PostsDto> getAllPublicPosts(Long cid){
		List<PostsEntity> entityList = postsRepository.getAllPublicPosts(cid);
		return entityList.stream().map(entity -> PostsDto.toDto(entity)).collect(Collectors.toList());
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
