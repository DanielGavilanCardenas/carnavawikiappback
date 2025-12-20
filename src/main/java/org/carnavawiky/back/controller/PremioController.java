package org.carnavawiky.back.controller;

import jakarta.validation.Valid;
import org.carnavawiky.back.dto.PageResponse;
import org.carnavawiky.back.dto.PremioRequest;
import org.carnavawiky.back.dto.PremioResponse;
import org.carnavawiky.back.service.PremioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/premios")
public class PremioController {

    @Autowired
    private PremioService premioService;

    // =======================================================
    // 1. CREAR (POST) - Solo ADMIN
    // =======================================================
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PremioResponse> crearPremio(
            @Valid @RequestBody PremioRequest request) {

        PremioResponse nuevoPremio = premioService.crearPremio(request);
        return new ResponseEntity<>(nuevoPremio, HttpStatus.CREATED);
    }

    // =======================================================
    // 2. OBTENER TODOS (GET) - Abierto a USER y ADMIN
    // =======================================================
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PageResponse<PremioResponse>> obtenerTodosPremios(
            Pageable pageable,
            @RequestParam(required = false) String search) { // Puede ser año o nombre de agrupación

        PageResponse<PremioResponse> premiosPage = premioService.obtenerTodosPremios(pageable, search);
        return ResponseEntity.ok(premiosPage);
    }

    // =======================================================
    // 3. OBTENER POR ID (GET /ID) - Abierto a USER y ADMIN
    // =======================================================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PremioResponse> obtenerPremioPorId(@PathVariable Long id) {
        PremioResponse premio = premioService.obtenerPremioPorId(id);
        return ResponseEntity.ok(premio);
    }

    // =======================================================
    // 4. ACTUALIZAR (PUT /ID) - Solo ADMIN
    // =======================================================
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PremioResponse> actualizarPremio(
            @PathVariable Long id,
            @Valid @RequestBody PremioRequest request) {

        PremioResponse premioActualizado = premioService.actualizarPremio(id, request);
        return ResponseEntity.ok(premioActualizado);
    }

    // =======================================================
    // 5. ELIMINAR (DELETE /ID) - Solo ADMIN
    // =======================================================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarPremio(@PathVariable Long id) {
        premioService.eliminarPremio(id);
        return ResponseEntity.noContent().build();
    }
}