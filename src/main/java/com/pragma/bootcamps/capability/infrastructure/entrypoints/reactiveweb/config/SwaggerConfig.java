package com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Capability Microservice",
                description = "Capabilities Management API",
                version = "1.0.0"
        )
)
public class SwaggerConfig {
}
