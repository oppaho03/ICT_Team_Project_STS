package com.ict.vita.service.keywordcounting;

import com.ict.vita.repository.keywordcounting.KeywordCountingEntity;
import com.ict.vita.service.termcategory.TermCategoryDto;

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
public class KeywordCountingRequestDto {

	private Long id;
	private String startDate;
	private String endDate;
	
	
}
