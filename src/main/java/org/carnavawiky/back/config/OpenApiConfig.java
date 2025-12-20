package org.carnavawiky.back.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SCHEME_NAME = "BearerAuth";

    @Autowired(required = false) // Opcional por si no se genera el build-info en tests
    private BuildProperties buildProperties;

    @Bean
    public OpenAPI customOpenAPI() {
        // Si buildProperties está disponible, usamos su versión. Si no, un valor por defecto.
        String version = (buildProperties != null) ? buildProperties.getVersion() : "dev-snapshot";

        return new OpenAPI()
                .info(new Info()
                        .title("Carnavawiky REST API")
                        .version(version)
                        .description("Documentación de la API de Carnavawiky, incluyendo autenticación JWT."))

                // 1. Define el Componente de Seguridad (el esquema JWT Bearer)
                .components(new Components()
                        .addSecuritySchemes(SCHEME_NAME,
                                new SecurityScheme()
                                        .name("Authorization") // Nombre del header
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)
                                        .description("Ingrese el token JWT con el prefijo 'Bearer '.")))

                // 2. Aplica la seguridad a TODAS las operaciones (a menos que se especifique lo contrario)
                .addSecurityItem(new SecurityRequirement().addList(SCHEME_NAME));
    }
}