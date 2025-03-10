package com.ict.vita.service.others;

import com.ict.vita.repository.postmeta.PostMetaEntity;
import com.ict.vita.repository.termmeta.TermMetaEntity;
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
public class ObjectMetaRequestDto {

	private Long id; // 오브젝트 ID
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
