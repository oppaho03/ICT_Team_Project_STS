package com.ict.vita.service.keywordcounting;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.vita.repository.keywordcounting.KeywordCountingEntity;
import com.ict.vita.repository.keywordcounting.KeywordCountingRepository;
import com.ict.vita.repository.termcategory.TermCategoryRepository;
import com.ict.vita.repository.terms.TermsRepository;
import com.ict.vita.service.termcategory.TermCategoryDto;
import com.ict.vita.service.terms.TermsDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class KeywordCountingService {
	//리포지토리 주입
	private final KeywordCountingRepository keywordCountingRepository;
	private final TermCategoryRepository termCategoryRepository;
	
    /**
     *  용어(TermsDto)를 받아서 키워드 카운트 증가 또는 신규 저장
     */

	@Transactional
	public void saveOrUpdateKeywordCount (TermCategoryDto termCategoryDto) {
		LocalDate today = LocalDate.now();
		String serachedDate = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		
		KeywordCountingEntity existCount = keywordCountingRepository.findByTermsEntityAndSearchedAt(termCategoryDto.toEntity().getTermsEntity(),serachedDate);
		
		if(existCount != null) {
			existCount.setCount(existCount.getCount() + 1);
			keywordCountingRepository.save(existCount);
		}
		else {
			KeywordCountingEntity newCount = KeywordCountingEntity.builder()
					.termsEntity(termCategoryDto.toEntity().getTermsEntity())
					.count(1)
					.searchedAt(serachedDate)
					.build();
			keywordCountingRepository.save(newCount);
					
		}
	
	}
	
	
	
}
