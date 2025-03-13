package com.ict.vita.util;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUtill {

    private static final String UPLOAD_DIR = "uploads/";

    /**
     * 파일명을 안전하게 변환하는 메서드 (특수문자 및 공백 처리)
     */
    public String SanitizeFileName(String fileName) {	 	
    	return fileName.replaceAll("[^a-zA-Z0-9가-힣.]", "_");  
    }
    
    
    
    /**
     * 파일을 저장하는 메서드
     * @param file 업로드할 파일
     * @return 저장된 파일명 (경로가 아닌 순수 파일명)
     * @throws IOException 파일 저장 실패 시 예외 발생
     */
    public String SaveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }

        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String safeFileName = SanitizeFileName(file.getOriginalFilename());     
        String filePath = Paths.get(UPLOAD_DIR,safeFileName).normalize().toString().replace("\\", "/");
        Files.write(Paths.get(filePath), file.getBytes());
        
        System.out.println(" 파일 저장 완료: " + filePath);
        return safeFileName;
    }
    /**
     * file 리스트를 반환하는 메소드
     * @return 파일목록 리스트
     */
    public List<String> GetFileList(){
    	File folder = new File(UPLOAD_DIR);
    	
    	if(!folder.exists()) {
    		folder.mkdirs();
    		return List.of();
    	}
    	File[] files = folder.listFiles();
    	if(files == null) {
    		return List.of();
    	}
    	List<String> fileList = Arrays.stream(files)
    			.filter(File::isFile)
    			.map(File::getName)
    			.collect(Collectors.toList());
    	
    	return fileList;
    }


    /**
     * 파일을 읽어서 byte[]로 반환하는 메서드 (파일 다운로드 기능)
     * @param fileName 다운로드할 파일 이름
     * @return 파일 데이터 (byte[])
     * @throws IOException 파일이 존재하지 않거나 읽기 실패 시 예외 발생
     */
    public byte[] GetFileAsBytes(String fileName) throws IOException {
        String filePath = Paths.get(UPLOAD_DIR, fileName).toString();
        System.out.println("파일 다운로드 요청: " + filePath);
        return Files.readAllBytes(Paths.get(filePath));
    }

    /**
     * 파일을 삭제하는 메서드
     * @param fileName 삭제할 파일 이름
     * @return 삭제 성공 여부 (true = 삭제됨, false = 파일 없음)
     */
    public boolean DeleteFile(String fileName) {
        String safeFileName = SanitizeFileName(fileName);
        String filePath = Paths.get(UPLOAD_DIR,safeFileName).toString();
        System.out.println(" 파일 삭제 요청: " + safeFileName);

        File file = new File(filePath);
        if (file.exists()) {
            boolean result = file.delete();
            if (result) {
                System.out.println("파일 삭제 성공: " + safeFileName);
            } else {
                System.out.println("파일 삭제 실패: " + safeFileName);
            }
            return result;
        } else {
            System.out.println("삭제 실패: 파일이 존재하지 않음: " + safeFileName);
            return false;
        }
    }
}
