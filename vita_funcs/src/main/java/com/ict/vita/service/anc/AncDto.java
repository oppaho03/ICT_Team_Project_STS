package com.ict.vita.service.anc;

import com.ict.vita.repository.anc.AncEntity;
import com.ict.vita.service.chatanswer.ChatAnswerDto;
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
//[답변카테고리(관계) DTO]
public class AncDto {
	private ChatAnswerDto chatAnswerDto;
	private TermCategoryDto termCategoryDto;
	
	//[AncDto를 AncEntity로 변환하는 메서드]
	public AncEntity toEntity() {
		return AncEntity.builder()
				.chatAnswerEntity(chatAnswerDto.toEntity())
				.termCategoryEntity(termCategoryDto.toEntity())
				.build();
	}
	
	//[AncEntity를 AncDto로 변환하는 메서드]
	public static AncDto toDto(AncEntity entity) {
		return AncDto.builder()
				.chatAnswerDto(ChatAnswerDto.toDto(entity.getChatAnswerEntity()))
				.termCategoryDto(TermCategoryDto.toDto(entity.getTermCategoryEntity()))
				.build();
	}
}
