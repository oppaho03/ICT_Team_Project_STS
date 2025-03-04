package com.ict.vita.service.chatsession;

import java.time.LocalDateTime;

import com.ict.vita.service.member.MemberDto;

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
//[대화 세션 요청 DTO]
public class ChatSessionRequestDto {
	@NotNull(message = "세션의 회원 정보를 입력하세요")
	private MemberDto memberDto; //회원
	
	@NotNull
	private LocalDateTime created_at = LocalDateTime.now(); //생성일
	
	@NotNull
	private LocalDateTime updated_at = LocalDateTime.now(); //수정일
	
	@NotNull
	private long status = 1; //상태(공개(0) / 비공개(1)(디폴트))
	
	@NotNull
	private long count = 0; //카운트(공개상태일때 외부인이 조회한 횟수)
}
