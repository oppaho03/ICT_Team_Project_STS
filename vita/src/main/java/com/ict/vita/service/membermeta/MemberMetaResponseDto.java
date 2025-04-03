package com.ict.vita.service.membermeta;

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
//[회원 메타정보 반환용 DTO]
public class MemberMetaResponseDto {
	private Long meta_id; //PK
	private String meta_key; //메타 키
	private String meta_value; //메타 값
	
	//[MemberMetaDto를 MemberMetaResponseDto로 변환]
	public static MemberMetaResponseDto toResponseDto(MemberMetaDto dto) {
		return MemberMetaResponseDto.builder()
					.meta_id(dto.getMeta_id())
					.meta_key(dto.getMeta_key())
					.meta_value(dto.getMeta_value())
					.build();
	}
}
