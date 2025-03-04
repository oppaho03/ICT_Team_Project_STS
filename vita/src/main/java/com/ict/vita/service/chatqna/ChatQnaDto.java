package com.ict.vita.service.chatqna;

import com.ict.vita.repository.chatqna.ChatQnaEntity;
import com.ict.vita.service.chatanswer.ChatAnswerDto;
import com.ict.vita.service.chatquestion.ChatQuestionDto;
import com.ict.vita.service.chatsession.ChatSessionDto;

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
//[큐앤에이(관계) DTO]
public class ChatQnaDto {
	private Long id; //PK
	private ChatSessionDto chatSessionDto; //세션
	private ChatQuestionDto chatQuestionDto; //질문
	private ChatAnswerDto chatAnswerDto; //답변
	private long is_matched; //매칭여부(1:매칭됨,0:매칭X)
	
	//[ChatQnaDto를 ChatQnaEntity로 변환하는 메서드]
	public ChatQnaEntity toEntity() {
		return ChatQnaEntity.builder()
				.id(id)
				.chatSessionEntity(chatSessionDto.toEntity())
				.chatQuestionEntity(chatQuestionDto.toEntity())
				.chatAnswerEntity(chatAnswerDto.toEntity())
				.is_matched(is_matched)
				.build();
	}
	
	//[ChatQnaEntity를 ChatQnaDto로 변환하는 메서드]
	public static ChatQnaDto toDto(ChatQnaEntity entity) {
		return ChatQnaDto.builder()
				.id(entity.getId())
				.chatSessionDto(ChatSessionDto.toDto(entity.getChatSessionEntity()))
				.chatQuestionDto(ChatQuestionDto.toDto(entity.getChatQuestionEntity()))
				.chatAnswerDto(ChatAnswerDto.toDto(entity.getChatAnswerEntity()))
				.is_matched(entity.getIs_matched())
				.build();
	}
}
