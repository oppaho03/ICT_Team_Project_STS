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
	private String post_content; //글 내용
	private String post_summary; //글 요약
	private String post_status; //글 상태(공개/비공개/삭제)
	private String post_pass = null; //글 비밀번호
	private String post_name = null; //글 이름(별칭)
	private LocalDateTime post_created_at = LocalDateTime.now(); //글 생성일
	private LocalDateTime post_modified_at = LocalDateTime.now(); //글 수정일
	
	private String comment_status = Commons.COMMENT_STATUS_OPEN; //댓글 허용 상태
	private long comment_count = 0; //댓글 개수
}
