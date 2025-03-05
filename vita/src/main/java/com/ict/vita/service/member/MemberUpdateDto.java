package com.ict.vita.service.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//[회원 정보 수정 요청 DTO]
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateDto {
	private String password; //비밀번호
	private String name; //이름
	private String nickname; //닉네임
	private String contact; //전화번호
	private String address; //주소
	
}
