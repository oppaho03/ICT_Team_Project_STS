package com.ict.vita.service.posts;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;

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
public class PostsFileService {
	//리포지토리 주입
	private final PostsRepository postsRepository;
    private final ResourcesSecRepository resourcesSecRepository;
    private final PostMetaRepository postMetaRepository;
    
    //************* vita.properties 파일에 추가해야 함!!
    private static final String STT_API_URL = "http://127.0.0.1:8000/api/files/upload_result";  // STT API URL
    private static final String SENTIMENT_API_URL = "http://127.0.0.1:8000/uploadfiles/upload_result";  // Sentiment API URL
    private static final String SPRINGBOOT_API_URL = "http://localhost:8080/api/files/upload_result"; // 스프링부트에서 결과 받는 API URL
    
    /**
     * [월별 음성파일 조회]
     * @param period 기간 ex."2025-04"
     * @param type 검색할 파일 타입
     * @return
     */
	public List<PostsDto> getMonthlyVoiceFiles(String period, String type) {
		YearMonth yearMonth = YearMonth.parse(period); //string에서 년월 얻기
		LocalDateTime start = yearMonth.atDay(1).atStartOfDay(); //달의 시작 시간 ex)2025-04-01 00:00:00
		LocalDateTime end = yearMonth.atEndOfMonth().atTime(LocalTime.MAX); //달의 끝 시간 ex)2025-04-30 23:59:59.999999999
		
		List<PostsEntity> finded = postsRepository.findAllByPeriod(start, end, type);
		
		return finded.stream().map(entity -> PostsDto.toDto(entity)).toList();
	}
   
    
}
