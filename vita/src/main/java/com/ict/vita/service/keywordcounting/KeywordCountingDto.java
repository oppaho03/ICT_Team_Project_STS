package com.ict.vita.service.keywordcounting;

import com.ict.vita.repository.keywordcounting.KeywordCountingEntity;
import com.ict.vita.service.chatanswer.ChatAnswerDto;
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
//[키워드 카운팅 Dto]
public class KeywordCountingDto {
	private Long id; //PK  
	private TermsDto termsDto; //용어
	private long count = 1; //카운팅 수
	private String searched_at; //검색날짜(월간/일간)
	
	//[KeywordCountingDto를 KeywordCountingEntity로 변환하는 메서드]
	public KeywordCountingEntity toEntity() {
		return KeywordCountingEntity.builder()
				.id(id)
				.termsEntity(termsDto.toEntity())
				.count(count)
				.searchedAt(searched_at)
				.build();
	}
	
	//[KeywordCountingEntity를 KeywordCountingDto로 변환하는 메서드]
	public static KeywordCountingDto toDto(KeywordCountingEntity entity) {
		return KeywordCountingDto.builder()
				.id(entity.getId())
				.termsDto(TermsDto.toDto(entity.getTermsEntity()))
				.count(entity.getCount())
				.searched_at(entity.getSearchedAt())
				.build();
	}
}
