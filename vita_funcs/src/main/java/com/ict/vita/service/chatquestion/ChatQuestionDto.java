package com.ict.vita.service.chatquestion;

import java.time.LocalDateTime;

import com.ict.vita.repository.chatquestion.ChatQuestionEntity;
import com.ict.vita.service.chatanswer.ChatAnswerDto;

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
//[질문 DTO]
public class ChatQuestionDto {
	private Long id; //PK  
	private String content; //내용
	private LocalDateTime created_at; //생성일
	
	//[ChatQuestionDto를 ChatQuestionEntity로 변환하는 메서드]
	public ChatQuestionEntity toEntity() {
		return ChatQuestionEntity.builder()
				.id(id)
				.content(content)
				.created_at(created_at)
				.build();
	}
	
	//[ChatQuestionEntity를 ChatQuestionDto로 변환하는 메서드]
	public static ChatQuestionDto toDto(ChatQuestionEntity entity) {
		return ChatQuestionDto.builder()
				.id(entity.getId())
				.content(entity.getContent())
				.created_at(entity.getCreated_at())
				.build();
	}
}
