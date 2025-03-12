package com.ict.vita.service.anc;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.ict.vita.repository.anc.AncEntity;
import com.ict.vita.repository.anc.AncRepository;
import com.ict.vita.repository.termcategory.TermCategoryRepository;
import com.ict.vita.service.chatanswer.ChatAnswerDto;
import com.ict.vita.service.postcategoryrelationships.PostCategoryRelationshipsDto;
import com.ict.vita.service.posts.PostsDto;
import com.ict.vita.service.termcategory.TermCategoryDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AncService {
	//리포지토리 주입
	private final AncRepository ancRepository;
	private final TermCategoryRepository termCategoryRepository;
	
	/**
	 * 카테고리 목록 가져오기
	 * @param id 답변 ID
	 * @return
	 */	
	public List<AncDto> findAllByAnswerId( Long id ) {
		List<AncEntity> relEntities = ancRepository.findByAnswerId( id );
		if ( relEntities.isEmpty() ) return null;
		return relEntities.stream().map( entity -> AncDto.toDto(entity) ).toList();
	}

	/**
	 * 답변 목록 가져오기
	 * @param id 카테고리 ID
	 * @return
	 */
	public List<AncDto> findAllByTermCategoryId( Long id ) {
		List<AncEntity> relEntities = ancRepository.findAllByTermCategoryId( id );
		if ( relEntities.isEmpty() ) return null;
		return relEntities.stream().map( entity -> AncDto.toDto(entity) ).toList();
	}

	/**
	 * 답변 + 카테고리 관계 등록
	 * @param reldto EmptyTermRelDto ( id, categories )
	 * @return
	 */	
	@Transactional(rollbackFor = Exception.class)
	public boolean save( ChatAnswerDto answerDto, List<Long> categories ) {
		if ( answerDto == null ) {
			return false;
		}

		try {
			for(  Long c_id : categories ) {
				TermCategoryDto _dto = TermCategoryDto.toDto(termCategoryRepository.findById( c_id ).orElse(null));
				
				if ( _dto == null || _dto.getId() == 0 || _dto.getId() == null ) continue;
	
				AncDto relDto = 
					AncDto
					.builder()
					.chatAnswerDto(answerDto)
					.termCategoryDto(_dto)
					.build();
	
				ancRepository.save( relDto.toEntity());
			}
	
		}
		catch ( Exception e ) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // Rollback
			return false;
		}

		return true;
	}

	/**
	 * 답변 + 카테고리 관계 삭제
	 */
	@Transactional(rollbackFor = Exception.class)
	public boolean delete( ChatAnswerDto answerDto, List<Long> categories ) {

		if ( answerDto == null ) return true;

		try {
			for(  Long c_id : categories ) {
				TermCategoryDto _dto = TermCategoryDto.toDto(termCategoryRepository.findById( c_id ).orElse(null));
				
				if ( _dto == null || _dto.getId() == 0 || _dto.getId() == null ) continue;
	
				AncDto relDto = AncDto
					.builder()
					.chatAnswerDto(answerDto)
					.termCategoryDto(_dto)
					.build();
	
				ancRepository.delete( relDto.toEntity());
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
	public List<AncDto> update( ChatAnswerDto ancDto, List<Long> catIds ) {

		// < 포스트 ID 로 카테고리 목록 불러오기 >
		List<AncDto> relDtos = findAllByAnswerId(ancDto.getId());

		// 현재 포트스가 가지고 있는 전체 목록 
		List<Long> olds = relDtos.stream().map( dto -> dto.getTermCategoryDto().getId() ).toList();

		// 등록, 삭제 할 카테고리 ID 분류
		// - 포스트 + 카테고리 관계 등록 
		Set<Long> adds = new HashSet<>(catIds);
		adds.removeAll(olds);
		if ( adds.size() > 0 ) save( ancDto, adds.stream().toList() );
		
		
		// - 포스트 + 카테고리 관계 삭제  
		Set<Long> dels = new HashSet<>(olds);
		dels.removeAll(catIds);
		if ( dels.size() > 0 ) delete( ancDto, dels.stream().toList() );

		System.out.println(dels);

		return findAllByAnswerId(ancDto.getId());
	}
	
}
