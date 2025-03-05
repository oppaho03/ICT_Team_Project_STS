package com.ict.vita.controller.termcategory;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.repository.terms.TermsRepository;
import com.ict.vita.service.termcategory.TermCategoryService;
import com.ict.vita.service.terms.TermsDto;
import com.ict.vita.service.terms.TermsService;
import com.ict.vita.util.ResultUtil;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/terms/categories")
//[카테고리(텍소노미) Controller]
public class TermCategoryController {
	//서비스 주입
	// private final TermsService termsService;
	// private final TermCategoryService termCategoryService;
}
