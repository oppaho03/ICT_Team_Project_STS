package com.ict.vita.service.others;

import java.time.LocalDateTime;

import com.ict.vita.repository.postmeta.PostMetaEntity;
import com.ict.vita.repository.posts.PostsEntity;
import com.ict.vita.repository.termmeta.TermMetaEntity;
import com.ict.vita.service.member.MemberDto;
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
//[글(포스트) DTO]
public class ObjectMetaDto {
	private Long meta_id; // 메타 ID
	private Long obj_id; // 오브젝트 ID
	private String meta_key; //메타 키
	private String meta_value; //메타 값

	public static ObjectMetaDto toDto( TermMetaEntity entity ) {
		return ObjectMetaDto.builder()
			.meta_id(entity.getMeta_id())
			.obj_id(entity.getTermsEntity().getId())
			.meta_key(entity.getMeta_key())
			.meta_value(entity.getMeta_value())
			.build();

	}
	public static ObjectMetaDto toDto( PostMetaEntity entity ) {
		return ObjectMetaDto.builder()
			.meta_id(entity.getMeta_id())
			.obj_id(entity.getPostsEntity().getId())
			.meta_key(entity.getMeta_key())
			.meta_value(entity.getMeta_value())
			.build();

	}
}
