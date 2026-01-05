package org.carnavawiky.back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class HealthController {

    // Marcamos la dependencia como opcional para que no de error si no existe el bean
    @Autowired(required = false)
    private BuildProperties buildProperties;

    @GetMapping("/health")
    public String checkHealth() {
        // Verificamos si buildProperties se ha inyectado correctamente
        String version = (buildProperties != null) ? buildProperties.getVersion() : "v1.0.0-dev";
        return "service UP " + version;
    }
}