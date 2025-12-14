package org.carnavawiky.back.controller;

import jakarta.validation.Valid;
import org.carnavawiky.back.dto.PageResponse;
import org.carnavawiky.back.dto.PersonaRequest;
import org.carnavawiky.back.dto.PersonaResponse;
import org.carnavawiky.back.service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {

    @Autowired
    private PersonaService personaService;

    // =======================================================
    // 1. CREAR (POST) - Solo ADMIN
    // =======================================================
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PersonaResponse> crearPersona(
            @Valid @RequestBody PersonaRequest request) {

        PersonaResponse nuevaPersona = personaService.crearPersona(request);
        return new ResponseEntity<>(nuevaPersona, HttpStatus.CREATED);
    }

    // =======================================================
    // 2. OBTENER TODOS (GET) - Abierto a USER y ADMIN
    // =======================================================
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PageResponse<PersonaResponse>> obtenerTodasPersonas(
            Pageable pageable,
            @RequestParam(required = false) String search) { // Búsqueda por nombre real o apodo

        PageResponse<PersonaResponse> personasPage = personaService.obtenerTodasPersonas(pageable, search);
        return ResponseEntity.ok(personasPage);
    }

    // =======================================================
    // 3. OBTENER POR ID (GET /ID) - Abierto a USER y ADMIN
    // =======================================================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PersonaResponse> obtenerPersonaPorId(@PathVariable Long id) {
        PersonaResponse persona = personaService.obtenerPersonaPorId(id);
        return ResponseEntity.ok(persona);
    }

    // =======================================================
    // 4. ACTUALIZAR (PUT /ID) - Solo ADMIN
    // =======================================================
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PersonaResponse> actualizarPersona(
            @PathVariable Long id,
            @Valid @RequestBody PersonaRequest request) {

        PersonaResponse personaActualizada = personaService.actualizarPersona(id, request);
        return ResponseEntity.ok(personaActualizada);
    }

    // =======================================================
    // 5. ELIMINAR (DELETE /ID) - Solo ADMIN
    // =======================================================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarPersona(@PathVariable Long id) {
        personaService.eliminarPersona(id);
        return ResponseEntity.noContent().build();
    }
}