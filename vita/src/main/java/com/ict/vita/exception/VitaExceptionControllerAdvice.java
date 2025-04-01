package com.ict.vita.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

//<<모든 컨트롤러의 예외 처리시>>
@RestControllerAdvice
public class VitaExceptionControllerAdvice {

	//파일 업로드 용량 초과시
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public String maxUploadSizeError(Exception e) {
		e.printStackTrace();
		return "파일 업로드 용량 초과";
	}
}
