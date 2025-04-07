package com.ict.vita.test.hjh;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import com.ict.vita.util.Commons;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//[파일 업로드용 DTO]
public class FileUploadDto {
	private MultipartFile file;
	
//	private String status;
//	private String password;
//	
//	private LocalDateTime createdAt;
//	private LocalDateTime modifiedAt;
//	
//	private String commentStatus;
//	private int commentCount;
}
