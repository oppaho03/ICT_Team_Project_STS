package com.ict.vita.service.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ict.vita.util.FileUtill;



@Service
public class FileService {
	@Autowired
	private FileUtill fileUtill;
	
    /**
     *  파일을 저장하고 저장된 경로를 반환하는 메서드
     */
	public String UploadFile(MultipartFile file) {
		try {
			return fileUtill.SaveFile(file);
		} 
		catch (IOException e) {
			throw new RuntimeException("파일 업로드 중 오류 발생",e);			
		}
	}
    /**
     *  업로드된 파일 목록 조회
     */
    public List<String> getUploadedFiles() {
    	return fileUtill.GetFileList();
    }
    /**
     * 파일 다운로드 기능
     * 파일 데이터를 바이트 배열(byte[])로 반환하여 다운로드 가능하게 함
     */
	public byte[] DownloadFile(String fileName) {
		try {
		return fileUtill.GetFileAsBytes(fileName);
		}
		catch(IOException e) {
			throw new RuntimeException("파일 다운로드 중 오류 발생",e);
		}
	}
    /**
     * 파일 삭제 기능
     * @return 
     */
	public boolean DeleteFile(String fileName) {
		String safeFileName = fileUtill.SanitizeFileName(fileName);
		return fileUtill.DeleteFile(safeFileName);
	}
    public String SanitizeFileName(String fileName) {
        return fileUtill.SanitizeFileName(fileName);
    }

}
