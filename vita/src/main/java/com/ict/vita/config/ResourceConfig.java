package com.ict.vita.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
//[정적 리소스 설정]
public class ResourceConfig implements WebMvcConfigurer {
	
	@Value("${file.upload-dir}") //application.yml에 설정
    private String uploadDir; //업로드 디렉터리

	//정적 리소스 설정 추가 -> url로 접근 가능
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/api/files/upload/**") // 요청 URL 경로
				.addResourceLocations("file:///" + uploadDir); // 실제 파일이 저장된 물리 경로에서 파일을 서빙
	}
}
