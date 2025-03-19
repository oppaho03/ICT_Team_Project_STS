package com.ict.vita.service.keywordcounting;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.vita.repository.keywordcounting.KeywordCountingRepository;
import com.ict.vita.repository.terms.TermsRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class KeywordCountingService {
	//리포지토리 주입
	private final KeywordCountingRepository keywordCountingRepository;
	private final TermsRepository termsRepository;
	
	
	
}
 