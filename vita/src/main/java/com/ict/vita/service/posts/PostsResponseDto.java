package com.ict.vita.service.posts;

import java.time.LocalDateTime;

import com.ict.vita.repository.posts.PostsEntity;
import com.ict.vita.service.member.MemberDto;

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
//[글(포스트) 응답 DTO]
public class PostsResponseDto {
	private Long id; //PK
	private Long author; //글 작성자(MemberDto 대신 회원 아이디값)
	private String post_title; //글 제목
	private String post_content; //글 내용
	private String post_summary; //글 요약
	private String post_status; //글 상태(공개/비공개/삭제)
	private String post_pass; //글 비밀번호
	private String post_name; //글 이름(별칭)
	private String post_mime_type; //글 타입(글/미디어파일)
	private LocalDateTime post_created_at; //글 생성일
	private LocalDateTime post_modified_at; //글 수정일
	
	private String comment_status; //댓글 허용 상태
	private long comment_count; //댓글 개수

	// PostsEntity -> PostsResponseDto 변환 
	public static PostsResponseDto toDto(PostsEntity entity) {
		return PostsResponseDto.builder()
			.id(entity.getId())
			.author(MemberDto.toDto(entity.getMemberEntity()).getId())
			.post_title(entity.getPostTitle())
			.post_content(entity.getPostContent())
			.post_summary(entity.getPostSummary())
			.post_status(entity.getPostStatus())
			.post_pass(entity.getPostPass())
			.post_name(entity.getPostName())
			.post_mime_type(entity.getPostMimeType())
			.post_created_at(entity.getPostCreatedAt())
			.post_modified_at(entity.getPostModifiedAt())
			.comment_status(entity.getCommentStatus())
			.comment_count(entity.getCommentCount())
			.build();

	}
}
