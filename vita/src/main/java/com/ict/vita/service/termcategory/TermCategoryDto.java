package com.ict.vita.service.termcategory;

import java.util.Optional;

import com.ict.vita.repository.termcategory.TermCategoryEntity;
import com.ict.vita.service.terms.TermsDto;

import jakarta.validation.Valid;
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
//[카테고리(텍소노미) DTO]
public class TermCategoryDto {
	private Long id; //PK
	private TermsDto termsDto; //용어
	private String category; //카테고리명
	private String description; //용어에 대한 설명
	private Long count = 0L; //해당 용어에 속하는 데이터 갯수
	private Long parent = 0L; //부모 용어id(0이면 최상위 부모다)
	
	//[TermCategoryDto를 TermCategoryEntity로 변환하는 메서드]
	public TermCategoryEntity toEntity() {
		return TermCategoryEntity.builder()
				.id(id)
				.termsEntity(termsDto.toEntity())
				.category(category)
				.description(description)
				.count(count)
				.parent(parent)
				.build();
	}
	
	//[TermCategoryEntity를 TermCategoryDto로 변환하는 메서드]
	public static TermCategoryDto toDto(TermCategoryEntity entity) {
		return TermCategoryDto.builder()
				.id(entity.getId())
				.termsDto(TermsDto.toDto(entity.getTermsEntity()))
				.category(entity.getCategory())
				.description(entity.getDescription())
				.count(entity.getCount())
				.parent(entity.getParent())
				.build();
	}
	
}
