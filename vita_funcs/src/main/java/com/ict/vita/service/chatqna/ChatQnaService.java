package com.ict.vita.service.chatqna;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.vita.repository.chatqna.ChatQnaRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatQnaService {
	//리포지토리 주입
	private final ChatQnaRepository chatQnaRepository;
	
}
