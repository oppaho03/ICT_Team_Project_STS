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
//[글(포스트) DTO]
public class PostsResponseDto {
	private Long id; //PK
	private Long author; //글 작성자
	private String post_title; //글 제목
	private String post_content; //글 내용
	private String post_summary; //글 요약
	private String post_status = "PUBLISH"; //글 상태(공개/비공개)
	private String post_pass = ""; //글 비밀번호
	private String post_name = ""; //글 이름(별칭)
	private String post_mime_type = ""; //글 타입(글/미디어파일)
	private LocalDateTime post_created_at; //글 생성일
	private LocalDateTime post_modified_at; //글 수정일
	private String comment_status = "OPEN"; //댓글 허용 상태
	private long comment_count = 0; //댓글 개수

	// PostsDto -> PostsResponseDto 변환 
	public static PostsResponseDto toDto(PostsEntity entity) {
		return PostsResponseDto.builder()
			.id(entity.getId())
			.author(MemberDto.toDto(entity.getMemberEntity()).getId())
			.post_title(entity.getPost_title())
			.post_content(entity.getPost_content())
			.post_summary(entity.getPost_summary())
			.post_status(entity.getPost_status())
			.post_pass(entity.getPost_pass())
			.post_name(entity.getPost_name())
			.post_mime_type(entity.getPost_mime_type())
			.post_created_at(entity.getPost_created_at())
			.post_modified_at(entity.getPost_modified_at())
			.comment_status(entity.getComment_status())
			.comment_count(entity.getComment_count())
			.build();

	}
}
