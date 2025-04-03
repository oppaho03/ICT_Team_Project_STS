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
//[글 메타 DTO]
public class PostMetaDto {
	private Long id; //메타id
	private Long postId; //글id
	private String key; //메타 key
	private String value; //메타 value
}
