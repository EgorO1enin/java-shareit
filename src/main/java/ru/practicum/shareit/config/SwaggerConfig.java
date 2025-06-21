package ru.practicum.shareit.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Java shareit API",
                version = "0.0.1",
                description = "API documentation for Service"
        )
)
public class SwaggerConfig {
    // Дополнительные настройки можно добавить здесь
}