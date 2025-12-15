package org.carnavawiky.back.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final FileStorageProperties fileStorageProperties;

    public WebConfig(FileStorageProperties fileStorageProperties) {
        this.fileStorageProperties = fileStorageProperties;
    }

    /**
     * Configura un manejador de recursos estáticos para exponer la carpeta de imágenes
     * a través de una URL pública.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // La ruta "/api/imagenes/ficheros/**" es la URL que usamos en el Controller y Service.
        // Mapea esa URL al directorio físico 'file.upload.location'.
        String locationUri = Paths.get(fileStorageProperties.getLocation()).toUri().toString();

        registry.addResourceHandler("/api/imagenes/ficheros/**")
                .addResourceLocations(locationUri);

        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}