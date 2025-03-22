package com.ict.vita.service.chatquestion;

import java.time.LocalDateTime;
import java.util.List;

import com.ict.vita.repository.member.MemberEntity;

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
//[질문들어올때 세션정보와 함께 넘어오는 요청을 받기 위한 DTO]
public class ChatQuestionWithSessionRequestDto {
	private Long sid; //세션id -> 요청으로 전달 안 해도 됨
	private String contents; //질문 내용
	private List<String> keywords; //질문 내용의 키워드
}
