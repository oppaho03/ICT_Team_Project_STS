package com.ict.vita.service.postmeta;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.vita.repository.postmeta.PostMetaRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PostMetaService {
	//리포지토리 주입
	private final PostMetaRepository postMetaRepository;

	
}
