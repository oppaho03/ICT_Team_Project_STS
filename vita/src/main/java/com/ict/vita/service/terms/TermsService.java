package com.ict.vita.service.terms;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

	@Transactional(readOnly = true)
	public List<TermCategoryEntity> toTermCategoryEntities ( List<? extends Object> entities ) {

		List<TermCategoryEntity> result = new ArrayList<>();

		if ( ! entities.isEmpty() ) {

			entities = entities.stream().filter( entity -> entity != null ).toList(); // null 객체 제거
			
			if ( entities.get(0) instanceof TermsEntity ) {

				List<Optional<TermCategoryEntity>> termCategoryEntity = entities.stream().map( entity->termCategoryRepository.findByTermsEntity( (TermsEntity)entity) ).filter(Optional::isPresent).toList();

				if ( ! termCategoryEntity.isEmpty() ) 
					result = termCategoryEntity.stream().filter(Optional::isPresent).map( Optional::get ).toList();

 			} // end instanceof TermsEntity

		}

		return result;
	} 

	/**
	 * 모두 검색 
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<TermCategoryDto> findAll() { 
		return toTermCategoryEntities( termsRepository.findAll( Sort.by(Sort.Order.asc("id")) ) )
			.stream()
			.map( entity->TermCategoryDto.toDto(entity) )
			.toList();
	} 

	/**
	 * 모두 검색 - 페이지 
	 * @param p : 페이지
	 * @param ol : 출력 개수 제한
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<TermCategoryDto> findAll(int p, int ol) { 

		Pageable pageable = PageRequest.of( p - 1, ol, Sort.by(Sort.Order.asc("id")) );
		Page page = termsRepository.findAll(pageable);
		
		/*
		Map<String, Object> result = new HashMap<>();
		result.put("page", p );
		result.put("total_pages", page.getTotalPages() );
		result.put("total_count", page.getTotalElements() );
		result.put("data", toTermDtoList( page.getContent() ));
		*/

		List<TermsEntity> entities = page.getContent().stream().filter( entity -> entity != null ).map(entity-> (TermsEntity) entity).toList();
		
		return toTermCategoryEntities( entities ).stream().map( entity->TermCategoryDto.toDto(entity) ).toList();
	} 

	/**
	 * 이름 검색 
	 * @param name 이름
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<TermCategoryDto> findAllByName (String name) { 
		return toTermCategoryEntities( termsRepository.findAllByName( name, Sort.by(Sort.Order.asc("id")) ) )
			.stream()
			.map( entity->TermCategoryDto.toDto(entity) )
			.toList();
	} 

	/**
	 * 카테고리(Taxonomy) 검색
	 * @param taxonomy 카테고리명
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<TermCategoryDto> findAllByTaxonomy (String taxonomy) { 
		return termCategoryRepository.findByCategory( taxonomy, Sort.by(Sort.Order.asc("id")) )
			.stream()
			.map( entity->TermCategoryDto.toDto(entity) )
			.toList();
	} 

	/**
	Repository.findByCategory( name, Sort.by(Sort.Order.asc("id")) ) ); }  

	/**
	 * [카테고리가 존재하는지 유무 확인]
	 * @param cid 카테고리id
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean isExistCategory(Long cid) {
		return termCategoryRepository.existsById(cid);
	}

	/**
	 * ID 검색 
	 * @param id 카테고리 Id
	 * @return
	 */
	@Transactional(readOnly = true)
	public TermCategoryDto findById ( Long id ) {  
		TermCategoryEntity entity = termCategoryRepository.findById( id ).orElse(null);
		return entity != null ? TermCategoryDto.toDto(entity) : null;
	}
	
	/**
	 * ID리스트로 검색 
	 * @param ids 카테고리 Ids
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<TermCategoryDto> findById ( List<Long> ids ) {  
		List<TermCategoryEntity> entities = termCategoryRepository.findById( ids );
		return entities.stream().map(entity -> TermCategoryDto.toDto(entity)).collect(Collectors.toList());
	}

	/**
	 * 검색 : 슬러그 
	 * @param slug 
	 * @param taxonomy 
	 * @return
	 */
	@Transactional(readOnly = true)
	public TermCategoryDto findBySlugByCategory ( String slug, String taxonomy ) { 
		Optional<TermCategoryEntity> entity = termCategoryRepository.findBySlugByCategory(slug, taxonomy);		
		return entity.isPresent() ? TermCategoryDto.toDto( entity.get() ) : null;
	}
	
	/**
	 * [카테고리에 해당하는 용어 검색]
	 * @param termName 용어 이름
	 * @param categoryName 카테고리 이름
	 * @return
	 */
	public TermCategoryDto findByNameAndCategory(String termName, String categoryName) {
		Optional<TermCategoryEntity> entity = termCategoryRepository.findByNameAndCategory(termName,categoryName);
		return entity.isPresent() ? TermCategoryDto.toDto( entity.get() ) : null;
	}

	/**
	 * 검색 : 부모 카테고리 ID
	 * @param slug 
	 * @param taxonomy 
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<TermCategoryDto> findByParent ( Long id ) { 
		if ( id > 0 ) {
			TermCategoryEntity termCategoryEntity = termCategoryRepository.findById( id ).orElse(null);
			
			if ( termCategoryEntity == null) 
				return new ArrayList<>();

			id = TermCategoryDto.toDto(termCategoryEntity).getTermsDto().getId(); 
		}

		return termCategoryRepository.findByParent( id ).stream().filter(entity->entity!=null).map( entity->TermCategoryDto.toDto(entity) ).toList(); 
	}

	/**
	 * 새 Term, TermCategory 등록
	 * @param dto TermsRequestDto
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public TermCategoryDto save ( TermsRequestDto dto ) {

		// 슬러그 중복 검사
		if ( findBySlugByCategory( dto.getSlug(), dto.getCategory() ) != null ) return null;
		
		//키워드에 속하는 용어 중복 검사
		if (dto.getCategory().equals("keywords") && findByNameAndCategory( dto.getName(), dto.getCategory()) != null ) return null;
		
		// .parent, .count 값 초기화 
		System.out.println(dto.getParent());
		if ( dto.getGroup_number() < 0L ) dto.setGroup_number( 0L );
		if ( dto.getParent() < 0L ) dto.setParent( 0L );
		if ( dto.getCount() < 0L ) dto.setCount( 0L );
					

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
		
		return TermCategoryDto.toDto(termCategoryEntity);		
	}
	
	/**
	 * Term, TermCategory 변경
	 * @param dto 
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public TermCategoryDto update ( TermsRequestDto dto ) {

		TermCategoryEntity termCategoryEntity = termCategoryRepository.findById( dto.getId() ).orElse(null);
		
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
		TermsEntity termsEntity = termsRepository.save(termsDto.toEntity());
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

		return TermCategoryDto.toDto(termCategoryEntity);

		
	}

}


