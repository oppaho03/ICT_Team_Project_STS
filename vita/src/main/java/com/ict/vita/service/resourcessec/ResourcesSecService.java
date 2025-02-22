package com.ict.vita.service.resourcessec;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.vita.repository.resourcessec.ResourcesSecRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ResourcesSecService {
	//리포지토리 주입
	private final ResourcesSecRepository resourcesSecRepository;
	
}
