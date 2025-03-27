package com.ict.vita.service.chatquestion;

import java.util.List;

import com.ict.vita.service.chatanswer.ChatAnswerDto;
import com.ict.vita.service.chatanswer.ChatAnswerResponseDto;
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
//[질문과 세션 정보를 함께 반환하는 DTO]
public class ChatQuestionWithSessionResponseDto {
	public Long sid; //세션id
	public String question; //질문내용
	public List<ChatAnswerResponseDto> answers; //질문의 키워드로 검색한 답변 리스트
}
