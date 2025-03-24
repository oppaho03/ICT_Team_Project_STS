package com.ict.vita.service.keywordcounting;

import com.ict.vita.repository.keywordcounting.KeywordCountingEntity;
import com.ict.vita.repository.termcategory.TermCategoryEntity;
import com.ict.vita.service.termcategory.TermCategoryDto;
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
public class KeywordCountingResponseDto {
	private int rank;
	private String keyword;
	private Long count;
	


}
