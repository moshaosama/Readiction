package dev.bakr.readiction_backend.config;

// this file overrides the automatic OpenAPI definition and makes a custom looking for OpenAPI specifiction.

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                              .title("OpenAPI specification - Readiction")
                              .version("1.0")
                              .description("The API documentation for Frontend and Testing")
                              .contact(new Contact()
                                               .name("Mohamed Aboubakr")
                                               .email("bakr.detachment2011@gmail.com")
                                               .url("https://www.linkedin.com/in/moaboubakr2001/"))
                              .license(new License()
                                               .name("Apache 2.0")
                                               .url("https://springdoc.org")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development")
//                        new Server()
//                                .url("https://staging.readiction.com")
//                                .description("Testing and Playing Arena"),
//                        new Server()
//                                .url("https://readiction.com")
//                                .description("Production")
                ))
                // Add a security layer to the specification to let OpenAPI accept JWT token
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components().addSecuritySchemes("bearerAuth", new SecurityScheme()
                        .name("bearerAuth")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                ));
    }
}
