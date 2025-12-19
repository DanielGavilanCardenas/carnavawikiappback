package org.carnavawiky.back.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = OpenApiConfig.class)
class OpenApiConfigTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private OpenApiConfig openApiConfig;

    @Test
    @DisplayName("Debe cargar el Bean de OpenAPI en el contexto de Spring")
    void testOpenApiBeanExists() {
        OpenAPI openAPI = context.getBean(OpenAPI.class);
        assertNotNull(openAPI, "El Bean de OpenAPI debería estar definido");
    }

    @Test
    @DisplayName("Debe contener la configuración de seguridad JWT Bearer")
    void testOpenApiSecurityConfiguration() {
        OpenAPI openAPI = openApiConfig.customOpenAPI();

        // 1. Verificar información básica
        assertEquals("Carnavawiky REST API", openAPI.getInfo().getTitle());
        assertEquals("v1.0", openAPI.getInfo().getVersion());

        // 2. Verificar que existe el esquema de seguridad BearerAuth
        SecurityScheme securityScheme = openAPI.getComponents().getSecuritySchemes().get("BearerAuth");
        assertNotNull(securityScheme, "Debe existir el esquema 'BearerAuth'");
        assertEquals(SecurityScheme.Type.HTTP, securityScheme.getType());
        assertEquals("bearer", securityScheme.getScheme());
        assertEquals("JWT", securityScheme.getBearerFormat());

        // 3. Verificar que la seguridad se aplica de forma global
        boolean hasGlobalSecurity = openAPI.getSecurity().stream()
                .anyMatch(s -> s.containsKey("BearerAuth"));
        assertTrue(hasGlobalSecurity, "La seguridad debe estar aplicada globalmente");
    }
}