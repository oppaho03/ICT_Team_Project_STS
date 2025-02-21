package com.ict.vita.service.terms;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.vita.repository.terms.TermsRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
//[용어 Service]
public class TermsService {
	//리포지토리 주입
	private final TermsRepository termsRepository;
	
	
}
