package com.ict.vita.test;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api2/files")
@CrossOrigin
public class PostsFileTestController2 {
	// 서비스 주입
    private final PostsFileTestService2 postsFileService;
    
//    @GetMapping
//    public void test() {
//    	System.out.println("aaa");
//    }
    
    // 파일 업로드 및 암호화 후 처리
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("postId") Long postId,
            @RequestParam("encStatus") Integer encStatus
            ) {
        try {
            // 1. 파일을 기본적으로 APP_POSTS 테이블에 저장
            postsFileService.savePostFile(file, postId);
            System.out.println("파일 업로드 성공");
            // 2. 파일을 AES256 암호화 후 APP_RESOURCES_SEC 테이블에 저장
            String encryptedFilePath = postsFileService.encryptAndSaveFile(file, postId, encStatus);
            System.out.println("암호화 파일 저장 성공");
            return ResponseEntity.ok("파일 업로드 및 분석 성공");
        } catch (Exception e) {
        	 e.printStackTrace();  // 예외 디버깅
            return ResponseEntity.status(500).body("파일 업로드 실패: " + e.getMessage());
        }
    }
    
    @PostMapping("/upload_result")
    public ResponseEntity<?> receiveResults(@RequestBody PostAnalysisRequest request) {
        try {
            // 받은 데이터를 로그로 출력
            System.out.println("Received STT result: " + request.getSttResult());
            System.out.println("Received sentiment score: " + request.getSentimentScore());
            // 분석 결과를 데이터베이스에 저장
            postsFileService.saveAnalysisResults(request.getPostId(), request.getSttResult(), request.getSentimentScore());
            return ResponseEntity.ok("결과 저장 성공");
        } catch (Exception e) {
        	 e.printStackTrace();  // 예외 디버깅
            return ResponseEntity.status(500).body("결과 저장 실패: " + e.getMessage());
        }
    }
}
