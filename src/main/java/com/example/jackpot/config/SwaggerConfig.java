package com.example.jackpot.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class SwaggerConfig {
    private static final String API_KEY = "api-key";

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info().title("Jackpot Betting Service API")
                        .description("Provides endpoints for placing and managing bets on jackpots.")
                        .version("1.0.0"))
                .components(new Components()
                        .addSecuritySchemes(API_KEY,
                                new SecurityScheme()
                                        .name("X-Api-Key")
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in((SecurityScheme.In.HEADER))))
                .security(Collections.singletonList(new SecurityRequirement().addList(API_KEY)));
    }


}
