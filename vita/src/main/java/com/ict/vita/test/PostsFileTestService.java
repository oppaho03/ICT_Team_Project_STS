package com.ict.vita.test;

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
    
    //※ 상수는 따로 파일로 하는게 좋을 것 같아요!!! 
    // 지금 url은 src/main/resource 아래 config/vita.properties 에 URL주소 넣으려고 하고있으니 거기에 넣어주세요!! 
    private static final String STT_API_URL = "http://127.0.0.1:8000/api/files/upload_result";  // STT API URL
    private static final String SENTIMENT_API_URL = "http://127.0.0.1:8000/uploadfiles/upload_result";  // Sentiment API URL
    private static final String SPRINGBOOT_API_URL = "http://localhost:8080/api/files/upload_result"; // 스프링부트에서 결과 받는 API URL
    
    // 파일을 업로드하고 APP_POSTS에 저장하는 메서드
    /*
    public void savePostFile(MultipartFile file, Long postId) throws Exception {
     // 수정된 부분
     PostsEntity postsEntity = postsRepository.findById(postId)
         .orElseThrow(() -> new Exception("Post not found with id: " + postId));
        String fileName = file.getOriginalFilename();
        // PostsEntity 객체를 가져오고, 해당 postId에 해당하는 게시물 엔티티를 설정합니다.
        PostsEntity postsEntity2 = postsRepository.findById(postId).orElseThrow(() -> new Exception("Post not found with id: " + postId));
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
    } */
    
    //APP_POSTS 테이블에 저장
    public PostsDto savePostFile(PostsDto post) {
    	PostsEntity savedPost = postsRepository.save( post.toEntity() );
    	return PostsDto.toDto(savedPost);
    }
    
    
    // 파일 이름을 AES256 암호화 후 저장하는 메서드
    /*
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
    } */
    
    // 메타 데이터를 저장하는 메서드
    /*
    private void saveMetaData(Long postId, String metaKey, String metaValue) {
        PostMetaEntity postMetaEntity = new PostMetaEntity();
        // postsRepository.findById(postId)로 PostsEntity를 찾고, 없으면 RuntimeException을 던집니다.
        PostsEntity postsEntity = postsRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
        postMetaEntity.setPostsEntity(postsEntity);
        postMetaEntity.setMetaKey(metaKey);
        postMetaEntity.setMetaValue(metaValue);
        postMetaRepository.save(postMetaEntity);  // 메타 데이터 저장
    } */
    
    /*
    public void saveAnalysisResults(Long postId, String sttResult, double sentimentScore) {
        saveMetaData(postId, "STT_RESULT", sttResult); // STT 결과
        saveMetaData(postId, "SENTIMENT_SCORE", String.valueOf(sentimentScore)); // 감성 분석 점수
    } */
    
    // 파일 확장자를 가져오는 헬퍼 메서드
    /*
    @Transactional(readOnly = true)
    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    } */
}
