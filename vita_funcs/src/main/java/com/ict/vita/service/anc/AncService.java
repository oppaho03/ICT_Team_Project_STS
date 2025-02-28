package com.ict.vita.service.anc;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.vita.repository.anc.AncRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AncService {
	//리포지토리 주입
	private final AncRepository ancRepository;
	
	
}
