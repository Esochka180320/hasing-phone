package com.vilka.hashing_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфігурація Swagger для автоматичної генерації документації API.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Налаштовує OpenAPI (Swagger) для документування REST API.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hashing Service API")
                        .version("1.0")
                        .description("REST API for hashing and retrieving phone numbers"));
    }
}
