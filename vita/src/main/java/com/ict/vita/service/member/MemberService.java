package com.ict.vita.service.member;

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
	
	/*
	//회원가입 처리
	public MemberDto signup(MemberDto dto) {
		//이메일 중복 여부 판단
		boolean isExistingEmail = memberRepository.existsByEmail(dto.getEmail());
		//전화번호 중복 여부 판단
		boolean isExistingContact = memberRepository.existsByContact(dto.getContact());
		
		//이메일,전화번호가 중복되지않은 경우 
		//[회원가입 처리]
		if(!(isExistingEmail || isExistingContact)) {
			//회원 닉네임 설정
			String nickname = "";
			if(dto.getNickname() == null | dto.getNickname().trim().length() == 0) { //닉네임을 입력 안 한 경우
				nickname = dto.getNickname().substring(0, dto.getNickname().indexOf("@")); //@ 전까지를 닉네임으로 지정
				System.out.println("MemberService 회원가입 - 회원 닉네임: "+nickname);
			}
			//회원 저장
			MemberEntity entity = memberRepository.save(MemberEntity.builder()
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
												.build()
			);
			return MemberDto.toDto(entity);
		}///if
		
		//이메일이나 전화번호가 중복되어
		//[회원가입이 불가능한 경우]
		return null;
	}
	*/
}
