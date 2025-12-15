package org.carnavawiky.back.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "file.upload")
public class FileStorageProperties {

    /**
     * Directorio base donde se almacenarán los ficheros subidos.
     * Ejemplo: /var/data/carnavawiky/images
     */
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}