package com.ict.vita.service.postcategoryrelationships;

import com.ict.vita.repository.postcategoryrelationships.PostCategoryRelationshipsEntity;
import com.ict.vita.service.anc.AncDto;
import com.ict.vita.service.chatanswer.ChatAnswerDto;
import com.ict.vita.service.posts.PostsDto;
import com.ict.vita.service.termcategory.TermCategoryDto;

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
public class PostCategoryRelationshipsDto {
	private PostsDto postsDto; //글(포스트)
	private TermCategoryDto termCategoryDto; //카테고리
	
	//[PostCategoryRelationshipsDto를 PostCategoryRelationshipsEntity로 변환하는 메서드]
	public PostCategoryRelationshipsEntity toEntity() {
		return PostCategoryRelationshipsEntity.builder()
				.postsEntity(postsDto.toEntity())
				.termCategoryEntity(termCategoryDto.toEntity())
				.build();
	}
	
	//[PostCategoryRelationshipsEntity를 PostCategoryRelationshipsDto로 변환하는 메서드]
	public static PostCategoryRelationshipsDto toDto(PostCategoryRelationshipsEntity entity) {
		return PostCategoryRelationshipsDto.builder()
				.postsDto(PostsDto.toDto(entity.getPostsEntity()))
				.termCategoryDto(TermCategoryDto.toDto(entity.getTermCategoryEntity()))
				.build();
	}
}
