package com.ict.vita.controller.termmeta;

import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.termmeta.TermMetaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TermMetaController {
	//서비스 주입
	private final TermMetaService termMetaService;
	
	
}
