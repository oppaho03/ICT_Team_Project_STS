package com.ict.vita.service.postmeta;

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
//[글 메타 정보 반환용 DTO]
public class PostMetaResponseDto {
	private Long id; //메타id
	private String key; //메타 key
	private String value; //메타 value
	
	//PostMetaDto를 PostMetaResponseDto로 변환
	public static PostMetaResponseDto toResponseDto(PostMetaDto dto) {
		return PostMetaResponseDto.builder()
				.id(dto.getMeta_id())
				.key(dto.getMeta_key())
				.value(dto.getMeta_value())
				.build();
	}
}
