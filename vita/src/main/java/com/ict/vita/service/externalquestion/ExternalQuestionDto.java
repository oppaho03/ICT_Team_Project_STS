package com.ict.vita.service.externalquestion;

import com.ict.vita.repository.externalquestion.ExternalQuestionEntity;

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
//[외부 질문 DTO - 통계용]
public class ExternalQuestionDto {
	private Long id; //PK
	private String fileName; //파일명
	private char gender; //성별
	private String age; //나이
	private String occupation; //직업
	private String diseaseCategory; //질병 카테고리명
	private String diseaseNameKor; //한글 질병명
	private String diseaseNameEng; //영어 질병명
	private String question; //질문 내용
	
	public ExternalQuestionEntity toEntity() {
		return ExternalQuestionEntity.builder()
				.id(id)
				.fileName(fileName)
				.gender(gender)
				.age(age)
				.occupation(occupation)
				.diseaseCategory(diseaseCategory)
				.diseaseNameKor(diseaseNameKor)
				.diseaseNameEng(diseaseNameEng)
				.question(question)
				.build();
	}
	
	public static ExternalQuestionDto toDto(ExternalQuestionEntity entity) {
		return ExternalQuestionDto.builder()
				.id(entity.getId())
				.fileName(entity.getFileName())
				.gender(entity.getGender())
				.age(entity.getAge())
				.occupation(entity.getOccupation())
				.diseaseCategory(entity.getDiseaseCategory())
				.diseaseNameKor(entity.getDiseaseNameKor())
				.diseaseNameEng(entity.getDiseaseNameEng())
				.question(entity.getQuestion())
				.build();
	}
	
}
