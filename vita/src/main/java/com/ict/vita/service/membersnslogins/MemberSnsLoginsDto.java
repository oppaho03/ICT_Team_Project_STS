package com.ict.vita.service.membersnslogins;

import java.time.LocalDateTime;

import com.ict.vita.repository.membersnslogins.MemberSnsLoginsEntity;
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
//[회원 SNS 로그인 DTO]
public class MemberSnsLoginsDto {
	private Long id; //PK
	private MemberDto memberDto; //회원
	private String login_id = ""; //로그인ID 또는 이메일
	private String provider; //인증서버
	private String provider_id; //인증서버 발급ID
	private String access_token; //인증토큰
	private String refresh_token; //인증토큰 갱신을 위한 토큰
	private long status = 1; //상태(계정과 연결/해제)(1: 계정과 연결)
	private LocalDateTime login_modified_at; //로그인 일시
	private LocalDateTime login_created_at; //생성일시(최초)
	
	//[MemberSnsLoginsDto를 MemberSnsLoginsEntity로 변환하는 메서드]
	public MemberSnsLoginsEntity toEntity() {
		return MemberSnsLoginsEntity.builder()
				.id(id)
				.memberEntity(memberDto.toEntity())
				.login_id(login_id)
				.provider(provider)
				.provider_id(provider_id)
				.access_token(access_token)
				.refresh_token(refresh_token)
				.status(status)
				.login_modified_at(login_modified_at)
				.login_created_at(login_created_at)
				.build();
	}
	
	//[MemberSnsLoginsEntity를 MemberSnsLoginsDto로 변환하는 메서드]
	public static MemberSnsLoginsDto toDto(MemberSnsLoginsEntity entity) {
		return MemberSnsLoginsDto.builder()
				.id(entity.getId())
				.memberDto(MemberDto.toDto(entity.getMemberEntity()))
				.login_id(entity.getLogin_id())
				.provider(entity.getProvider())
				.provider_id(entity.getProvider_id())
				.access_token(entity.getAccess_token())
				.refresh_token(entity.getRefresh_token())
				.status(entity.getStatus())
				.login_modified_at(entity.getLogin_modified_at())
				.login_created_at(entity.getLogin_created_at())
				.build();
	}
}
