package com.ict.vita.service.posts;

import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.vita.repository.posts.PostsRepository;
import com.ict.vita.util.Commons;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PostsFileService {
	//리포지토리 주입
	private final PostsRepository postsRepository;
	
}
