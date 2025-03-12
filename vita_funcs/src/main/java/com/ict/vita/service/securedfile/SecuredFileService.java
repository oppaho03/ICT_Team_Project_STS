package com.ict.vita.service.securedfile;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ict.vita.service.securedfile.SecuredFileDTO;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class SecuredFileService {

    private static final String UPLOAD_DIR = System.getProperty("file.upload-dir", "C:/server_files/");

    public SecuredFileDTO saveEncryptedFile(MultipartFile file, Long postId, Integer encStatus) throws Exception {
        if (postId == null || postId <= 0) {
            throw new IllegalArgumentException("postId가 NULL이거나 유효하지 않습니다.");
        }

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("업로드된 파일이 없습니다.");
        }

        String originalFileName = file.getOriginalFilename();
        System.out.println("[서비스] 원본 파일명: " + originalFileName);

        String fileExt = originalFileName.substring(originalFileName.lastIndexOf('.'));
        System.out.println("[서비스] 파일 확장자: " + fileExt);

        String randomFileName = UUID.randomUUID().toString().replace("-", "") + fileExt;
        
        // 폴더 생성 로직 추가 (없으면 폴더 생성)
        Path uploadDir = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);  // 디렉토리 생성
            System.out.println("[서비스] 폴더 생성: " + uploadDir.toString());
        }
        
        
        Path filePath = Paths.get(UPLOAD_DIR, randomFileName);
        Files.write(filePath, file.getBytes());

        System.out.println("[서비스] After Save File: " + filePath.toString());

        return new SecuredFileDTO(postId, randomFileName, fileExt, filePath.toString(), encStatus);
    }
}
