package com.ict.vita.exception;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.ict.vita.service.member.MemberService;
import com.ict.vita.service.postcategoryrelationships.PostCategoryRelationshipsService;
import com.ict.vita.service.posts.PostsService;
import com.ict.vita.service.terms.TermsService;

import lombok.RequiredArgsConstructor;

//<<모든 컨트롤러의 예외 처리시>>
@RestControllerAdvice
public class VitaExceptionControllerAdvice {
	
	public VitaExceptionControllerAdvice() {
        // 기본 생성자 (필수 X, 하지만 클래스 인스턴스화를 명확히 할 수 있음)
    }

	//파일 업로드 용량 초과시
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public String maxUploadSizeError(Exception e) {
		e.printStackTrace();
		return "파일 업로드 용량 초과";
	}
}
