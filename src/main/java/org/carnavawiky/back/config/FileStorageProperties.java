package org.carnavawiky.back.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "file.upload")
public class FileStorageProperties {

    /**
     * Directorio base donde se almacenarán los ficheros subidos.
     */
    private String location;

}