package com.easy.interviewapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI interviewOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Interview API")
                        .description("用户查询接口（SQLite）")
                        .version("v1")
                        .contact(new Contact().name("interview-api"))
                        .license(new License().name("Apache 2.0")));
    }
}
