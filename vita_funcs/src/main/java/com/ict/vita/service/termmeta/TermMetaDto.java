package com.ict.vita.service.termmeta;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ict.vita.repository.termmeta.TermMetaEntity;
import com.ict.vita.service.member.MemberDto;
import com.ict.vita.service.terms.TermsDto;

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
//[용어메타 DTO]
public class TermMetaDto {
	private Long meta_id; //PK
	private TermsDto termsDto; //용어
	private String meta_key; //메타 키
	private String meta_value; //메타 값
	
	//[TermMetaDto를 TermMetaEntity로 변환하는 메서드]
	public TermMetaEntity toEntity() {
		return TermMetaEntity.builder()
				.meta_id(meta_id)
				.termsEntity(termsDto.toEntity())
				.meta_key(meta_key)
				.meta_value(meta_value)
				.build();
	}
	
	//[TermMetaEntity를 TermMetaDto로 변환하는 메서드]
	public static TermMetaDto toDto(TermMetaEntity entity) {
		return TermMetaDto.builder()
				.meta_id(entity.getMeta_id())
				.termsDto(TermsDto.toDto(entity.getTermsEntity()))
				.meta_key(entity.getMeta_key())
				.meta_value(entity.getMeta_value())
				.build();
	}
}
