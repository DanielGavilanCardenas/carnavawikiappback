package org.carnavawiky.back.controller;

import jakarta.validation.Valid;
import org.carnavawiky.back.dto.LocalidadRequest;
import org.carnavawiky.back.dto.LocalidadResponse;
import org.carnavawiky.back.dto.PageResponse;
import org.carnavawiky.back.service.LocalidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/localidades")
public class LocalidadController {

    @Autowired
    private LocalidadService localidadService;

    // =======================================================
    // 1. CREAR (POST) - Solo ADMIN
    // =======================================================
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LocalidadResponse> crearLocalidad(
            @Valid @RequestBody LocalidadRequest request) {

        LocalidadResponse nuevaLocalidad = localidadService.crearLocalidad(request);
        return new ResponseEntity<>(nuevaLocalidad, HttpStatus.CREATED);
    }

    // =======================================================
    // 2. OBTENER TODAS (GET) - Abierto a USER y ADMIN
    // =======================================================
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PageResponse<LocalidadResponse>> obtenerTodasLocalidades(
            Pageable pageable, // Paginación
            @RequestParam(required = false) String search) { // Búsqueda opcional

        PageResponse<LocalidadResponse> localidadesPage = localidadService.obtenerTodasLocalidades(pageable, search);
        return ResponseEntity.ok(localidadesPage);
    }

    // =======================================================
    // 3. OBTENER POR ID (GET /ID) - Abierto a USER y ADMIN
    // =======================================================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<LocalidadResponse> obtenerLocalidadPorId(@PathVariable Long id) {
        LocalidadResponse localidad = localidadService.obtenerLocalidadPorId(id);
        return ResponseEntity.ok(localidad);
    }

    // =======================================================
    // 4. ACTUALIZAR (PUT /ID) - Solo ADMIN
    // =======================================================
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LocalidadResponse> actualizarLocalidad(
            @PathVariable Long id,
            @Valid @RequestBody LocalidadRequest request) {

        LocalidadResponse localidadActualizada = localidadService.actualizarLocalidad(id, request);
        return ResponseEntity.ok(localidadActualizada);
    }

    // =======================================================
    // 5. ELIMINAR (DELETE /ID) - Solo ADMIN
    // =======================================================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarLocalidad(@PathVariable Long id) {
        localidadService.eliminarLocalidad(id);
        return ResponseEntity.noContent().build();
    }
}