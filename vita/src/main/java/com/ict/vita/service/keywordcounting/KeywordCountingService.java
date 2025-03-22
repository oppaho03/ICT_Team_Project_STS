package com.ict.vita.service.keywordcounting;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.vita.repository.keywordcounting.KeywordCountingEntity;
import com.ict.vita.repository.keywordcounting.KeywordCountingRepository;
import com.ict.vita.repository.termcategory.TermCategoryRepository;
import com.ict.vita.repository.terms.TermsEntity;
import com.ict.vita.repository.terms.TermsRepository;
import com.ict.vita.service.termcategory.TermCategoryDto;
import com.ict.vita.service.termcategory.TermCategoryService;
import com.ict.vita.service.terms.TermsDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class KeywordCountingService {
	//리포지토리 주입
	private final KeywordCountingRepository keywordCountingRepository;
	private final TermCategoryService termCategoryService;
	//날짜 형식 상수 설정
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
	
	
    /**
     *  용어(TermsDto)를 받아서 키워드 카운트 증가 또는 신규 저장
     *  @param termsDto 검색된 키워드 정보 (TermsDto)
     *  @return 업데이트된 키워드 카운팅 정보 (KeywordCountingDto)
     */
	@Transactional
	public KeywordCountingDto saveOrUpdateKeywordCount (TermCategoryDto termCategoryDto) {
		LocalDate today = LocalDate.now();
		String searchedDate = today.format(DATE_FORMATTER);
		
		KeywordCountingEntity existCount = keywordCountingRepository.findByTermsEntityAndSearchedAt(termCategoryDto.toEntity().getTermsEntity(),searchedDate);
		
		if(existCount != null) {
			existCount.setCount(existCount.getCount() + 1);
			keywordCountingRepository.save(existCount);
			return KeywordCountingDto.toDto(existCount);
		}
		else {
			KeywordCountingEntity newCount = KeywordCountingEntity.builder()
					.termsEntity(termCategoryDto.toEntity().getTermsEntity())
					.count(1)
					.searchedAt(searchedDate)
					.build();
			keywordCountingRepository.save(newCount);
			return KeywordCountingDto.toDto(newCount);		
		}
		
		
	
	}
	//시작일자~지정일자 사이에 그키워드가 얼마나 검색되었는지?
	@Transactional
	public List<KeywordCountingResponseDto> getCountBetweenDates(KeywordCountingRequestDto requestDto,String startDate,String endDate){
		System.out.println("id값"+requestDto.getId());
		TermCategoryDto termCategoryDto = termCategoryService.findById(requestDto.getId());
	    TermsEntity termsEntity = termCategoryDto.toEntity().getTermsEntity();
	    
	    List<Object[]> results =
	        keywordCountingRepository.findKeywordCountBetweenDates(termsEntity, startDate, endDate);

	    return results.stream()
	        .map(row -> KeywordCountingResponseDto.builder()
	            .keyword((String) row[0])
	            .count((Long) row[1])
	            
	            .build())
	        .collect(Collectors.toList());
						
	}
	
}
