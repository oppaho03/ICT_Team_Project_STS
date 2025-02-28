package com.ict.vita.service.member;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import jakarta.validation.constraints.Email;
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
//[회원 로그인용 DTO]
public class MemberJoinDto {
	@NotNull
	@Email(message = "올바른 이메일 주소를 입력해주세요.") //이메일 형식에 맞는지 검사
	private String email; //이메일
	
	@NotNull
	private String password; //비밀번호
	
	@NotNull
	private String role = "USER"; //역할
	
	private String name; //이름
	
	private String nickname; //닉네임
	
	private LocalDate birth; //생년월일
	
	private char gender; //성별
	
	private String contact; //전화번호
	
	private String address; //주소
	
	private String token = ""; //활성화 토큰
	
	private LocalDateTime created_at = LocalDateTime.now(); //가입일
	
	private LocalDateTime updated_at = LocalDateTime.now(); //수정일
	
	private long status; //상태(가입/탈퇴/대기)
}
