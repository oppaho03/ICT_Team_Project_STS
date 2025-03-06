package com.ict.vita.service.postcategoryrelationships;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.vita.repository.postcategoryrelationships.PostCategoryRelationshipsEntity;
import com.ict.vita.repository.postcategoryrelationships.PostCategoryRelationshipsRepository;
import com.ict.vita.repository.posts.PostsRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PostCategoryRelationshipsService {
	//리포지토리 주입
	private final PostCategoryRelationshipsRepository postCategoryRelationshipsRepository;

	private final PostsRepository postsRepository;
	// private final termCategoryRepository termCategoryRepository;

	/**
	 * 카테고리 목록 가져오기
	 * @param id 포스트 ID
	 * @return 
	 */
	public List<PostCategoryRelationshipsDto> findAllByPostId( Long id ) {
		
		List<PostCategoryRelationshipsEntity> relEntities = postCategoryRelationshipsRepository.findByPostId( id );

		if ( relEntities.isEmpty() ) return null;
		return relEntities.stream().map( entity -> PostCategoryRelationshipsDto.toDto(entity) ).toList();


		// if ( relEntities.isEmpty() ) return null;
		// else return relEntities.stream().map( entity -> TermCategoryDto.toDto(entity.getTermCategoryEntity()) ).toList();
	}

	/**
	 * 포스트 목록 가져오기
	 * @param id 카테고리 ID
	 * @return
	 */
	public List<PostCategoryRelationshipsDto> findAllByTermCategoryId( Long id ) {

		List<PostCategoryRelationshipsEntity> relEntities = postCategoryRelationshipsRepository.findByTermCategoryId( id );

		if ( relEntities.isEmpty() ) return null;
		return relEntities.stream().map( entity -> PostCategoryRelationshipsDto.toDto(entity) ).toList();
	}

}
