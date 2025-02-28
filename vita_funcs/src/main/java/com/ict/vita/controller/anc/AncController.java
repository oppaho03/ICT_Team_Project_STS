package com.ict.vita.controller.anc;

import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.anc.AncService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AncController {
	//서비스 주입
	private final AncService ancService;
	
	
}
