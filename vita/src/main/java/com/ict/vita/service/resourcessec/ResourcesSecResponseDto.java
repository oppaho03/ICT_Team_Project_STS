package com.ict.vita.service.resourcessec;

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
//[리소스 보안 응답 객체]
public class ResourcesSecResponseDto {
	private Long id; //PK
	private PostsResponseDto post;
	private String file_name; //파일명(암호화)
	private String file_ext = ""; //파일 확장자
	private String file_url = ""; //파일URL(GUID)
	private String enc_key = ""; //암호화 키
	private long enc_status = 0; //암호화 상태(0: 암호화X)
	
}
