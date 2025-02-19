package com.ict.vita.controller.termcategory;

import org.springframework.stereotype.Controller;

import com.ict.vita.service.termcategory.TermCategoryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
//[카테고리(텍소노미) Controller]
public class TermCategoryController {
	//서비스 주입
	private final TermCategoryService termCategoryService;
	
	
}
