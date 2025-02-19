package com.ict.vita.service.termcategory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.vita.repository.termcategory.TermCategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
//[카테고리(텍소노미) Service]
public class TermCategoryService {
	private final TermCategoryRepository termCategoryRepository;
}
