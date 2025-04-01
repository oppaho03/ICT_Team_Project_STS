package com.ict.vita.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
//src/main/resources/config/vita.properties에서 속성파일 읽겠다
@PropertySource({"classpath:config/vita.properties"})
public class VitaConfig {
	//PropertySourcesPlaceholderConfigurer는 .properties 파일만 지원
	@Bean
	PropertySourcesPlaceholderConfigurer configurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
