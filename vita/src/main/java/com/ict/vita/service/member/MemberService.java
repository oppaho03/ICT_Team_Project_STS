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
	
<<<<<<< HEAD
	/** 
=======
	/**
>>>>>>> branch 'master' of https://github.com/oppaho03/ICT_Team_Project_STS.git
	 * 이메일 중복 여부 판단
	 * @param email 입력된 이메일 주소
	 * @return 가입된 이메일이 존재하는지 여부
	 */
	public boolean isExistsEmail(String email) {
		return memberRepository.existsByEmail(email);
	}
	
<<<<<<< HEAD
	/** 
=======
	/**
>>>>>>> branch 'master' of https://github.com/oppaho03/ICT_Team_Project_STS.git
	 * 전화번호 중복 여부 판단
	 * @param contact 입력된 전화번호
	 * @return 가입된 전화번호가 존재하는지 여부
	 */
	public boolean isExistsContact(String contact) {
		return memberRepository.existsByContact(contact);
	}
	
<<<<<<< HEAD
	/**
	 * 회원가입 처리
	 * @param MemberDto 사용자가 입력한 정보가 있는 DTO객체
	 * @return MemberDto 회원가입된 후 반환된 DTO객체
	 */
	public MemberDto join(MemberDto dto) {	
=======
	
	/**
	 * 회원가입 처리
	 * @param member 회원정보를 담은 객체
	 * @return
	 */
	public MemberDto signup(MemberDto member) {	
>>>>>>> branch 'master' of https://github.com/oppaho03/ICT_Team_Project_STS.git
		//[회원가입 처리]
<<<<<<< HEAD
		String nickname = "";
=======
		//회원 닉네임 설정
		String nickname = "";
		//<닉네임 미입력시>
		if(member.getNickname() == null || member.getNickname().isBlank()) { //닉네임을 입력 안 한 경우
			nickname = member.getNickname().substring(0, member.getNickname().indexOf("@")); //@ 전까지를 닉네임으로 지정
			System.out.println("MemberService 회원가입 - 회원 닉네임: "+nickname);
			//회원 저장
			MemberEntity entity = memberRepository.save(MemberEntity.builder()
								.email(member.getEmail())
								.password(member.getPassword())
								.role(member.getRole())
								.name(member.getName())
								.nickname(nickname)
								.birth(member.getBirth())
								.gender(member.getGender())
								.contact(member.getContact())
								.address(member.getAddress())
								.token(member.getToken())
								.created_at(member.getCreated_at())
								.updated_at(member.getUpdated_at())
								.status(1) //회원가입:1 / 탈퇴:0 / 대기:9
								.build());
			return MemberDto.toDto(entity);
		}
		//<닉네임 입력시>
		//회원 저장
		MemberEntity entity = memberRepository.save(MemberEntity.builder()
								.email(member.getEmail())
								.password(member.getPassword())
								.role(member.getRole())
								.name(member.getName())
								.nickname(member.getNickname())
								.birth(member.getBirth())
								.gender(member.getGender())
								.contact(member.getContact())
								.address(member.getAddress())
								.token(member.getToken())
								.created_at(member.getCreated_at())
								.updated_at(member.getUpdated_at())
								.status(1) //회원가입:1 / 탈퇴:0 / 대기:9
								.build());
		return MemberDto.toDto(entity);
>>>>>>> branch 'master' of https://github.com/oppaho03/ICT_Team_Project_STS.git
		
<<<<<<< HEAD
		//회원 저장
		//닉네임을 입력 안 한 경우
		if(dto.getNickname() == null || dto.getNickname().trim().length() == 0) { 
			nickname = dto.getEmail().substring(0, dto.getEmail().indexOf("@")); //이메일의 @ 전까지를 닉네임으로 지정
			System.out.println("MemberService 회원가입 - 회원 닉네임: "+nickname);
			MemberEntity entity = MemberEntity.builder()
					.email(dto.getEmail())
					.password(dto.getPassword())
					.name(dto.getName())
					.nickname(nickname)
					.birth(dto.getBirth())
					.gender(dto.getGender())
					.contact(dto.getContact())
					.address(dto.getAddress())
					.token("test토큰입니다") //테스트용 토큰
					.status(1) //회원가입:1 / 탈퇴:0 / 대기:9
					.role(dto.getRole())
					.created_at(LocalDateTime.now())
					.updated_at(dto.getUpdated_at())
					.build();
			//entity.setUpdated_at(entity.getCreated_at());
			memberRepository.save(entity);
			return MemberDto.toDto(entity);
		}
		//닉네임을 입력한 경우
		else {
			MemberEntity entity = MemberEntity.builder()
					.email(dto.getEmail())
					.password(dto.getPassword())
					.name(dto.getName())
					.nickname(dto.getNickname())
					.birth(dto.getBirth())
					.gender(dto.getGender())
					.contact(dto.getContact())
					.address(dto.getAddress())
					.token("test토큰입니다") //테스트용 토큰
					.status(1) //회원가입:1 / 탈퇴:0 / 대기:9
					.role(dto.getRole())
					.created_at(LocalDateTime.now())
					.updated_at(dto.getUpdated_at())
					.build();
			//entity.setUpdated_at(entity.getCreated_at());
			memberRepository.save(entity);
			return MemberDto.toDto(entity);
		}
=======
>>>>>>> branch 'master' of https://github.com/oppaho03/ICT_Team_Project_STS.git
	}
	
}
