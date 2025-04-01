package com.ict.vita.util;

import java.io.File;

public class FileUtil {
	/**
	 * [중복되지 않는 파일명 얻기]
	 * @param path 파일이 저장된 서버의 물리적 경로
	 * @param fileName 업로드한 파일명
	 * @return
	 */
	public static String getNewFileName(String path, String fileName) {
		File file = new File(path+File.separator+fileName);
		//파일명이 존재하지 않을때
		if(!file.exists())
			return fileName;
		
		//파일명이 존재하는 경우	
		String extension = fileName.substring( fileName.lastIndexOf(".")+1 ).trim(); //확장자
		String fileNameExcludeExt = fileName.substring(0,fileName.lastIndexOf(".")).trim(); //확장자를 앞부분
		
		//test.png -> test_1.png -> test_2.png -> .... 형식으로 
		String newFileName; //새 파일명
		
		
		
		
		return null;
		
	}
}
