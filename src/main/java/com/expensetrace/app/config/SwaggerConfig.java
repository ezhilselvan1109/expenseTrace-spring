package com.expensetrace.app.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI expenseTraceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ExpenseTrace API")
                        .description("Personal finance management API documentation")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Ezhil selvan P")
                                .email("support@expensetrace.com")
                                .url("https://expensetrace.vercel.app")))
                .externalDocs(new ExternalDocumentation()
                        .description("GitHub Repository")
                        .url("https://github.com/ezhilselvan1109/expenseTrace-spring"))
                .components(new Components()
                        .addSecuritySchemes("cookieAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.COOKIE)
                                        .name("jwt")))
                .addSecurityItem(new SecurityRequirement().addList("cookieAuth"));
    }
}