package com.ict.vita.service.membersnslogins;

import java.time.LocalDateTime;

import com.ict.vita.service.member.MemberDto;
import com.ict.vita.service.member.MemberResponseDto;

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
//[회원 SNS 로그인 요청 DTO]
public class MemberSnsLoginsRequestDto {
	
	private String email; //이메일
	private String token; //SNS 인증 토큰
	
	/*
	private Long member; //회원
	private String login_id; //로그인ID 또는 이메일
	private String provider; //인증서버
	private String provider_id; //인증서버 발급ID
	private String access_token; //인증토큰
	private String refresh_token; //인증토큰 갱신을 위한 토큰
	private long status = 1; //상태(계정과 연결/해제)(1: 계정과 연결)
	private LocalDateTime login_modified_at = LocalDateTime.now(); //로그인 일시
	private LocalDateTime login_created_at = LocalDateTime.now(); //생성일시(최초)
	
	//[MemberSnsLoginsRequestDto를 MemberSnsLoginsDto로 변환]
	public MemberSnsLoginsDto toLoginDto(MemberDto findedMember) {
		return MemberSnsLoginsDto.builder()
				.memberDto(findedMember)
				.login_id(login_id)
				.provider(provider)
				.provider_id(provider_id)
				.access_token(access_token)
				.refresh_token(refresh_token)
				.status(status)
				.login_modified_at(login_modified_at)
				.login_created_at(login_created_at)
				.build();
	} */
}
