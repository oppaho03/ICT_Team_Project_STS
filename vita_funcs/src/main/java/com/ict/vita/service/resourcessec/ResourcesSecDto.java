package com.ict.vita.service.resourcessec;

import com.ict.vita.repository.posts.PostsEntity;
import com.ict.vita.repository.resourcessec.ResourcesSecEntity;
import com.ict.vita.service.chatanswer.ChatAnswerDto;
import com.ict.vita.service.posts.PostsDto;

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
//[리소스 보안 DTO]
public class ResourcesSecDto {
	private Long id; //PK
	private PostsDto postsDto; //글(포스트)
	private String file_name; //파일명(암호화)
	private String file_ext = ""; //파일 확장자
	private String file_url = ""; //파일URL(GUID)
	private String enc_key = ""; //암호화 키
	private long enc_status = 0; //암호화 상태(0: 암호화X)
	
	//[ResourcesSecDto를 ResourcesSecEntity로 변환하는 메서드]
	public ResourcesSecEntity toEntity() {
		return ResourcesSecEntity.builder()
				.id(id)
				.postsEntity(postsDto.toEntity())
				.file_name(file_name)
				.file_ext(file_ext)
				.file_url(file_url)
				.enc_key(enc_key)
				.enc_status(enc_status)
				.build();
	}
	
	//[ResourcesSecEntity를 ResourcesSecDto로 변환하는 메서드]
	public static ResourcesSecDto toDto(ResourcesSecEntity entity) {
		return ResourcesSecDto.builder()
				.id(entity.getId())
				.postsDto(PostsDto.toDto(entity.getPostsEntity()))
				.file_name(entity.getFile_name())
				.file_ext(entity.getFile_ext())
				.file_url(entity.getFile_url())
				.enc_key(entity.getEnc_key())
				.enc_status(entity.getEnc_status())
				.build();
	}
}
