package com.ict.vita.service.posts;

import java.time.LocalDateTime;

import com.ict.vita.service.member.MemberDto;
import com.ict.vita.util.Commons;

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
//[글(포스트) 요청 DTO]
public class PostsRequestDto {
	//글 작성자 정보는 요청헤더에서 갖고오자
	private String post_title; //글 제목
	private String post_content = ""; //글 내용
	private String post_summary = ""; //글 요약
	private String post_status = Commons.POST_STATUS_PUBLISH; //글 상태(공개/비공개/삭제)
	private String post_pass = null; //글 비밀번호
	private String post_name = null; //글 이름(별칭)
	private String post_mime_type = null; //글 타입(글/미디어파일) - 글일때는 타입이 null
	private LocalDateTime post_created_at = LocalDateTime.now(); //글 생성일
	private LocalDateTime post_modified_at = LocalDateTime.now(); //글 수정일
	
	private String comment_status = Commons.COMMENT_STATUS_OPEN; //댓글 허용 상태
	private long comment_count = 0; //댓글 개수
	
	//[PostsRequestDto를 PostsDto로 변환하는 메서드]
	//PostsDto의 memberDto필드는 따로 설정해줘야 함
	public PostsDto toDto() {
		return PostsDto.builder()
						.post_title(post_title)
						.post_content(post_content)
						.post_summary(post_summary)
						.post_pass(post_pass)
						.post_name(post_name)
						.post_mime_type(post_mime_type)
						.post_created_at(post_created_at)
						.post_modified_at(post_modified_at)
						.comment_status(comment_status)
						.comment_count(comment_count)
						.build();
	}
}
