package com.ict.vita.service.chatanswer;

import com.ict.vita.repository.chatanswer.ChatAnswerEntity;

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
//[답변 DTO]
public class ChatAnswerResponseDto {
	private Long id; //PK 
	private String file_name; //파일이름
	private String intro; //인트로
	private String body; //메인내용
	private String conclusion; //결론
	
	
	//[ChatAnswerEntity를 ChatAnswerDto로 변환하는 메서드]
	public static ChatAnswerResponseDto toDto(ChatAnswerEntity entity) {
		return ChatAnswerResponseDto.builder()
			.id(entity.getId())
			.file_name(entity.getFile_name())
			.intro(entity.getIntro())
			.body(entity.getBody())
			.conclusion(entity.getConclusion())
			.build();
	}
}
