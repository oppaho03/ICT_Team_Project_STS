package com.ict.vita.service.chatsession;

import java.time.LocalDateTime;

import com.ict.vita.repository.chatsession.ChatSessionEntity;
import com.ict.vita.repository.member.MemberEntity;
import com.ict.vita.service.chatquestion.ChatQuestionDto;
import com.ict.vita.service.member.MemberDto;

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
//[대화 세션 DTO]
public class ChatSessionDto {
	private Long id; //PK
	private MemberDto memberDto; //회원
	private LocalDateTime created_at = LocalDateTime.now(); //생성일
	private LocalDateTime updated_at = LocalDateTime.now(); //수정일
	private long status; //상태(공개/비공개)
	private long count = 0; //카운트(공개상태일때 외부인이 조회한 횟수)
	
	//[ChatSessionDto를 ChatSessionEntity로 변환하는 메서드]
	public ChatSessionEntity toEntity() {
		return ChatSessionEntity.builder()
				.id(id)
				.memberEntity(memberDto.toEntity())
				.created_at(created_at)
				.updated_at(updated_at)
				.status(status)
				.count(count)
				.build();
	}
	
	//[ChatSessionEntity를 ChatSessionDto로 변환하는 메서드]
	public static ChatSessionDto toDto(ChatSessionEntity entity) {
		return ChatSessionDto.builder()
				.id(entity.getId())
				.memberDto(MemberDto.toDto(entity.getMemberEntity()))
				.created_at(entity.getCreated_at())
				.updated_at(entity.getUpdated_at())
				.status(entity.getStatus())
				.count(entity.getCount())
				.build();
	}
}
