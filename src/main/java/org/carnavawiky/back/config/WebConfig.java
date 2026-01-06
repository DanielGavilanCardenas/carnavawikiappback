package org.carnavawiky.back.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload.location}")
    private String uploadLocation;

    /**
     * Configura un manejador de recursos estáticos para exponer la carpeta de imágenes
     * a través de una URL pública.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Convertimos la ruta del properties a una URI válida para Spring (file:/...)
        String locationUri = Paths.get(uploadLocation).toUri().toString();

        // Mapea las peticiones que entren por /api/imagenes/ al directorio físico
        registry.addResourceHandler("/api/imagenes/**")
                .addResourceLocations(locationUri);

        // Es buena práctica llamar al super si no hay más configuraciones
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}