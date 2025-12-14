package org.carnavawiky.back.controller;

import jakarta.validation.Valid;
import org.carnavawiky.back.dto.ComponenteRequest;
import org.carnavawiky.back.dto.ComponenteResponse;
import org.carnavawiky.back.dto.PageResponse;
import org.carnavawiky.back.service.ComponenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/componentes")
public class ComponenteController {

    @Autowired
    private ComponenteService componenteService;

    // =======================================================
    // 1. CREAR (POST) - Solo ADMIN
    // =======================================================
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ComponenteResponse> crearComponente(
            @Valid @RequestBody ComponenteRequest request) {

        ComponenteResponse nuevoComponente = componenteService.crearComponente(request);
        return new ResponseEntity<>(nuevoComponente, HttpStatus.CREATED);
    }

    // =======================================================
    // 2. OBTENER TODOS (GET) - Abierto a USER y ADMIN
    // =======================================================
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PageResponse<ComponenteResponse>> obtenerTodosComponentes(
            Pageable pageable,
            @RequestParam(required = false) String search) { // Búsqueda por Persona o Agrupación

        PageResponse<ComponenteResponse> componentesPage = componenteService.obtenerTodosComponentes(pageable, search);
        return ResponseEntity.ok(componentesPage);
    }

    // =======================================================
    // 3. OBTENER POR ID (GET /ID) - Abierto a USER y ADMIN
    // =======================================================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ComponenteResponse> obtenerComponentePorId(@PathVariable Long id) {
        ComponenteResponse componente = componenteService.obtenerComponentePorId(id);
        return ResponseEntity.ok(componente);
    }

    // =======================================================
    // 4. ACTUALIZAR (PUT /ID) - Solo ADMIN
    // =======================================================
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ComponenteResponse> actualizarComponente(
            @PathVariable Long id,
            @Valid @RequestBody ComponenteRequest request) {

        ComponenteResponse componenteActualizado = componenteService.actualizarComponente(id, request);
        return ResponseEntity.ok(componenteActualizado);
    }

    // =======================================================
    // 5. ELIMINAR (DELETE /ID) - Solo ADMIN
    // =======================================================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarComponente(@PathVariable Long id) {
        componenteService.eliminarComponente(id);
        return ResponseEntity.noContent().build();
    }
}