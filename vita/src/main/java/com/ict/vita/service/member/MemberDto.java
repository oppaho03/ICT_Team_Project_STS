package com.ict.vita.service.member;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ict.vita.repository.member.MemberEntity;
import com.ict.vita.service.terms.TermsDto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "회원 DTO")
//[회원 DTO]
public class MemberDto {
	@Schema(description = "회원 ID", example = "1")
	private Long id; //PK
	@Schema(description = "이메일", example = "test@example.com")
	private String email; //이메일
	@Schema(description = "비밀번호", example = "hashed_password")
	private String password; //비밀번호
	@Schema(description = "사용자 역할", example = "USER")
	private String role = "USER"; //역할
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
	private long status; //상태(1:가입/0:탈퇴/9:대기)
	
	//[MemberDto를 MemberEntity로 변환하는 메서드]
	public MemberEntity toEntity() {
		return MemberEntity.builder()
				.id(id)
				.email(email)
				.password(password)
				.role(role)
				.name(name)
				.nickname(nickname)
				.birth(birth)
				.gender(gender)
				.contact(contact)
				.address(address)
				.token(token)
				.created_at(created_at)
				.updated_at(updated_at)
				.status(status)
				.build();
	}
	
	//[MemberEntity를 MemberDto로 변환하는 메서드]
	public static MemberDto toDto(MemberEntity entity) {
		return MemberDto.builder()
				.id(entity.getId())
				.email(entity.getEmail())
				.password(entity.getPassword())
				.role(entity.getRole())
				.name(entity.getName())
				.nickname(entity.getNickname())
				.birth(entity.getBirth())
				.gender(entity.getGender())
				.contact(entity.getContact())
				.address(entity.getAddress())
				.token(entity.getToken())
				.created_at(entity.getCreated_at())
				.updated_at(entity.getUpdated_at())
				.status(entity.getStatus())
				.build();
	}
}
