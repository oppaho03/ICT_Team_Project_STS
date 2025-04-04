package com.ict.vita.controller.resourcessec;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.resourcessec.ResourcesSecService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class ResourcesSecController {
	//서비스 주입
	private final ResourcesSecService resourcesSecService;
	
}
