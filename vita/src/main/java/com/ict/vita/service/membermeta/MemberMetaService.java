package com.ict.vita.service.membermeta;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.vita.repository.membermeta.MemberMetaRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberMetaService {
	//리포지토리 주입
	private final MemberMetaRepository memberMetaRepository;
	
	
}
