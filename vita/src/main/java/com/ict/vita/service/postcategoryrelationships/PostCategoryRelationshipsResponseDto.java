package com.ict.vita.service.postcategoryrelationships;

import java.util.List;

import com.ict.vita.repository.postcategoryrelationships.PostCategory;
import com.ict.vita.repository.postcategoryrelationships.PostCategoryRelationshipsEntity;
import com.ict.vita.service.posts.PostsDto;
import com.ict.vita.service.termcategory.TermCategoryDto;
import com.ict.vita.service.terms.TermDto;

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
//글 카테고리(관계) DTO]
public class PostCategoryRelationshipsResponseDto {
	private Long post_id; //글(포스트)
	private List<TermDto> categories; //카테고리
	
	
	//[PostCategoryRelationshipsEntity를 PostCategoryRelationshipsResponseDto로 변환하는 메서드]
	public static PostCategoryRelationshipsResponseDto toDto(PostCategoryRelationshipsEntity entity) {
		return PostCategoryRelationshipsResponseDto.builder()
				.post_id(PostsDto.toDto(entity.getPostsEntity()).getId())
				.termCategoryDto(TermCategoryDto.toDto(entity.getTermCategoryEntity()))
				.build();
	}
}
