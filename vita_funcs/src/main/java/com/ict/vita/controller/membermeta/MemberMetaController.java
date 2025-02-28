package com.ict.vita.controller.membermeta;

import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.membermeta.MemberMetaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberMetaController {
	//서비스 주입
	private final MemberMetaService memberMetaService;
	
	
}
