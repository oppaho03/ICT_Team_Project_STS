package com.ict.vita.controller.termcategory;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.termcategory.TermCategoryService;
import com.ict.vita.service.terms.TermsDto;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
//[카테고리(텍소노미) Controller]
public class TermCategoryController {
	//서비스 주입
	private final TermCategoryService termCategoryService;


	/**
	 * 카테고리(APP_TERM_CATEGORY) 전체 리스트 검색
	 * @return null 또는 배열 반환
	 */
	@GetMapping("/")
	@Operation( summary = "카테고리(APP_TERM_CATEGORY) 전체 리스트 검색", description = "카테고리(APP_TERM_CATEGORY) 전체 리스트 검색" )
	public ResponseEntity<?> findAll() {
		
		return null;
	} 


	/**
	  * 용어(및 카테고리) slug 검색 
	 * APP_TERM.slug 와 APP_TERM_CATEGORY.category 
	 * APP_TERM + APP_TERM_CATEGORY
	 * @param slug - 용어 슬러그
	 * @param taxonomy - 카테고리명 (텍소노미)
	 * @return
	 */ 

}
