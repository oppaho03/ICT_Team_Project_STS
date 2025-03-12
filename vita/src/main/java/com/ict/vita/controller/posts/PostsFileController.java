package com.ict.vita.controller.posts;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import com.ict.vita.service.posts.PostsService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class PostsFileController {
	//서비스 주입
	private final PostsService postsService;
	
	
}
