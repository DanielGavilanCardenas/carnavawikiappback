package org.carnavawiky.back.controller;

import jakarta.validation.Valid;
import org.carnavawiky.back.dto.EdicionRequest;
import org.carnavawiky.back.dto.EdicionResponse;
import org.carnavawiky.back.dto.PageResponse;
import org.carnavawiky.back.service.EdicionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ediciones")
public class EdicionController {

    @Autowired
    private EdicionService edicionService;

    // =======================================================
    // 1. CREAR (POST) - Solo ADMIN
    // =======================================================
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EdicionResponse> crearEdicion(
            @Valid @RequestBody EdicionRequest request) {

        EdicionResponse nuevaEdicion = edicionService.crearEdicion(request);
        return new ResponseEntity<>(nuevaEdicion, HttpStatus.CREATED);
    }

    // =======================================================
    // 2. OBTENER TODOS (GET) - Abierto a USER y ADMIN
    // =======================================================
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PageResponse<EdicionResponse>> obtenerTodasEdiciones(
            Pageable pageable,
            @RequestParam(required = false) String search) {

        PageResponse<EdicionResponse> edicionesPage = edicionService.obtenerTodasEdiciones(pageable, search);
        return ResponseEntity.ok(edicionesPage);
    }

    // =======================================================
    // 3. OBTENER POR ID (GET /ID) - Abierto a USER y ADMIN
    // =======================================================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<EdicionResponse> obtenerEdicionPorId(@PathVariable Long id) {
        EdicionResponse edicion = edicionService.obtenerEdicionPorId(id);
        return ResponseEntity.ok(edicion);
    }

    // =======================================================
    // 4. ACTUALIZAR (PUT /ID) - Solo ADMIN
    // =======================================================
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EdicionResponse> actualizarEdicion(
            @PathVariable Long id,
            @Valid @RequestBody EdicionRequest request) {

        EdicionResponse edicionActualizada = edicionService.actualizarEdicion(id, request);
        return ResponseEntity.ok(edicionActualizada);
    }

    // =======================================================
    // 5. ELIMINAR (DELETE /ID) - Solo ADMIN
    // =======================================================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarEdicion(@PathVariable Long id) {
        edicionService.eliminarEdicion(id);
        return ResponseEntity.noContent().build();
    }
}