package com.ict.vita.service.chatquestion;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
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
//[질문 요청 DTO]
public class ChatQuestionRequestDto {
	@NotNull
	private String content; //내용
	
	@NotNull
	private LocalDateTime created_at = LocalDateTime.now(); //생성일
}
