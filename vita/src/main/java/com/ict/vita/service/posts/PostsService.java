package com.ict.vita.service.posts;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	 * @param id 
	 */
	public PostsDto findById( Long id ) { return PostsDto.toDto( postsRepository.findById(id).get() ); }
	
}
