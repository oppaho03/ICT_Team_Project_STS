package com.ict.vita.controller.keywordcounting;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.keywordcounting.KeywordCountingRequestDto;
import com.ict.vita.service.keywordcounting.KeywordCountingResponseDto;
import com.ict.vita.service.keywordcounting.KeywordCountingService;
import com.ict.vita.service.termcategory.TermCategoryDto;
import com.ict.vita.service.termcategory.TermCategoryService;
import com.ict.vita.service.terms.TermsDto;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/keywordcounting")
public class KeywordCountingController {
	// 서비스 주입
	private final KeywordCountingService keywordCountingService;
	private final TermCategoryService termCategoryService;
	@GetMapping("/counting/{id}")
	public ResponseEntity<String> getKeywordCounting(@PathVariable long id){
		TermCategoryDto termCategoryDto = termCategoryService.findById(id);
		if(termCategoryDto == null) {
			return ResponseEntity.badRequest().body("해당하는 값이 없음");
		}
		TermsDto termsDto = termCategoryDto.getTermsDto();
		if(termsDto == null) {
			return ResponseEntity.badRequest().body("해당 termCategory에 연결된 값 미존재");
		}
		
		keywordCountingService.saveOrUpdateKeywordCount(termCategoryDto);
		
		return ResponseEntity.ok("키워드 카운팅 저장 성공"+termCategoryDto.getTermsDto().getId());
						

	}
	@PostMapping("/search")
	public ResponseEntity<List<KeywordCountingResponseDto>> getKeywordStatus(@RequestBody KeywordCountingRequestDto requestDto) {
		System.out.println(requestDto.getStartDate());
		System.out.println("id값:"+requestDto.getId());

		List<KeywordCountingResponseDto> result = keywordCountingService.getCountBetweenDates(requestDto, requestDto.getStartDate(), requestDto.getEndDate());
		return null;
//		return ResponseEntity.ok(result);
		
		
	}
	

}
