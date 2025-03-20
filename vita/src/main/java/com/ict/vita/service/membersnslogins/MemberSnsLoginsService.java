package com.ict.vita.service.membersnslogins;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.vita.repository.member.MemberEntity;
import com.ict.vita.repository.membersnslogins.MemberSnsLoginsEntity;
import com.ict.vita.repository.membersnslogins.MemberSnsLoginsRepository;
import com.ict.vita.service.member.MemberDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberSnsLoginsService {
	//리포지토리 주입
	private final MemberSnsLoginsRepository memberSnsLoginsRepository;

	//[sns회원 테이블에 이메일에 해당하는 회원 조회]
	public MemberSnsLoginsDto getSnsMemberByEmail(String email) {
		MemberSnsLoginsEntity member = memberSnsLoginsRepository.findByEmail(email).orElse(null);
		return member != null ? MemberSnsLoginsDto.toDto(member) : null;
	}

	//[sns회원 테이블에 회원 등록]
	public MemberSnsLoginsDto save(MemberSnsLoginsDto loginDto) {
		MemberSnsLoginsEntity entity = memberSnsLoginsRepository.save(loginDto.toEntity());
		return entity != null ? MemberSnsLoginsDto.toDto(entity) : null;
	}
	
}
