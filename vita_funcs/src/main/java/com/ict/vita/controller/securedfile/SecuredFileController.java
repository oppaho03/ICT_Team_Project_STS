
package com.ict.vita.controller.securedfile;

import com.ict.vita.service.securedfile.SecuredFileDTO;
import com.ict.vita.service.securedfile.SecuredFileService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class SecuredFileController {

    private final SecuredFileService service;

    public SecuredFileController(SecuredFileService service) {
        this.service = service;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("postId") Long postId,
            @RequestParam("encStatus") Integer encStatus) {

        try {
            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException("업로드된 파일이 없습니다.");
            }

            SecuredFileDTO responseDTO = service.saveEncryptedFile(file, postId, encStatus);

            System.out.println("[컨트롤러] 원본 파일명: " + responseDTO.getFileName());
            System.out.println("[컨트롤러] 파일 확장자: " + responseDTO.getFileExt());
            System.out.println("[컨트롤러] After Save File: " + responseDTO.getFileUrl());

            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("파일 업로드 실패: " + e.getMessage());
        }
    }
}
