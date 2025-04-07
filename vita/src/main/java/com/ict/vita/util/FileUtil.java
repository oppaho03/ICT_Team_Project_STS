package com.ict.vita.util;

import java.io.File;

public class FileUtil {
	/**
	 * [중복되지 않는 파일명 얻기] - 중복파일이 있을 경우 인덱싱 번호를 추가해서 새로운 파일명을 반환
	 * @param path 파일이 저장된 서버의 물리적 경로
	 * @param fileName 업로드한 파일명
	 * @return
	 */
	public static String getNewFileName(String path, String fileName) {
		File file = new File(path+File.separator+fileName);
		//<파일명이 존재하지 않을때>
		if(!file.exists()) {
			return fileName;
		}
		
		//<파일명이 존재하는 경우>
		//ex) 사진_5.jpg
		int dotIndex = fileName.lastIndexOf(".");
		String extension = fileName.substring( dotIndex + 1 ).trim(); //확장자 ex) jpg
		String fileNameExcludeExt = fileName.substring(0,dotIndex).trim(); // .확장자 앞부분 ex) 사진_5
		
		String newFileName;
		
		while (true) {
			newFileName = "";
			if (fileNameExcludeExt.indexOf("_") != -1) {// 파일명에 _가 포함됨
				String[] arrFiles = fileNameExcludeExt.split("_");
				String lastName = arrFiles[arrFiles.length - 1];
				try {
					int index = Integer.parseInt(lastName);
					for (int i = 0; i < arrFiles.length; i++) {
						if (i != arrFiles.length - 1)
							newFileName += arrFiles[i] + "_";
						else
							newFileName += String.valueOf(index + 1);
					}
					newFileName += "." + extension;
				} catch (Exception e) {
					newFileName += fileNameExcludeExt + "_1." + extension;
				}
			} else {// _가 없음
				newFileName += fileNameExcludeExt + "_1." + extension;
			}
			File f = new File(path + File.separator + newFileName);
			if (f.exists()) {
				fileNameExcludeExt = newFileName.substring(0, newFileName.lastIndexOf("."));
				continue;
			} else
				break;
		} //////////// while

		return newFileName;
		
	}
	
}
