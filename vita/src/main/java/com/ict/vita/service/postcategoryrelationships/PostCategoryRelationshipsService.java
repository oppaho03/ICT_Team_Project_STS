package com.ict.vita.service.postcategoryrelationships;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.ict.vita.repository.postcategoryrelationships.PostCategoryRelationshipsEntity;
import com.ict.vita.repository.postcategoryrelationships.PostCategoryRelationshipsRepository;

import com.ict.vita.repository.posts.PostsRepository;
import com.ict.vita.repository.termcategory.TermCategoryRepository;
import com.ict.vita.service.posts.PostsDto;
import com.ict.vita.service.termcategory.TermCategoryDto;
import com.ict.vita.service.others.ObjectCategoryRelDto;

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
	@Transactional(readOnly = true)
	public List<PostCategoryRelationshipsDto> findAllByPostId( Long id ) {
		
		List<PostCategoryRelationshipsEntity> relEntities = postCategoryRelationshipsRepository.findByPostId( id );

		// if ( relEntities.isEmpty() ) return null;
		return relEntities.stream().map( entity -> PostCategoryRelationshipsDto.toDto(entity) ).toList();
	}

	/**
	 * 포스트 목록 가져오기
	 * @param id 카테고리 ID
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<PostCategoryRelationshipsDto> findAllByTermCategoryId( Long id ) {

		List<PostCategoryRelationshipsEntity> relEntities = postCategoryRelationshipsRepository.findByTermCategoryId( id );

		if ( relEntities.isEmpty() ) return null;
		return relEntities.stream().map( entity -> PostCategoryRelationshipsDto.toDto(entity) ).toList();
	}

	/**
	 * 포스트 목록 가져오기
	 * @param id 카테고리 ID
	 * @param p 페이지
	 * @param ol 출력 개수 제한
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<PostCategoryRelationshipsDto> findAllByTermCategoryId( Long id, int p, int ol ) {

		Pageable pageable = PageRequest.of( p - 1, ol, Sort.by(Sort.Order.asc("term_category_id")) );
		Page page = postCategoryRelationshipsRepository.findByTermCategoryId(id, pageable);

		if ( page.isEmpty() || page.getContent().isEmpty() ) return null;
		
		List<PostCategoryRelationshipsEntity> relEntities = page.getContent();
		return relEntities.stream().map( entity -> PostCategoryRelationshipsDto.toDto(entity) ).toList();
	}

	/**
	 * 포스트 + 카테고리 관계 등록
	 * @param reldto ObjectCategoryRelDto ( id, categories )
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

	/**
	 * 카테고리 목록 비교 및 추가 / 삭제
	 * @param id 포스트 ID
	 * @return
	 */
	public List<PostCategoryRelationshipsDto> update( PostsDto postsDto, List<Long> catIds ) {

		// < 포스트 ID 로 카테고리 목록 불러오기 >
		List<PostCategoryRelationshipsDto> relDtos = findAllByPostId(postsDto.getId());

		// 현재 포트스가 가지고 있는 전체 목록 
		List<Long> olds = relDtos.stream().map( dto -> dto.getTermCategoryDto().getId() ).toList();

		// 등록, 삭제 할 카테고리 ID 분류
		// - 포스트 + 카테고리 관계 등록 
		Set<Long> adds = new HashSet<>(catIds);
		adds.removeAll(olds);
		if ( adds.size() > 0 ) save( postsDto, adds.stream().toList() );
		
		
		// - 포스트 + 카테고리 관계 삭제  
		Set<Long> dels = new HashSet<>(olds);
		dels.removeAll(catIds);
		if ( dels.size() > 0 ) delete( postsDto, dels.stream().toList() );

		System.out.println(dels);

		return findAllByPostId(postsDto.getId());
	}

}
