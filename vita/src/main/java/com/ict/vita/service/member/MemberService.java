package com.ict.vita.service.member;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.vita.repository.member.MemberEntity;
import com.ict.vita.repository.member.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
	//리포지토리 주입
	private final MemberRepository memberRepository;
	
	/**
	 * 이메일 중복 여부 판단
	 * @param email 입력된 이메일 주소
	 * @return 가입된 이메일이 존재하는지 여부
	 */
	public boolean isExistsEmail(String email) {
		return memberRepository.existsByEmail(email);
	}

	/**
	 * 전화번호 중복 여부 판단
	 * @param contact 입력된 전화번호
	 * @return 가입된 전화번호가 존재하는지 여부
	 */
	public boolean isExistsContact(String contact) {
		return memberRepository.existsByContact(contact);
	}
	

	/**
	 * 회원가입 처리
	 * @param member 회원정보를 담은 객체
	 * @return
	 */
	public MemberDto join(MemberJoinDto joinDto) {	
		String nickname = "";
		//<닉네임 미입력시>
		if(joinDto.getNickname() == null || joinDto.getNickname().isBlank()) { 
			nickname = joinDto.getEmail().substring(0, joinDto.getEmail().indexOf("@")); //이메일에서 @ 전까지를 닉네임으로 지정
			System.out.println("MemberService 회원가입 - 회원 닉네임: "+nickname);
			//회원 저장
			MemberEntity entity = memberRepository.save(MemberEntity.builder()
								.email(joinDto.getEmail())
								.password(joinDto.getPassword())
								.role(joinDto.getRole())
								.name(joinDto.getName())
								.nickname(nickname)
								.birth(joinDto.getBirth())
								.gender(joinDto.getGender())
								.contact(joinDto.getContact())
								.address(joinDto.getAddress())
								.token(joinDto.getToken())
								.created_at(joinDto.getCreated_at())
								.updated_at(joinDto.getUpdated_at())
								.status(9) //회원가입:1 / 탈퇴:0 / 대기:9
								.build());
			return MemberDto.toDto(entity);
		}
		//<닉네임 입력시>
		//회원 저장
		MemberEntity entity = memberRepository.save(MemberEntity.builder()
								.email(joinDto.getEmail())
								.password(joinDto.getPassword())
								.role(joinDto.getRole())
								.name(joinDto.getName())
								.nickname(joinDto.getNickname())
								.birth(joinDto.getBirth())
								.gender(joinDto.getGender())
								.contact(joinDto.getContact())
								.address(joinDto.getAddress())
								.token(joinDto.getToken())
								.created_at(joinDto.getCreated_at())
								.updated_at(joinDto.getUpdated_at())
								.status(9) //회원가입:1 / 탈퇴:0 / 대기:9
								.build());
		return MemberDto.toDto(entity);

	}
	
}
