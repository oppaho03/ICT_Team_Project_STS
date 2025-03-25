package com.ict.vita.service.chatqna;

import com.ict.vita.service.chatanswer.ChatAnswerDto;
import com.ict.vita.service.chatquestion.ChatQuestionDto;
import com.ict.vita.service.chatsession.ChatSessionDto;
import com.ict.vita.service.chatsession.ChatSessionResponseDto;

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
//[큐앤에이(관계) 응답 DTO]
public class ChatQnaResponseDto {
	private Long id; //PK
	private ChatSessionResponseDto session; //세션
	private ChatQuestionDto question; //질문
	private ChatAnswerDto answer; //답변
	private long is_matched; //매칭여부(1:매칭됨,0:매칭X)
	
	//[ChatQnaDto 를 ChatQnaResponseDto로 변환]
	public static ChatQnaResponseDto toDto(ChatQnaDto dto) {
		return ChatQnaResponseDto.builder()
				.id(dto.getId())
				.session( ChatSessionResponseDto.toDto(dto.getChatSessionDto()) )
				.question(dto.getChatQuestionDto())
				.answer(dto.getChatAnswerDto())
				.is_matched(dto.getIs_matched())
				.build();
	}
}
