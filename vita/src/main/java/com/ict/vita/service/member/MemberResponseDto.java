package com.ict.vita.service.member;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.ict.vita.repository.member.MemberEntity;
import com.ict.vita.service.membermeta.MemberMetaResponseDto;
import com.ict.vita.service.posts.PostsDto;

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
public class MemberResponseDto {
	private Long id; //PK
	private String email; //이메일
	private String role; // 역할
	private String name; //이름
	private String nickname; //닉네임
	private LocalDate birth; //생년월일
	private char gender; //성별
	private String contact; //전화번호
	private String address; //주소
	private String token; //활성화 토큰
	private LocalDateTime created_at = LocalDateTime.now(); //가입일
	private LocalDateTime updated_at = LocalDateTime.now(); //수정일
	private long status; //상태(1:가입/0:탈퇴/9:대기)
	
	private List<MemberMetaResponseDto> meta;
	
	//[MemberDto 를 MemberResponseDto로]
	public static MemberResponseDto toDto(MemberDto memberDto, List<MemberMetaResponseDto> meta) {
		return MemberResponseDto.builder()
								.id(memberDto.getId())
								.email(memberDto.getEmail())
								.role(memberDto.getRole())
								.name(memberDto.getName())
								.nickname(memberDto.getNickname())
								.birth(memberDto.getBirth())
								.gender(memberDto.getGender())
								.contact(memberDto.getContact())
								.address(memberDto.getAddress())
								.token(memberDto.getToken())
								.created_at(memberDto.getCreated_at())
								.updated_at(memberDto.getUpdated_at())
								.status(memberDto.getStatus())
								.meta(meta)
								.build();
	}
	
	//[MemberEntity 를 MemberResponseDto로]
	public static MemberResponseDto toDto(MemberEntity memberEntity, List<MemberMetaResponseDto> meta) {
		return MemberResponseDto.builder()
				.id(memberEntity.getId())
				.email(memberEntity.getEmail())
				.role(memberEntity.getRole())
				.name(memberEntity.getName())
				.nickname(memberEntity.getNickname())
				.birth(memberEntity.getBirth())
				.gender(memberEntity.getGender())
				.contact(memberEntity.getContact())
				.address(memberEntity.getAddress())
				.token(memberEntity.getToken())
				.created_at(memberEntity.getCreated_at())
				.updated_at(memberEntity.getUpdated_at())
				.status(memberEntity.getStatus())
				.meta(meta)
				.build();
	}
}
