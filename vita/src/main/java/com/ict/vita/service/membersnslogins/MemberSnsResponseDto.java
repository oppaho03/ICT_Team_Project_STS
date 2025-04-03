package com.ict.vita.service.membersnslogins;

import java.time.LocalDateTime;
import java.util.List;

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
//[회원 SNS 응답용 DTO]
public class MemberSnsResponseDto {
	private Long id; //PK
	private MemberResponseDto member; //회원
	private String login_id; //로그인ID 또는 이메일
	private String provider; //인증서버
	private String provider_id; //인증서버 발급ID
	private String access_token; //인증토큰
	private String refresh_token; //인증토큰 갱신을 위한 토큰
	private long status = 1; //상태(계정과 연결/해제)(1: 계정과 연결)
	private LocalDateTime login_modified_at; //로그인 일시
	private LocalDateTime login_created_at; //생성일시(최초)
	
	//[MemberSnsDto를 MemberSnsResponseDto로 변환]
//	public static MemberSnsResponseDto toResponseDto(MemberSnsDto snsDto) {
//		return MemberSnsResponseDto.builder()
//				.id(snsDto.getId())
//				.member(null) //**************
//				.login_id(snsDto.getLogin_id())
//				.provider(snsDto.getProvider())
//				.provider_id(snsDto.getProvider_id())
//				.access_token(snsDto.getAccess_token())
//				.refresh_token(snsDto.getRefresh_token())
//				.status(snsDto.getStatus())
//				.login_created_at(snsDto.getLogin_created_at())
//				.login_modified_at(snsDto.getLogin_modified_at())
//				.build();
//	}
	
	public static MemberSnsResponseDto toResponseDto(MemberSnsDto snsDto,MemberDto memberDto,List<MemberMetaResponseDto> meta) {
		return MemberSnsResponseDto.builder()
				.id(snsDto.getId())
				.member(MemberResponseDto.toDto(memberDto, meta)) //**************
				.login_id(snsDto.getLogin_id())
				.provider(snsDto.getProvider())
				.provider_id(snsDto.getProvider_id())
				.access_token(snsDto.getAccess_token())
				.refresh_token(snsDto.getRefresh_token())
				.status(snsDto.getStatus())
				.login_created_at(snsDto.getLogin_created_at())
				.login_modified_at(snsDto.getLogin_modified_at())
				.build();
	}
}
