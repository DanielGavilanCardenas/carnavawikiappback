package org.carnavawiky.back.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/all")
    public String allAccess() {
        return "Contenido Público.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String userAccess() {
        // --- CÓDIGO TEMPORAL PARA PRUEBA DE ERRORES ---

        // 1. **Prueba ResourceNotFoundException (Simula un 404)**
        // Descomenta la siguiente línea para probar el 404:
        //throw new ResourceNotFoundException("Agrupacion", "id", 999L);

        // 2. **Prueba BadRequestException (Simula un 400)**
        // Descomenta la siguiente línea para probar el 400:
        //throw new BadRequestException("El nombre de usuario ya está en uso.");

        // 3. **Prueba Exception Genérica (Simula un 500)**
        // Descomenta la siguiente línea para probar el 500:
        //throw new RuntimeException("Error inesperado en la base de datos.");

        // ---------------------------------------------
        return "Contenido de Usuario.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Contenido de Administrador.";
    }
}