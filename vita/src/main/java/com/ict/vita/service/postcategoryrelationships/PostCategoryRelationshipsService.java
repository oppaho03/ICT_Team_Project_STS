package com.ict.vita.service.postcategoryrelationships;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.ict.vita.repository.postcategoryrelationships.PostCategoryRelationshipsEntity;
import com.ict.vita.repository.postcategoryrelationships.PostCategoryRelationshipsRepository;
import com.ict.vita.repository.posts.PostsEntity;
import com.ict.vita.repository.posts.PostsRepository;
import com.ict.vita.repository.termcategory.TermCategoryRepository;
import com.ict.vita.service.posts.PostsDto;
import com.ict.vita.service.termcategory.TermCategoryDto;
import com.ict.vita.service.terms.EmptyTermRelDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PostCategoryRelationshipsService {
	//리포지토리 주입
	private final PostCategoryRelationshipsRepository postCategoryRelationshipsRepository;

	private final PostsRepository postsRepository;
	private final TermCategoryRepository termCategoryRepository;

	/**
	 * 카테고리 목록 가져오기
	 * @param id 포스트 ID
	 * @return 
	 */
	public List<PostCategoryRelationshipsDto> findAllByPostId( Long id ) {
		
		List<PostCategoryRelationshipsEntity> relEntities = postCategoryRelationshipsRepository.findByPostId( id );

		if ( relEntities.isEmpty() ) return null;
		return relEntities.stream().map( entity -> PostCategoryRelationshipsDto.toDto(entity) ).toList();
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

	
	/**
	 * 포스트 + 카테고리 관계 등록
	 * @param reldto EmptyTermRelDto ( id, categories )
	 * @return
	 */	
	@Transactional(rollbackFor = Exception.class)
	public boolean save( PostsDto postsDto, List<Long> categories ) {

		if ( postsDto == null ) {
			return false;
		}

		try {
			for(  Long c_id : categories ) {
				TermCategoryDto _dto = TermCategoryDto.toDto(termCategoryRepository.findById( c_id ).orElse(null));
				
				if ( _dto == null || _dto.getId() == 0 || _dto.getId() == null ) continue;
	
				PostCategoryRelationshipsDto relDto = 
					PostCategoryRelationshipsDto
					.builder()
					.postsDto(postsDto)
					.termCategoryDto(_dto)
					.build();
	
				postCategoryRelationshipsRepository.save( relDto.toEntity());
			}
	
		}
		catch ( Exception e ) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // Rollback
			return false;
		}

		return true;
	}

	/**
	 * 포스트 + 카테고리 관계 삭제
	 */
	@Transactional(rollbackFor = Exception.class)
	public boolean delete( PostsDto postsDto, List<Long> categories ) {

		if ( postsDto == null ) return true;

		try {
			for(  Long c_id : categories ) {
				TermCategoryDto _dto = TermCategoryDto.toDto(termCategoryRepository.findById( c_id ).orElse(null));
				
				if ( _dto == null || _dto.getId() == 0 || _dto.getId() == null ) continue;
	
				PostCategoryRelationshipsDto relDto = 
					PostCategoryRelationshipsDto
					.builder()
					.postsDto(postsDto)
					.termCategoryDto(_dto)
					.build();
	
				postCategoryRelationshipsRepository.delete( relDto.toEntity());
			}
	
		}
		catch ( Exception e ) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // Rollback
			return false;
		}

		return true;

	}



}
