package com.ict.vita.controller.postmeta;

import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.postmeta.PostMetaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PostMetaController {
	//서비스 주입
	private final PostMetaService postMetaService;
	
	
}
