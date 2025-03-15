package com.ict.vita.service.termcategory;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.vita.repository.termcategory.TermCategoryEntity;
import com.ict.vita.repository.termcategory.TermCategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
//[카테고리(텍소노미) Service]
public class TermCategoryService {
	//레포지토리 주입	
	private final TermCategoryRepository termCategoryRepository;
	
	//카테고리가 존재하는지 유무 확인
	public boolean isExistCategory(Long cid) {
		return termCategoryRepository.existsById(cid);
	}

	/**
	 * [카테고리id로 카테고리 조회]
	 * @param id 카테고리id
	 */
	@Transactional(readOnly = true)
	public TermCategoryDto findById(Long id) {
		TermCategoryEntity findedCategory = termCategoryRepository.findById(id).orElse(null);
		if(findedCategory != null)
			return TermCategoryDto.toDto(findedCategory);
		
		return null;
	}
}
