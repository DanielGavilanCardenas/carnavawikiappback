package org.carnavawiky.back.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class HealthController {


    
    private final org.springframework.boot.info.BuildProperties buildProperties;

    public HealthController(org.springframework.boot.info.BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @GetMapping("/health")
    public String checkHealth() {
        return "service UP " + buildProperties.getVersion();
    }
}
