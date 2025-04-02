package com.ict.vita.test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ict.vita.repository.postmeta.PostMetaEntity;
import com.ict.vita.repository.posts.PostsEntity;
import com.ict.vita.repository.posts.PostsRepository;
import com.ict.vita.repository.resourcessec.ResourcesSecEntity;
import com.ict.vita.util.EncryptAES256;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PostsFileTestService2 {
	 private final PostsRepository postsRepository;
	    private final ResourcesSecRepository2 resourcesSecRepository;
	    private final PostMetaRepository2 postMetaRepository;
	    
	    @PersistenceContext
	    private EntityManager entityManager;
	    
//	    @PersistenceContext
//	    private EntityManager entityManager2;
	    
	    //*****
	    private static final String STT_API_URL = "http://127.0.0.1:8000/api/files/upload_result";  // STT API URL
	    private static final String SENTIMENT_API_URL = "http://localhost:8080/api/files/upload_result";  // Sentiment API URL
	    private static final String SPRINGBOOT_API_URL = "http://localhost:8080/api/files/upload_result"; // 스프링부트에서 결과 받는 API URL
	    
//	    @Transactional
	    // 파일을 업로드하고 APP_POSTS에 저장하는 메서드
	    public void savePostFile(MultipartFile file, Long postId) throws Exception {
	     // 수정된 부분
	     PostsEntity postsEntity = postsRepository.findById(postId)   ///******
	         .orElseThrow(() -> new Exception("Post not found with id: " + postId));
	        String fileName = file.getOriginalFilename();
	        // 파일 관련 정보를 설정합니다.
	        ResourcesSecEntity resourcesSecEntity = new ResourcesSecEntity();
	        resourcesSecEntity.setPostsEntity(postsEntity); // 게시물과 연결
	        resourcesSecEntity.setFile_name(fileName);
	        resourcesSecEntity.setFile_ext(getFileExtension(fileName));
	        // 파일 업로드 디렉토리 경로
	        String filePath = "C:/server_files/" + UUID.randomUUID().toString() + "_" + fileName;
	        Path path = Paths.get(filePath);
	        Files.write(path, file.getBytes());
	        resourcesSecEntity.setFile_url(filePath); // 실제 파일 저장 경로
	        resourcesSecRepository.save(resourcesSecEntity);
	        // 게시물 내용 저장 (APP_POSTS 테이블에 삽입)
	        postsEntity.setPostTitle(fileName); // 파일명으로 게시물 제목 설정
	        postsEntity.setPostContent("파일이 업로드 되었습니다."); // 파일에 대한 내용 (기본 설명)
	        postsEntity.setPostStatus("PUBLISH"); // 게시물 상태는 "PUBLISH"
	        postsRepository.save(postsEntity);
	    }
	    
//	    @Transactional
	    // 파일 이름을 AES256 암호화 후 저장하는 메서드
	    public String encryptAndSaveFile(MultipartFile file, Long postId, Integer encStatus) throws Exception {
	        String fileName = file.getOriginalFilename();
	        String encryptedFileName = EncryptAES256.encrypt(fileName); // 파일 이름만 암호화
	        // 암호화된 파일 이름을 사용하여 파일을 저장할 경로 설정
	        String encryptedFilePath = "C:/server_files/" + encryptedFileName;
	        // 파일을 저장
	        Path path = Paths.get(encryptedFilePath);
	        Files.write(path, file.getBytes()); // 파일 내용 그대로 저장
	        // 암호화된 파일 정보를 APP_RESOURCES_SEC
	        ResourcesSecEntity resourcesSecEntity = new ResourcesSecEntity();
	        resourcesSecEntity.setPostsEntity(postsRepository.findById(postId).orElseThrow(() -> new Exception("Post not found with id: " + postId))); // 게시물 연결
	        resourcesSecEntity.setEnc_key(encryptedFileName); // 암호화된 파일 이름을 enc_key에 설정
	        resourcesSecEntity.setFile_name(fileName); // 원본 파일 이름을 file_name에 설정
	        resourcesSecEntity.setFile_ext(getFileExtension(fileName)); // 파일 확장자 설정
	        resourcesSecEntity.setFile_url(encryptedFilePath); // 파일 URL 설정
	        resourcesSecEntity.setEnc_status(encStatus); // 암호화 상태 설정
	        resourcesSecRepository.save(resourcesSecEntity); // APP_RESOURCES_SEC 테이블에 저장
	        // 게시물 내용 저장 (APP_POSTS 테이블에 삽입)
	        PostsEntity postsEntity = postsRepository.findById(postId).orElseThrow(() -> new Exception("Post not found with id: " + postId));
	        postsEntity.setPostTitle(fileName); // 파일명으로 게시물 제목 설정
	        postsEntity.setPostContent("암호화된 파일이 업로드 되었습니다."); // 파일에 대한 내용 (기본 설명)
	        postsEntity.setPostStatus("PUBLISH"); // 게시물 상태는 "PUBLISH"
	        postsRepository.save(postsEntity);
	        return encryptedFilePath; // 암호화된 파일 경로 반환
	    }
	    
	    // 메타 데이터를 저장하는 메서드
	 // 메타데이터 저장 메서드 (중복 저장 방지)
	    private void saveMetaData(Long postId, String metaKey, String metaValue) {
	        // 해당 게시물(postId)과 메타 키(metaKey)가 이미 존재하는지 확인
	        PostMetaEntity existingMeta = postMetaRepository.findByPostsEntityAndMetaKey(
	            postsRepository.findById(postId)
	                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId)),
	            metaKey
	        );
	        if (existingMeta == null) {
	            // 메타데이터가 없다면 새로운 데이터를 추가
	            PostMetaEntity postMetaEntity = new PostMetaEntity();
	            PostsEntity postsEntity = postsRepository.findById(postId)
	                    .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
	            postMetaEntity.setPostsEntity(postsEntity);
	            postMetaEntity.setMetaKey(metaKey);
	            postMetaEntity.setMetaValue(metaValue);
	            postMetaRepository.save(postMetaEntity);
	        } else {
	            // 메타데이터가 이미 존재하면 덮어쓰지 않고 그대로 두기
	            System.out.println("메타데이터가 이미 존재합니다. 추가하지 않습니다.");
	        }
	    }
	    
	    public void saveAnalysisResults(Long postId, String sttResult, double sentimentScore) {
	        saveMetaData(postId, "STT_RESULT", sttResult); // STT 결과
	        saveMetaData(postId, "SENTIMENT_SCORE", String.valueOf(sentimentScore)); // 감성 분석 점수
	    }
	    
	    // 파일 확장자를 가져오는 헬퍼 메서드
	    private String getFileExtension(String fileName) {
	        return fileName.substring(fileName.lastIndexOf('.') + 1);
	    }
}
