package com.ict.vita.service.member;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import com.ict.vita.util.Commons;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
@Schema(description = "회원 DTO (회원가입)")
//[회원 direct 회원가입용 DTO]
public class MemberJoinDto {
	@Schema(description = "이메일", example = "test@example.com")
	@NotBlank(message = "이메일을 입력하세요")
	@Email(message = "올바른 이메일 주소를 입력하세요.") //이메일 형식에 맞는지 검사
	private String email; //이메일
	
	@Schema(description = "비밀번호", example = "hashed_password")
	@NotBlank(message = "비밀번호를 입력하세요")
	private String password; //비밀번호
	
	@Schema(description = "사용자 역할", example = "USER")
	@NotBlank(message = "role을 지정하세요")
	private String role = Commons.ROLE_USER; //역할
	
	@Schema(description = "이름", example = "홍길동")
	private String name; //이름
	
	@Schema(description = "닉네임", example = "gildong123")
	private String nickname; //닉네임
	
	@Schema(description = "생년월일", example = "1990-01-01")
	private LocalDate birth; //생년월일
	
	@Schema(description = "성별 (M: 남성, F: 여성)", example = "M")
	private char gender; //성별
	
	@Schema(description = "전화번호", example = "000-0000-0000")
	private String contact; //전화번호
	
	@Schema(description = "주소", example = "서울시 강남구 역삼동")
	private String address; //주소
	
	@Schema(description = "JWT 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
	private String token = ""; //활성화 토큰
	
	@Schema(description = "가입일", example = "2025-03-03T05:46:09.470Z")
	private LocalDateTime created_at = LocalDateTime.now(); //가입일
	
	@Schema(description = "수정일", example = "2025-03-03T05:46:09.470Z")
	private LocalDateTime updated_at = LocalDateTime.now(); //수정일
	
	@Schema(description = "상태 (1: 가입, 0: 탈퇴, 9: 대기)", example = "1")
	private long status; //상태(가입/탈퇴/대기)
	
	@Schema(description = "이메일 인증이 됐는지 여부 확인(0: 인증X, 1: 인증O)",example = "1")
	private int isEmailAuth; //이메일 인증여부(이메일 인증 후 회원가입 처리시 사용)
}
