package com.popzii.app.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "SuperApp API",
        version = "v1",
        description = "Modular monolith backend for SuperApp"
    )
)
public class OpenApiConfig {
}
