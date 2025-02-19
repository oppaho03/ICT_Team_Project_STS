package com.ict.vita.controller.terms;

import org.springframework.stereotype.Controller;

import com.ict.vita.service.terms.TermsService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
//[용어 Controller]
public class TermsController {
	//서비스 주입
	private final TermsService termsService;
	
	
}
