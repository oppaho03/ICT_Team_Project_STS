package com.ict.vita.controller.posts;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ict.vita.service.posts.PostsService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/")
public class PostsController {
	//서비스 주입
	private final PostsService postsService;
	
	/**
	 * 모두 검색
	 */
	
	
}
