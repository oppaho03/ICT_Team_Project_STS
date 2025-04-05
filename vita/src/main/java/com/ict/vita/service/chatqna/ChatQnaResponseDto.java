package com.ict.vita.service.chatqna;

import java.util.List;

import com.ict.vita.service.chatanswer.ChatAnswerDto;
import com.ict.vita.service.chatanswer.ChatAnswerResponseDto;
import com.ict.vita.service.chatquestion.ChatQuestionDto;
import com.ict.vita.service.chatsession.ChatSessionDto;
import com.ict.vita.service.chatsession.ChatSessionResponseDto;
import com.ict.vita.service.membermeta.MemberMetaResponseDto;
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
//[큐앤에이(관계) 응답 DTO]
public class ChatQnaResponseDto {
	private Long id; //PK
	private ChatSessionResponseDto session; //세션
	private ChatQuestionDto question; //질문
	//private ChatAnswerDto answer; //답변
	private ChatAnswerResponseDto answer; //답변
	private long is_matched; //매칭여부(1:매칭됨,0:매칭X)
	
	//[ChatQnaDto 를 ChatQnaResponseDto로 변환]
	public static ChatQnaResponseDto toDto(
			ChatQnaDto qna, 
			ChatSessionDto session, 
			String qContent,
			List<TermsResponseDto> categories,
			List<MemberMetaResponseDto> meta
			) {
		
		ChatSessionResponseDto sessionResponse = ChatSessionResponseDto.toDto(qna.getChatSessionDto(), meta );
		sessionResponse.setLastQuestion(qContent);
		
		return ChatQnaResponseDto.builder()
				.id(qna.getId())
				.session( sessionResponse )
				.question(qna.getChatQuestionDto())
				.answer( ChatAnswerResponseDto.toDto(qna.getChatAnswerDto(), categories) )
				.is_matched(qna.getIs_matched())
				.build();
	}
}
