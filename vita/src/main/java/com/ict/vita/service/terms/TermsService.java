package com.ict.vita.service.terms;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.vita.repository.termcategory.TermCategoryRepository;
import com.ict.vita.repository.terms.TermsEntity;
import com.ict.vita.repository.terms.TermsRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
//[용어 Service]
public class TermsService {
	//리포지토리 주입
	private final TermsRepository termsRepository;
	private final TermCategoryRepository termCategoryRepository;
	
	/**
	 * 전체 APP_TERMS 리스트 불러오기
	 * @return
	 */
	public List<TermsDto> findAll() {
		
		List<TermsEntity> terms = termsRepository.findAll( Sort.by(Sort.Order.asc("id")) );
		
		return terms.isEmpty() 
				? null 
				: terms.stream().map( term -> new TermsDto().toDto(term) ).collect(Collectors.toList());
		
	}
	
	/**
	 * APP_TERMS.name 으로 검색	
	 * @param name - 용어 이름
	 * @return
	 */
	public List<TermsDto> findAllByName(String name) {
		List<TermsEntity> terms = termsRepository.findAllByName( name, Sort.by(Sort.Order.asc("id")) );
		return terms.isEmpty() 
				? null 
				: terms.stream().map( term -> new TermsDto().toDto(term) ).collect(Collectors.toList());
	}
	
	/**
	 * APP_TERMS.slug 으로 검색
	 * @param slug - 용어 슬러그
	 * @param taxonomy - 카테고리 (텍소노미)
	 * @return
	 */
	public List<TermsDto> findAllBySlug(String slug, String taxonomy) {	
		
		System.out.println( taxonomy );
		System.out.println( "aaa" );
//		System.out.println( termCategoryRepository.findTermCategoryBySlug(slug, taxonomy) );
		System.out.println( termCategoryRepository.findTermCategoryBySlug(slug, taxonomy) );
		System.out.println( "aaa" );
		
		return null;
	}
	
	
}
