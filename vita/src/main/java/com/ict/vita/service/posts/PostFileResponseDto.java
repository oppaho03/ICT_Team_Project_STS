package com.ict.vita.service.posts;

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
//[파일 정보를 반환할 DTO]
public class PostFileResponseDto {
	private PostsResponseDto postResponse; //글+카테고리 정보
	private PostMetaResponseDto metaResponse; //메타 정보
	
	public static PostFileResponseDto toDto(PostsResponseDto postResponse, PostMetaResponseDto metaResponse) {
		return PostFileResponseDto.builder()
				.postResponse(postResponse)
				.metaResponse(metaResponse)
				.build();
	}
}
