package org.carnavawiky.back.controller;

import jakarta.validation.Valid;
import org.carnavawiky.back.dto.AgrupacionRequest;
import org.carnavawiky.back.dto.AgrupacionResponse;
import org.carnavawiky.back.dto.PageResponse; // << IMPORTAR
import org.carnavawiky.back.service.AgrupacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable; // << IMPORTAR
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/agrupaciones")
public class AgrupacionController {

    @Autowired
    private AgrupacionService agrupacionService;

    // =======================================================
    // 1. CREAR (POST)
    // Permite solo a ADMIN crear nuevas agrupaciones.
    // =======================================================
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AgrupacionResponse> crearAgrupacion(
            @Valid @RequestBody AgrupacionRequest request) {

        AgrupacionResponse nuevaAgrupacion = agrupacionService.crearAgrupacion(request);
        return new ResponseEntity<>(nuevaAgrupacion, HttpStatus.CREATED);
    }

    // =======================================================
    // 2. OBTENER TODAS (GET) - CON PAGINACIÓN Y BÚSQUEDA
    // CORREGIDO: Ahora usa Pageable y devuelve PageResponse
    // =======================================================
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PageResponse<AgrupacionResponse>> obtenerTodasAgrupaciones(
                                                                                      Pageable pageable, // Spring inyecta Pageable
                                                                                      @RequestParam(required = false) String search) { // Parámetro de búsqueda

        PageResponse<AgrupacionResponse> agrupacionesPage = agrupacionService.obtenerTodasAgrupaciones(pageable, search);
        return ResponseEntity.ok(agrupacionesPage);
    }

    // =======================================================
    // 3. OBTENER POR ID (GET /ID)
    // Accesible por USER y ADMIN.
    // =======================================================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<AgrupacionResponse> obtenerAgrupacionPorId(@PathVariable Long id) {
        AgrupacionResponse agrupacion = agrupacionService.obtenerAgrupacionPorId(id);
        return ResponseEntity.ok(agrupacion);
    }

    // =======================================================
    // 4. ACTUALIZAR (PUT /ID)
    // Permite solo a ADMIN actualizar.
    // =======================================================
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AgrupacionResponse> actualizarAgrupacion(
            @PathVariable Long id,
            @Valid @RequestBody AgrupacionRequest request) {

        AgrupacionResponse agrupacionActualizada = agrupacionService.actualizarAgrupacion(id, request);
        return ResponseEntity.ok(agrupacionActualizada);
    }

    // =======================================================
    // 5. ELIMINAR (DELETE /ID)
    // Permite solo a ADMIN eliminar.
    // =======================================================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarAgrupacion(@PathVariable Long id) {
        agrupacionService.eliminarAgrupacion(id);
        return ResponseEntity.noContent().build();
    }
}