package com.ict.vita.service.chatsession;

import java.time.LocalDateTime;
import java.util.List;

import com.ict.vita.repository.chatsession.ChatSessionEntity;
import com.ict.vita.service.member.MemberDto;
import com.ict.vita.service.member.MemberResponseDto;
import com.ict.vita.service.membermeta.MemberMetaResponseDto;

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
//[대화 세션 응답 DTO]
public class ChatSessionResponseDto {
	private Long id; //PK
//	private Long member; //회원
	private MemberResponseDto member; //회원
	private LocalDateTime created_at; //생성일
	private LocalDateTime updated_at; //수정일
	private long status; //상태(공개(0) / 비공개(1)(디폴트))
	private long count; //카운트(공개상태일때 외부인이 조회한 횟수)
	
	private String lastQuestion; //해당 세션의 마지막 질문 내용
	
	//ChatSessionDto 를 ChatSessionResponseDto 로 변환
//	public static ChatSessionResponseDto toDto(ChatSessionDto sessionDto) {
	public static ChatSessionResponseDto toDto(ChatSessionDto sessionDto, List<MemberMetaResponseDto> meta ) {
		return ChatSessionResponseDto.builder()
				.id(sessionDto.getId())
//				.member(sessionDto.getMemberDto().getId())
				.member( MemberResponseDto.toDto(sessionDto.getMemberDto(), meta) )
				.created_at(sessionDto.getCreated_at())
				.updated_at(sessionDto.getUpdated_at())
				.status(sessionDto.getStatus())
				.count(sessionDto.getCount())
				.build();
	}
	
	//ChatSessionEntity 를 ChatSessionResponseDto 로 변환
	public static ChatSessionResponseDto toDto(ChatSessionEntity entity, List<MemberMetaResponseDto> meta ) {
		return ChatSessionResponseDto.builder()
				.id(entity.getId())
//				.member(entity.getMemberEntity().getId())
				.member( MemberResponseDto.toDto(entity.getMemberEntity(), meta) )
				.created_at(entity.getCreatedAt())
				.updated_at(entity.getUpdatedAt())
				.status(entity.getStatus())
				.count(entity.getCount())
				.build();
	}
	
}
