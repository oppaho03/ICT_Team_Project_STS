package com.ict.vita.service.keywordcounting;

//import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.vita.repository.keywordcounting.KeywordCountingEntity;
import com.ict.vita.repository.keywordcounting.KeywordCountingRepository;
import com.ict.vita.repository.termcategory.TermCategoryRepository;
import com.ict.vita.repository.terms.TermsEntity;
import com.ict.vita.repository.terms.TermsRepository;
import com.ict.vita.service.termcategory.TermCategoryDto;
import com.ict.vita.service.termcategory.TermCategoryService;

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

	
	
	//날짜지정 시 상위5개
	@Transactional
	public List<KeywordCountingResponseDto> getRankBetweenDates(KeywordCountingRequestDto requestDto,String startDate,String endDate){
		PageRequest pageRequest = PageRequest.of(0,5);
	    List<Object[]> rankCount = keywordCountingRepository.findKeywordRankingTop5BetweenDates(startDate,endDate,pageRequest);
	    	
	    AtomicInteger atomic = new AtomicInteger(1);
	    
	    return rankCount.stream()
	    		.map(row -> KeywordCountingResponseDto.builder()
	    				.rank(atomic.getAndIncrement())    				
	    				.keyword((String) row[0])
	    				.count((Long) row[1])
	    				.build())
	    				.collect(Collectors.toList());
	    				
	}
	//실시간 랭킹
	@Transactional
	public List<KeywordCountingResponseDto> realTimeRank(){
		/*실시간 시간단위까지 진행 시
		Sring today = LocalDateTime.now().format(DateTimeFormatter.ofpattern("yyyyMMddHH"));
		*/
		String today = LocalDate.now().format(DATE_FORMATTER);
		PageRequest pageRequest = PageRequest.of(0,10);
		List<Object[]> rtRank = keywordCountingRepository.findRealtimeRankingTop10(today,pageRequest);
		
		AtomicInteger atomic = new AtomicInteger(1);
		
		return rtRank.stream()
				.map(row -> KeywordCountingResponseDto.builder()
						.rank(atomic.getAndIncrement())
						.keyword((String) row[0])
						.count((Long) row[1])
						.build())
				.collect(Collectors.toList());
				
				
	}
	
	//월간 탑10
	@Transactional
	public List<KeywordCountingResponseDto> monthRanking(){
		PageRequest pageRequest = PageRequest.of(0, 10);
		LocalDate today = LocalDate.now();
		
		String startOfMonth = today.withDayOfMonth(1).format(DATE_FORMATTER);
		String endOfMonth = today.format(DATE_FORMATTER);
		
		
	
		
		
		List<Object[]> monthRank = keywordCountingRepository.getMonthRanking(startOfMonth,endOfMonth,pageRequest);
		AtomicInteger atomic = new AtomicInteger(1);
		
		return monthRank.stream()
				.map(row ->KeywordCountingResponseDto.builder()
				.rank(atomic.getAndIncrement())
				.keyword((String) row[0])
				.count((Long) row[1])
				.build())
				.collect(Collectors.toList());
				
				
		
				
		
	}
	
	
	
}
