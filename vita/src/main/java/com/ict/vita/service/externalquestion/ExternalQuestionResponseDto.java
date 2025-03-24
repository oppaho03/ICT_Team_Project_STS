package com.ict.vita.service.externalquestion;

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
//[외부 질문 응답 DTO - 통계용]
public class ExternalQuestionResponseDto {
	private String age_group;
	private char gender;
	private String occupation;
	
	private String category;
	private int question_count;
}
