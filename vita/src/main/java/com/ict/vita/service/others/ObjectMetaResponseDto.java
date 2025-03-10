package com.ict.vita.service.others;

import java.time.LocalDateTime;

import com.ict.vita.repository.postmeta.PostMetaEntity;
import com.ict.vita.repository.posts.PostsEntity;
import com.ict.vita.repository.termmeta.TermMetaEntity;
import com.ict.vita.service.member.MemberDto;
import com.ict.vita.service.posts.PostsDto;

import jakarta.persistence.Transient;
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
public class ObjectMetaResponseDto {

	private Long meta_id; // 메타 ID
	private String meta_key; //메타 키
	private String meta_value; //메타 값

	public static ObjectMetaResponseDto toDto( TermMetaEntity entity ) {
		return ObjectMetaResponseDto.builder()
			.meta_id(entity.getMetaId())
			.meta_key(entity.getMetaKey())
			.meta_value(entity.getMetaValue())
			.build();
	}

	public static ObjectMetaResponseDto toDto( PostMetaEntity entity ) {
		return ObjectMetaResponseDto.builder()
			.meta_id(entity.getMetaId())
			.meta_key(entity.getMetaKey())
			.meta_value(entity.getMetaValue())
			.build();
	}
	
}
