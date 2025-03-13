package com.ict.vita.controller.file;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ict.vita.service.file.FileService;

@Controller
public class FileController {

    @Autowired
    FileService fileService;

    /**
     * 파일 업로드 처리 후 목록 보여주기
     */
    @PostMapping("upload.do")
    public String UploadFile(@RequestParam("file") MultipartFile file) {
        fileService.UploadFile(file);
        return "redirect:/login.do"; // ✅ 업로드 후 최신 파일 목록으로 리다이렉트
    }

    /**
     * 업로드된 파일 목록을 보여줌
     */
    @GetMapping("login.do")
    public String ShowFile(Model model) {
        List<String> files = fileService.getUploadedFiles();
        System.out.println("[파일 목록 컨트롤러에서 전달]: " + files);  // ✅ 로그 추가
        model.addAttribute("files", files);
        model.addAttribute("username", "kim"); // ✅ username 유지
        return "DH/login";
    }

    /**
     * 파일 다운로드 처리
     */
    @GetMapping(value = "/download/{fileName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> DownloadFile(@PathVariable String fileName) throws IOException {
    	byte[] fileData = fileService.DownloadFile(fileName);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentDisposition(ContentDisposition.attachment().filename(fileName).build());
		return ResponseEntity.ok()
		        .headers(headers)
		        .body(fileData);
    }

    /**
     * 파일 삭제 처리 후 리다이렉트
     */
    @DeleteMapping("delete/{fileName}")
    public ResponseEntity<String> DeleteFile(@PathVariable String fileName) {
        String safeFileName = fileService.SanitizeFileName(fileName);
        
        
        
    	boolean isDeleted = fileService.DeleteFile(safeFileName); // ✅ 성공 여부 반환

        if (isDeleted) {
            return ResponseEntity.ok("✅ 파일 삭제 성공: " + safeFileName);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("⚠️ 삭제할 파일이 존재하지 않음: " + fileName);
        }
    }
}

