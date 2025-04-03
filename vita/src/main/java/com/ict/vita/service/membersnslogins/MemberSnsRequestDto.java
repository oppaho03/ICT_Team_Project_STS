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
public class MemberSnsRequestDto {
	//[필수값]
	private String email; //이메일
	private String access_token; //SNS 인증 토큰
	private String provider; //인증서버
	private String provider_id; //인증서버 발급id(UNIQUE)
	
	//[옵션값]
	private String name; //이름
	private String picture; //사진url
	
}
