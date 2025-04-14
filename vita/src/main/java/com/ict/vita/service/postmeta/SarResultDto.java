package com.ict.vita.service.postmeta;

import java.util.List;

import com.ict.vita.service.posts.PostsResponseDto;

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
//[음성분석결과 반환 객체]
public class SarResultDto {
	private PostsResponseDto post;
	private List<PostMetaResponseDto> meta;
}
