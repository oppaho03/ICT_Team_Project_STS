package com.ict.vita.controller.keywordcounting;

import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.keywordcounting.KeywordCountingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class KeywordCountingController {
	//서비스 주입
	private final KeywordCountingService keywordCountingService;
	
	
}
