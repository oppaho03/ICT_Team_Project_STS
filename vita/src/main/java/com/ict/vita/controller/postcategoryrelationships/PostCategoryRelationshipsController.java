package com.ict.vita.controller.postcategoryrelationships;

import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.postcategoryrelationships.PostCategoryRelationshipsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PostCategoryRelationshipsController {
	//서비스 주입
	private final PostCategoryRelationshipsService postCategoryRelationshipsService;
	
	
}
