package com.ict.vita;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.ict.vita")  // 예외 핸들러가 있는 패키지를 포함해야 함
@SpringBootApplication(exclude = SecurityFilterAutoConfiguration.class)
public class VitaApplication {

	public static void main(String[] args) {
		SpringApplication.run(VitaApplication.class, args);
	}

}
