package com.ict.vita.util.config;

import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
  info = @Info(title = "API", description = "VITA RESTfull API Document (Spring)", version = "V1.0")
)
@Configuration 
public class SwaggerConfig {
  
}
