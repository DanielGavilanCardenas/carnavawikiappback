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
        // Forzamos que la ruta empiece por file:/ y termine en /
        String location = uploadLocation.replace("\\", "/");
        if (!location.endsWith("/")) {
            location += "/";
        }
        if (!location.startsWith("file:")) {
            location = "file:/" + location;
        }

        registry.addResourceHandler("/recursos/imagenes/**")
                .addResourceLocations(location);

        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}