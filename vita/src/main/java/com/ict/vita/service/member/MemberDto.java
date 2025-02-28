package com.ict.vita.service.member;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ict.vita.repository.member.MemberEntity;
import com.ict.vita.service.terms.TermsDto;

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
//[회원 DTO]
public class MemberDto {
	private Long id; //PK
	private String email; //이메일
	private String password; //비밀번호
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
