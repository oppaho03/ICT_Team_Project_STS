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
import com.ict.vita.service.others.ObjectMetaRequestDto;
import com.ict.vita.service.others.ObjectMetaResponseDto;
import com.ict.vita.service.termcategory.TermCategoryDto;

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
	public List<ObjectMetaResponseDto> findAll( TermCategoryDto term ) {

		List<TermMetaEntity> termMetaEntities = new ArrayList<>();
		Optional<TermCategoryEntity> termCategory = termCategoryRepository.findById(term.getId());
		
		if ( termCategory.isPresent() )
			termMetaEntities.addAll( termMetaRepository.findAllByTermsEntity(termCategory.get().getTermsEntity()) );

		return termMetaEntities.stream().map( entity->ObjectMetaResponseDto.toDto(entity) ).toList();
	}

	/**
	 * ID 검색
	 * @param id 메타 ID
	 * @return 메타 또는 NULL 반환
	 */	
	public ObjectMetaResponseDto findById( Long id ) {
		TermMetaEntity termMetaEntity = termMetaRepository.findById(id).orElse(null);
		return termMetaEntity == null ? null : ObjectMetaResponseDto.toDto(termMetaEntity);
	}

	/**
	 * TermsDto AND 메타 키 검색
	 * @param termsDto TermsDto
	 * @param meta_key 메타 키
	 * @return 메타 또는 NULL 반환
	 */	
	public ObjectMetaResponseDto findByTermsDtoByMetaKey ( TermMetaDto metaDto ) {

		// < TermMetaDto 로 검사 > 
		if ( metaDto.getTermsDto() == null ) return null;

		TermMetaEntity result = termMetaRepository.findByTermsEntityAndMetaKey( metaDto.getTermsDto().toEntity(), metaDto.getMeta_key());

		return result == null ? null : ObjectMetaResponseDto.toDto(result);
	}


	/**
	 * 등록  
	 * @param reqDto ObjectMetaRequestDto
	 * @return 메타 값 반환
	 */	
	public ObjectMetaResponseDto save( ObjectMetaRequestDto reqDto ) {

		// < 카테고리 ID - 카테고리 엔티티 검색 >
		TermCategoryEntity termCategoryEntity = termCategoryRepository.findById(reqDto.getId()).orElse(null);

		if ( termCategoryEntity != null ) {

			/// < 메타 엔티티 생성 >
			TermMetaEntity metaEntity = new TermMetaEntity();
			metaEntity.setTermsEntity( termCategoryEntity.getTermsEntity() );
			metaEntity.setMetaKey( reqDto.getMeta_key() );
			metaEntity.setMetaValue( reqDto.getMeta_value() );

			/// < 현재 TermsEntity 와 MetaKey 값으로 중복 확인 >			
			ObjectMetaResponseDto resDto = findByTermsDtoByMetaKey( TermMetaDto.toDto(metaEntity) );
			if ( resDto != null ) 
				metaEntity.setMetaId(resDto.getMeta_id());  // 해당 메타 데이터 존재할 경우 덮어쓰기

			metaEntity = termMetaRepository.save( metaEntity );
			return metaEntity == null ? null : ObjectMetaResponseDto.toDto(metaEntity);
		}
		else return null;
	}
}
