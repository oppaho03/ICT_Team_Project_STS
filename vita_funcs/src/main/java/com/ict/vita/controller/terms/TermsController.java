package com.ict.vita.controller.terms;


import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.terms.TermsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
//[용어 Controller]
public class TermsController {
	//서비스 주입
	private final TermsService termsService;
	
	
}
