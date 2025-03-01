package com.ict.vita.service.member;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
//[회원 direct 로그인용 DTO]
public class MemberLoginDto {
	@NotBlank(message = "이메일을 입력하세요")
	@Email(message = "올바른 이메일 주소를 입력해주세요.") //이메일 형식에 맞는지 검사
	private String email; //이메일
	
	@NotBlank(message = "비밀번호를 입력하세요")
	private String password; //비밀번호
}
