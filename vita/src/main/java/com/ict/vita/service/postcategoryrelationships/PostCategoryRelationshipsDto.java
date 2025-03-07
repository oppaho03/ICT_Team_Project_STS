package com.ict.vita.service.postcategoryrelationships;

import com.ict.vita.repository.postcategoryrelationships.PostCategory;
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
		// 복합키(PostCategory) 생성
        PostCategory postCategoryId = new PostCategory();
        postCategoryId.setPost_id(postsDto.getId()); // post_id 설정
        postCategoryId.setTerm_category_id(termCategoryDto.getId()); // term_category_id 설정
		
		return PostCategoryRelationshipsEntity.builder()
				.id(postCategoryId) //복합키 설정
				.postsEntity(postsDto.toEntity())
				.termCategoryEntity(termCategoryDto.toEntity())
				.build();
	}
	
	//[PostCategoryRelationshipsEntity를 PostCategoryRelationshipsDto로 변환하는 메서드]
	public static PostCategoryRelationshipsDto toDto(Object entity) {
		return PostCategoryRelationshipsDto.builder()
				.postsDto(PostsDto.toDto(entity.getPostsEntity()))
				.termCategoryDto(TermCategoryDto.toDto(entity.getTermCategoryEntity()))
				.build();
	}
}
