package com.ict.vita.service.termmeta;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.vita.repository.termmeta.TermMetaRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TermMetaService {
	//리포지토리 주입
	private final TermMetaRepository termMetaRepository;
	
	
}
