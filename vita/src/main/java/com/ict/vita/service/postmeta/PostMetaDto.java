package com.ict.vita.service.postmeta;

import com.ict.vita.repository.postmeta.PostMetaEntity;
import com.ict.vita.service.posts.PostsDto;
import com.ict.vita.service.termmeta.TermMetaDto;
import com.ict.vita.service.terms.TermsDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//[글(포스트) 메타 DTO]
public class PostMetaDto {
	private Long meta_id; //PK
	private PostsDto postsDto;
	private String meta_key; //메타 키
	private String meta_value; //메타 값

	//[PostMetaDto를 PostMetaEntity로 변환하는 메서드]
	public PostMetaEntity toEntity() {
		return PostMetaEntity.builder()
				.meta_id(meta_id)
				.postsEntity(postsDto.toEntity())
				.meta_key(meta_key)
				.meta_value(meta_value)
				.build();
	}
	
	//[PostMetaEntity를 PostMetaDto로 변환하는 메서드]
	public static PostMetaDto toDto(PostMetaEntity entity) {
		return PostMetaDto.builder()
				.meta_id(entity.getMeta_id())
				.postsDto(PostsDto.toDto(entity.getPostsEntity()))
				.meta_key(entity.getMeta_key())
				.meta_value(entity.getMeta_value())
				.build();
	}
}
