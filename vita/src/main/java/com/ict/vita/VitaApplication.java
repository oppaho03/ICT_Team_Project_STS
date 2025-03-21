package com.ict.vita;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;

@SpringBootApplication(exclude = SecurityFilterAutoConfiguration.class)
public class VitaApplication {

	public static void main(String[] args) {
		SpringApplication.run(VitaApplication.class, args);
	}

}
