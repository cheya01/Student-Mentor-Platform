package com.skill_mentor.root.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Student Mentor Platform API")
                        .version("1.0.0")
                        .description("API documentation for the Student Mentor Platform")
                        .contact(new Contact()
                                .name("Support Team")
                                .email("support@bluebrand.org")
                                .url("https://bluebrand.org"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org"))
                );
    }
}

