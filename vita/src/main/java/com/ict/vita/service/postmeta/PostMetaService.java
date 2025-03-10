package com.ict.vita.service.postmeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.vita.repository.postmeta.PostMetaEntity;
import com.ict.vita.repository.postmeta.PostMetaRepository;
import com.ict.vita.repository.posts.PostsEntity;
import com.ict.vita.repository.posts.PostsRepository;
import com.ict.vita.service.others.ObjectMetaRequestDto;
import com.ict.vita.service.posts.PostsDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PostMetaService {
	//리포지토리 주입
	private final PostMetaRepository postMetaRepository;
	private final PostsRepository postsRepository;
	
	/**
	 * 전체 검색 (카테고리 ID)
	 * @param post TermDto
	 * @return 메타 리스트 반환
	 */	
	public List<PostMetaDto> findAll( PostsDto post ) {

		List<PostMetaEntity> postMetaEntities = new ArrayList<>();
		Optional<PostsEntity> posts = postsRepository.findById(post.getId());
		
		if ( posts.isPresent() )
			postMetaEntities.addAll( postMetaRepository.findAllByPostsEntity(posts.get()) );

		return postMetaEntities.stream().map( entity->PostMetaDto.toDto(entity) ).toList();
	}

	/**
	 * ID 검색
	 * @param id 메타 ID
	 * @return 메타 또는 NULL 반환
	 */	
	public PostMetaDto findById( Long id ) {
		PostMetaEntity postMetaEntity = postMetaRepository.findById(id).orElse(null);
		return postMetaEntity == null ? null : PostMetaDto.toDto(postMetaEntity);
	}


	/**
	 * PostMetaDto AND 메타 키 검색
	 * @param metaDto PostMetaDto
	 * @param meta_key 메타 키
	 * @return 메타 또는 NULL 반환
	 */	
	public PostMetaDto findByPostDtoByMetaKey ( PostMetaDto metaDto ) {

		// < PostMetaDto 로 검사 > 
		if ( metaDto.getPostsDto() == null ) return null;
		PostMetaEntity result = postMetaRepository.findByPostsEntityAndMetaKey( metaDto.getPostsDto().toEntity(), metaDto.getMeta_key());

		return result == null ? null : PostMetaDto.toDto(result);
	}



	/**
	 * 등록  
	 * @param reqDto ObjectMetaRequestDto
	 * @return 메타 값 반환
	 */	
	public PostMetaDto save( ObjectMetaRequestDto reqDto ) {
		// < 포스트 ID - 카테고리 엔티티 검색 >
		PostsEntity postsEntity = postsRepository.findById(reqDto.getId()).orElse(null);

		if ( postsEntity != null ) {
			/// < 메타 엔티티 생성 >
			PostMetaEntity metaEntity = new PostMetaEntity();
			metaEntity.setPostsEntity( postsEntity );
			metaEntity.setMetaKey( reqDto.getMeta_key() );
			metaEntity.setMetaValue( reqDto.getMeta_value() );

			/// < 현재 PostsEntity 와 MetaKey 값으로 중복 확인 >
			PostMetaDto resDto = findByPostDtoByMetaKey( PostMetaDto.toDto(metaEntity) );
			if ( resDto != null ) metaEntity.setMetaId(resDto.getMeta_id()); // 해당 메타 데이터 존재할 경우 덮어쓰기


			metaEntity = postMetaRepository.save( metaEntity );
			return metaEntity == null ? null : PostMetaDto.toDto(metaEntity);
		}
		else return null;
	}
}
