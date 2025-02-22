package com.ict.vita.service.membersnslogins;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.vita.repository.membersnslogins.MemberSnsLoginsRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberSnsLoginsService {
	//리포지토리 주입
	private final MemberSnsLoginsRepository memberSnsLoginsRepository;
	
}
