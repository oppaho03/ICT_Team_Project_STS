package com.ict.vita.service.termmeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.vita.repository.termcategory.TermCategoryEntity;
import com.ict.vita.repository.termcategory.TermCategoryRepository;
import com.ict.vita.repository.termmeta.TermMetaEntity;
import com.ict.vita.repository.termmeta.TermMetaRepository;
import com.ict.vita.service.others.ObjectMetaDto;
import com.ict.vita.service.terms.TermDto;
import com.ict.vita.service.terms.TermsDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TermMetaService {
	//리포지토리 주입
	private final TermCategoryRepository termCategoryRepository; 
	private final TermMetaRepository termMetaRepository;
	
	/**
	 * 전체 검색 (카테고리 ID)
	 * @param term TermDto
	 * @return 메타 리스트 반환
	 */	
	public List<ObjectMetaDto> findAll( TermDto term ) {

		List<TermMetaEntity> termMetaEntities = new ArrayList<>();
		Optional<TermCategoryEntity> termCategory = termCategoryRepository.findById(term.getId());
		
		if ( termCategory.isPresent() )
			termMetaEntities.addAll( termMetaRepository.findAllByTermsEntity(termCategory.get().getTermsEntity()) );

		return termMetaEntities.stream().map( entity->ObjectMetaDto.toDto(entity) ).toList();
	}

	/**
	 * ID 검색
	 * @param id 메타 ID
	 * @return 메타 또는 NULL 반환
	 */	
	public ObjectMetaDto findById( Long id ) {
		TermMetaEntity termMetaEntity = termMetaRepository.findById(id).orElse(null);
		return termMetaEntity == null ? null : ObjectMetaDto.toDto(termMetaEntity);
	}
}
