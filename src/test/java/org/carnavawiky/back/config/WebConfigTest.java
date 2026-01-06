package org.carnavawiky.back.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebConfigTest {

    @Mock
    private ResourceHandlerRegistry registry;

    @Mock
    private ResourceHandlerRegistration registration;

    @InjectMocks
    private WebConfig webConfig;

    @Test
    @DisplayName("Debe registrar el manejador de recursos para las imágenes correctamente")
    void testAddResourceHandlers() {
        // ARRANGE
        // Inyectamos el valor de la propiedad @Value manualmente
        ReflectionTestUtils.setField(webConfig, "uploadLocation", "uploads");

        // El método addResourceHandler devuelve un objeto Registration que debemos mockear
        // para que las llamadas encadenadas (fluent API) no den NullPointerException
        when(registry.addResourceHandler("/api/imagenes/**")).thenReturn(registration);
        when(registration.addResourceLocations(anyString())).thenReturn(registration);

        // ACT
        webConfig.addResourceHandlers(registry);

        // ASSERT
        // 1. Verificamos que se intenta registrar la ruta URL correcta
        verify(registry, times(1)).addResourceHandler("/api/imagenes/**");

        // 2. Verificamos que se le asigna una ubicación física (el path convertido a URI)
        // Como "uploads" se convierte a URI absoluta, verificamos que contenga "uploads"
        // Usamos cast explícito a String para evitar ambigüedad en Mockito
        verify(registration, times(1)).addResourceLocations(argThat((String uri) -> uri.contains("uploads")));
    }
}