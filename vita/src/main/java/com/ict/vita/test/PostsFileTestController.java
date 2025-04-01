package com.ict.vita.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.util.UriEncoder;

import com.ict.vita.service.member.MemberDto;
import com.ict.vita.service.member.MemberService;
import com.ict.vita.service.posts.PostsDto;
import com.ict.vita.util.Commons;
import com.ict.vita.util.ResultUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
@CrossOrigin
public class PostsFileTestController {
	 // 서비스 주입
    private final PostsFileTestService postsFileService;
    private final MemberService memberService;
    
    private final MessageSource messageSource;
    
    @Value("${file.upload-dir}")
    private String uploadDir; //업로드 디렉터리

    // 파일 업로드 및 암호화 후 처리
    /*
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("postId") Long postId,
            @RequestParam("encStatus") Integer encStatus,
            @RequestParam("AUTHOR") Integer postAuthor) {
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
    } */
  
    @PostMapping(value = "/upload")
    public ResponseEntity<?> uploadFile(
    		@RequestHeader(name = Commons.AUTHORIZATION) String token,
    		FileUploadDto fileInfo) {
//    		@RequestPart(value = "file",required = false) MultipartFile file) {
//    		@RequestParam(value = "file",required = false) MultipartFile file) {
    	
    	//토큰으로 회원 조회
    	MemberDto loginMember = memberService.findMemberByToken(token); 
    	//회원이 존재하지 않는 경우
		if(loginMember == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail( messageSource.getMessage("user.invalid_token", null, new Locale("ko")) ));
		} 
		
		System.out.println("[파일업로드]");
		System.out.println("파일명:"+fileInfo.getFile().getOriginalFilename());
		System.out.println("파일타입:"+fileInfo.getFile().getContentType());

		//업로드 디렉터리 경로에 회원id 추가
		uploadDir += String.valueOf(loginMember.getId());
		
		// 업로드 디렉터리가 없으면 디렉터리 생성
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            try {
				Files.createDirectories(uploadPath);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("[파일 업로드]업로드 디렉터리 생성 실패");
			}
        }
		
		PostsDto post = PostsDto.builder()
							.memberDto(loginMember)
							.post_title( fileInfo.getFile().getOriginalFilename() )
							.post_content(null)
							.post_summary(null)
							.post_status(Commons.POST_STATUS_PUBLISH)
							.post_pass( null )
							.post_name( UriEncoder.encode( fileInfo.getFile().getOriginalFilename() ) )
							.post_mime_type( fileInfo.getFile().getContentType() )
							.post_created_at(LocalDateTime.now())
							.post_modified_at(LocalDateTime.now())
							.comment_status(Commons.COMMENT_STATUS_OPEN)
							.comment_count(0)
							.build();  
							
//		// 1. 파일을 기본적으로 APP_POSTS 테이블에 저장
        postsFileService.savePostFile(post);
        System.out.println("파일 APP_POSTS 테이블에 저장");
    	
       return null;
    }
    
//    @PostMapping("/upload_result")
//    public ResponseEntity<?> receiveResults(@RequestBody PostAnalysisRequest request) {
//        try {
//            // 받은 데이터를 로그로 출력
//            System.out.println("Received STT result: " + request.getSttResult());
//            System.out.println("Received sentiment score: " + request.getSentimentScore());
//            // 분석 결과를 데이터베이스에 저장
//            postsFileService.saveAnalysisResults(request.getPostId(), request.getSttResult(), request.getSentimentScore());
//            return ResponseEntity.ok("결과 저장 성공");
//        } catch (Exception e) {
//        	 e.printStackTrace();  // 예외 디버깅
//            return ResponseEntity.status(500).body("결과 저장 실패: " + e.getMessage());
//        }
//    }
}


