package com.ict.vita.service.posts;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.vita.repository.posts.PostsEntity;
import com.ict.vita.repository.posts.PostsRepository;

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
	public PostsDto findById( Long id ) { return PostsDto.toDto( postsRepository.findById(id).orElse(null) ); }
	
	/**
	 * [모든 회원의 공개글 조회]
	 * @param cid 카테고리 id
	 * @return List<PostsDto>
	 */
	public List<PostsDto> getAllPublicPosts(Long cid){
		List<PostsEntity> entityList = postsRepository.getAllPublicPosts(cid);
		return entityList.stream().map(entity -> PostsDto.toDto(entity)).collect(Collectors.toList());
	}
	
	/**
	 * [특정 회원의 모든 게시글을 조회]
	 * @param memberId 조회하고 싶은 회원id
	 * @return List<PostsDto>
	 */
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
	public List<PostsDto> getPostsByNickname(Long cid, String nickname){
		List<PostsEntity> postsList = postsRepository.findByNickname(cid, nickname);
		return postsList.stream().map(post -> PostsDto.toDto(post)).collect(Collectors.toList());
	}
	
}
