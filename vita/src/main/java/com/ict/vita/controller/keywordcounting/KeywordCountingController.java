package com.ict.vita.controller.keywordcounting;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.keywordcounting.KeywordCountingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class KeywordCountingController {
	//서비스 주입
	private final KeywordCountingService keywordCountingService;
	
	
}
