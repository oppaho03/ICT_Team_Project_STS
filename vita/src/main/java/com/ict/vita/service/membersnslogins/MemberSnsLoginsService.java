package com.ict.vita.service.membersnslogins;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.vita.repository.member.MemberEntity;
import com.ict.vita.repository.member.MemberRepository;
import com.ict.vita.repository.membersnslogins.MemberSnsEntity;
import com.ict.vita.repository.membersnslogins.MemberSnsLoginsRepository;
import com.ict.vita.service.member.MemberDto;
import com.ict.vita.service.membermeta.MemberMetaService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberSnsLoginsService {
	//리포지토리 주입
	private final MemberSnsLoginsRepository memberSnsLoginsRepository;

	//[sns회원 테이블에 이메일에 해당하는 회원 조회]
	public MemberSnsDto getSnsMemberByEmail(String email) {
		MemberSnsEntity member = memberSnsLoginsRepository.findByEmail(email).orElse(null);
		
		return member != null ? MemberSnsDto.toDto(member) : null;
	}

	//[sns회원 테이블에 회원 등록]
	public MemberSnsDto save(MemberSnsDto loginDto) {
		MemberSnsEntity entity = memberSnsLoginsRepository.save(loginDto.toEntity());
		return entity != null ? MemberSnsDto.toDto(entity) : null;
	}
	
	//[sns회원 수정]
	public MemberSnsDto update(MemberSnsDto loginDto) {
		MemberSnsEntity entity = memberSnsLoginsRepository.save(loginDto.toEntity());
		return MemberSnsDto.toDto(entity);
	}
	
}
