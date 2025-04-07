package com.ict.vita.test.hjh;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ict.vita.repository.postmeta.PostMetaRepository;
import com.ict.vita.repository.posts.PostsEntity;
import com.ict.vita.repository.posts.PostsRepository;
import com.ict.vita.repository.resourcessec.ResourcesSecRepository;
import com.ict.vita.service.posts.PostsDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PostsFileTestService {
	
	private final PostsRepository postsRepository;
    private final ResourcesSecRepository resourcesSecRepository;
    private final PostMetaRepository postMetaRepository;
    
    //***** 상수는 따로 파일로 하는게 좋을 것 같아요!!! 
    //***** 지금 url은 src/main/resource 아래 config/vita.properties 에 URL주소 넣으려고 하고있으니 거기에 넣어주세요!! 
    private static final String STT_API_URL = "http://127.0.0.1:8000/api/files/upload_result";  // STT API URL
    private static final String SENTIMENT_API_URL = "http://127.0.0.1:8000/uploadfiles/upload_result";  // Sentiment API URL
    private static final String SPRINGBOOT_API_URL = "http://localhost:8080/api/files/upload_result"; // 스프링부트에서 결과 받는 API URL
    
    //APP_POSTS 테이블에 저장
    public PostsDto savePostFile(PostsDto post) {
    	PostsEntity savedPost = postsRepository.save( post.toEntity() );
    	return PostsDto.toDto(savedPost);
    }
    
    
}
