package org.carnavawiky.back.controller;

import jakarta.validation.Valid;
import org.carnavawiky.back.dto.ConcursoRequest;
import org.carnavawiky.back.dto.ConcursoResponse;
import org.carnavawiky.back.dto.PageResponse;
import org.carnavawiky.back.service.ConcursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/concursos")
public class ConcursoController {

    @Autowired
    private ConcursoService concursoService;

    // =======================================================
    // 1. CREAR (POST) - Solo ADMIN
    // =======================================================
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ConcursoResponse> crearConcurso(
            @Valid @RequestBody ConcursoRequest request) {

        ConcursoResponse nuevoConcurso = concursoService.crearConcurso(request);
        return new ResponseEntity<>(nuevoConcurso, HttpStatus.CREATED);
    }

    // =======================================================
    // 2. OBTENER TODOS (GET) - Abierto a USER y ADMIN
    // =======================================================
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PageResponse<ConcursoResponse>> obtenerTodosConcursos(
            Pageable pageable,
            @RequestParam(required = false) String search) {

        PageResponse<ConcursoResponse> concursosPage = concursoService.obtenerTodosConcursos(pageable, search);
        return ResponseEntity.ok(concursosPage);
    }

    // =======================================================
    // 3. OBTENER POR ID (GET /ID) - Abierto a USER y ADMIN
    // =======================================================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ConcursoResponse> obtenerConcursoPorId(@PathVariable Long id) {
        ConcursoResponse concurso = concursoService.obtenerConcursoPorId(id);
        return ResponseEntity.ok(concurso);
    }

    // =======================================================
    // 4. ACTUALIZAR (PUT /ID) - Solo ADMIN
    // =======================================================
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ConcursoResponse> actualizarConcurso(
            @PathVariable Long id,
            @Valid @RequestBody ConcursoRequest request) {

        ConcursoResponse concursoActualizado = concursoService.actualizarConcurso(id, request);
        return ResponseEntity.ok(concursoActualizado);
    }

    // =======================================================
    // 5. ELIMINAR (DELETE /ID) - Solo ADMIN
    // =======================================================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarConcurso(@PathVariable Long id) {
        concursoService.eliminarConcurso(id);
        return ResponseEntity.noContent().build();
    }
}