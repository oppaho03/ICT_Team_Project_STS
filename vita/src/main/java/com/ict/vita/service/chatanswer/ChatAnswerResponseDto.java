package com.ict.vita.service.chatanswer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ict.vita.repository.chatanswer.ChatAnswerEntity;
import com.ict.vita.service.terms.TermsResponseDto;

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
//[답변 response DTO]
public class ChatAnswerResponseDto {
	private Long id; //PK 
	private String file_name; //파일이름
	private String intro; //인트로
	private String body; //메인내용
	private String conclusion; //결론
	
//	private List<TermsResponseDto> categories; //응답시 카테고리 목록 보여주기 위한 필드
	private Set<TermsResponseDto> categories; //응답시 카테고리 목록 보여주기 위한 필드
	
	
	//[ChatAnswerEntity를 ChatAnswerDto로 변환하는 메서드]
	public static ChatAnswerResponseDto toDto(ChatAnswerEntity entity,List<TermsResponseDto> categories) {
		return ChatAnswerResponseDto.builder()
			.id(entity.getId())
			.file_name(entity.getFile_name())
			.intro(entity.getIntro())
			.body(entity.getBody())
			.conclusion(entity.getConclusion())
			.categories(new HashSet<>(categories))
			.build();
	}
	
	//[ChatAnswerDto를 ChatAnswerDto로 변환하는 메서드]
	public static ChatAnswerResponseDto toDto(ChatAnswerDto dto,List<TermsResponseDto> categories) {
		return ChatAnswerResponseDto.builder()
			.id(dto.getId())
			.file_name(dto.getFile_name())
			.intro(dto.getIntro())
			.body(dto.getBody())
			.conclusion(dto.getConclusion())
			.categories(new HashSet<>(categories))
			.build();
	}
	
	//[ChatAnswerResponseDto를 ChatAnswerDto로 변환하는 메서드]
	public ChatAnswerDto toAnswerDto() {
		return ChatAnswerDto.builder()
				.id(id)
				.file_name(file_name)
				.intro(intro)
				.body(body)
				.conclusion(conclusion)
				.build();
	}
}
