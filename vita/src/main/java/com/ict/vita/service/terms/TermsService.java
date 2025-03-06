package com.ict.vita.service.terms;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ict.vita.repository.postcategoryrelationships.PostCategoryRelationshipsEntity;
import com.ict.vita.repository.postcategoryrelationships.PostCategoryRelationshipsRepository;
import com.ict.vita.repository.posts.PostsEntity;
import com.ict.vita.repository.posts.PostsRepository;
import com.ict.vita.repository.termcategory.TermCategoryEntity;
import com.ict.vita.repository.termcategory.TermCategoryRepository;
import com.ict.vita.repository.terms.TermsEntity;
import com.ict.vita.repository.terms.TermsRepository;
import com.ict.vita.service.postcategoryrelationships.PostCategoryRelationshipsDto;
import com.ict.vita.service.posts.PostsDto;
import com.ict.vita.service.termcategory.TermCategoryDto;
import com.ict.vita.service.termcategory.TermCategoryService;
import com.ict.vita.util.Commons;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
//[용어 Service]
public class TermsService {
	// 리포지토리 주입
	private final TermsRepository termsRepository;		
	private final TermCategoryRepository termCategoryRepository;

	/**
	 * TermDto 리스트 생성 
	 * @param entities List<TermsEntity>
	 * @return
	 */
	public List<TermDto> toTermDtoList ( List<? extends Object> entities ) {

		List<TermDto> result = new ArrayList<>();

		if ( ! entities.isEmpty() ) {

			List<TermCategoryEntity> dataset;

			if ( entities.get(0) instanceof TermsEntity ) {
				List<Optional<TermCategoryEntity>> termCategoryEntities = entities.stream().map( entity -> termCategoryRepository.findByTermsEntity( (TermsEntity) entity) ).filter(Optional::isPresent).toList();

				dataset = termCategoryEntities.stream().map( Optional::get ).toList();

			}
			else if ( entities.get(0) instanceof TermCategoryEntity ) {
				dataset = entities.stream().map( entity->(TermCategoryEntity)entity ).toList();
			}
			else return result;

			for ( TermCategoryEntity data : dataset ) {
				TermDto dto = toTermDto( data );
				if ( dto != null ) result.add( dto ); 
			} // end for

		} // end if

		return result;
	}


	/**
	 *  TermDto 생성
	 * @param entity TermCategoryEntity
	 * @return
	 */
	public TermDto toTermDto ( TermCategoryEntity entity ) {

		if ( entity == null ) return null;

		try {
			TermCategoryDto mdto = TermCategoryDto.toDto(entity);
			TermsDto sdto = mdto.getTermsDto();

			return ( TermDto
				.builder()
				.id(mdto.getId())					
				.description(mdto.getDescription())
				.category(mdto.getCategory())
				.count(mdto.getCount())
				.parent(mdto.getParent())
				// .term_id(sdto.getId())
				.name(sdto.getName())
				.slug(sdto.getSlug())
				.group_number(sdto.getGroup_number())		
				.build()
			);
		}
		catch ( Exception e ) {
			System.out.println( e.getMessage() );
			return null;
		}
		
	}

	/**
	 * 모두 검색 
	 * @return
	 */
	public List<TermDto> findAll() { return toTermDtoList( termsRepository.findAll( Sort.by(Sort.Order.asc("id")) ) ); } 

	/**
	 * 이름 검색 
	 * @param name 이름
	 * @return
	 */
	public List<TermDto> findAllByName (String name) { return toTermDtoList( termsRepository.findAllByName( name, Sort.by(Sort.Order.asc("id")) ) ); } 

	/**
	 * 카테고리(Taxonomy) 검색
	 * @param taxonomy 카테고리명
	 * @return
	 */
	public List<TermDto> findAllByTaxonomy (String taxonomy) { return toTermDtoList( termCategoryRepository.findByCategory( taxonomy, Sort.by(Sort.Order.asc("id")) ) ); } 

	/**
	Repository.findByCategory( name, Sort.by(Sort.Order.asc("id")) ) ); }  

	/**
	 * ID 검색 
	 * @param id 카테고리 Id
	 * @return
	 */
	public TermDto findById ( Long id ) {  
		TermCategoryEntity entity = termCategoryRepository.findById( id ).orElse(null);
		return entity != null ? toTermDto( entity ) : null;
		// // find TermsEntity 
		// TermsEntity entity = termsRepository.findById( id ).orElse(null);
		// if ( entity != null ) {
		// 	// TermsEntity -> TermCategoryEntity -> TermDto
		// 	return toTermDto( termCategoryRepository.findByTermsEntity(entity).orElse(null) );
		// }
		// else return null;
	}

	/**
	 * 검색 : 슬러그 
	 * @param slug 
	 * @param taxonomy 
	 * @return
	 */
	public TermDto findBySlugByCategory ( String slug, String taxonomy ) { 
		Optional<TermCategoryEntity> entity = termCategoryRepository.findBySlugByCategory(slug, taxonomy);		
		return entity.isPresent() ? toTermDto( entity.get() ) : null;
	}

	/**
	 * 검색 : 부모 카테고리 ID
	 * @param slug 
	 * @param taxonomy 
	 * @return
	 */
	public List<TermDto> findByParent ( Long id ) { 
		if ( id > 0 ) {
			TermCategoryEntity entity = termCategoryRepository.findById( id ).orElse(null);
			
			if ( entity == null) 
				return toTermDtoList(null);

			id = TermCategoryDto.toDto(entity).getTermsDto().getId(); 
		}
		return toTermDtoList( termCategoryRepository.findByParent( id ) ); 
	}

	/**
	 * 새 Term, TermCategory 등록
	 * @param dto 
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public TermDto save ( EmptyTermDto dto ) {

		// 슬러그 중복 검사
		if ( findBySlugByCategory( dto.getSlug(), dto.getCategory() ) != null ) return null;

		TermsDto termsDto = 
			TermsDto.builder()
			.name(dto.getName())
			.slug(dto.getSlug())
			.group_number( dto.getGroup_number() )
			.build();

		TermsEntity termsEntity = termsRepository.save(termsDto.toEntity());


		if ( termsEntity == null ) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // Rollback
			return null;
		}

		TermCategoryDto termCategoryDto = TermCategoryDto.builder()
			.category(dto.getCategory())
			.description(dto.getDescription())
			.count(dto.getCount())
			.parent(dto.getParent())
			.termsDto( TermsDto.toDto(termsEntity) )
			.build();


		TermCategoryEntity termCategoryEntity = termCategoryRepository.save(termCategoryDto.toEntity());

		if ( termCategoryEntity == null ) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // Rollback
			return null;
		}
		
		return toTermDto( termCategoryEntity );
	}
	
	/**
	 * Term, TermCategory 변경
	 * @param dto 
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public TermDto update ( EmptyTermDto dto ) {

		TermsEntity termsEntity = termsRepository.findById( dto.getTerm_id() ).orElse(null);

		TermCategoryEntity termCategoryEntity = termsEntity == null ? null : termCategoryRepository.findByTermsEntity( termsEntity ).orElse(null);

		// 유효하지 않은 ID
		if ( termCategoryEntity == null ) {
			System.out.println( String.format("Not found Terms data. id=%d", dto.getTerm_id()) );
			return null;
		}	
		
		TermCategoryDto termCategoryDto = TermCategoryDto.toDto(termCategoryEntity);
		TermsDto termsDto = termCategoryDto.getTermsDto();

		/* 값 비교 및 변경 
		 */
		if ( ! Commons.isNull(dto.getName()) ) termsDto.setName( dto.getName() );
		if ( ! Commons.isNull(dto.getSlug()) ) termsDto.setSlug( dto.getSlug() );
		if ( dto.getGroup_number() >= 0L ) 
			termsDto.setGroup_number( dto.getGroup_number() );
		
		if ( dto.getCount() >= 0L ) 
			termCategoryDto.setCount( dto.getCount() );
		if ( dto.getParent() >= 0L ) 
			termCategoryDto.setParent( dto.getParent() );
		
		if ( ! Commons.isNull(dto.getCategory()) ) 
			termCategoryDto.setCategory( dto.getCategory() );
		if ( ! Commons.isNull(dto.getDescription()) ) 
			termCategoryDto.setDescription( dto.getDescription() );

		// Term 등록
		termsEntity = termsRepository.save(termsDto.toEntity());
		if ( termsEntity == null ) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // Rollback
			return null;
		}
		else termCategoryDto.setTermsDto(TermsDto.toDto(termsEntity));
		
		// Term Category 등록
		termCategoryEntity = termCategoryRepository.save(termCategoryDto.toEntity());
		if ( termCategoryEntity == null ) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // Rollback
			return null;
		}

		return toTermDto(termCategoryEntity);
	}

}


