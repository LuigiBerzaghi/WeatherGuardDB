package com.fiap.WeatherGuard.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @io.swagger.v3.oas.annotations.info.Info(
        title = "WeatherGuard API",
        version = "1.0",
        description = "API REST para alertas climáticos baseados em eventos extremos"
    )
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new io.swagger.v3.oas.models.info.Info()
                .title("WeatherGuard API")
                .version("1.0")
                .description("API REST para alertas climáticos baseados em eventos extremos")
                .contact(new Contact()
                    .name("Luigi Berzaghi")
                    .email("luigisespedes@gmail.com")
                    .url("https://github.com/LuigiBerzaghi")
                )
            );
    }
}
