package org.carnavawiky.back.controller;

import jakarta.validation.Valid;
import org.carnavawiky.back.dto.ComentarioRequest;
import org.carnavawiky.back.dto.ComentarioResponse;
import org.carnavawiky.back.dto.PageResponse;
import org.carnavawiky.back.service.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comentarios")
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    // =======================================================
    // 1. CREAR (POST) - Abierto a USER y ADMIN (Comentario se crea como 'aprobado=false')
    // =======================================================
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ComentarioResponse> crearComentario(
            @Valid @RequestBody ComentarioRequest request) {

        ComentarioResponse nuevoComentario = comentarioService.crearComentario(request);
        return new ResponseEntity<>(nuevoComentario, HttpStatus.CREATED);
    }

    // =======================================================
    // 2. OBTENER TODOS (GET) - Abierto a USER y ADMIN
    // La lógica de servicio filtra automáticamente por 'aprobado=true' en búsquedas públicas.
    // =======================================================
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PageResponse<ComentarioResponse>> obtenerTodosComentarios(
            Pageable pageable,
            @RequestParam(required = false) String search) {

        PageResponse<ComentarioResponse> comentariosPage = comentarioService.obtenerTodosComentarios(pageable, search);
        return ResponseEntity.ok(comentariosPage);
    }

    // =======================================================
    // 3. OBTENER POR ID (GET /ID) - Abierto a USER y ADMIN
    // =======================================================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ComentarioResponse> obtenerComentarioPorId(@PathVariable Long id) {
        ComentarioResponse comentario = comentarioService.obtenerComentarioPorId(id);
        return ResponseEntity.ok(comentario);
    }

    // =======================================================
    // 4. ACTUALIZAR (PUT /ID) - Solo ADMIN
    // =======================================================
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ComentarioResponse> actualizarComentario(
            @PathVariable Long id,
            @Valid @RequestBody ComentarioRequest request) {

        ComentarioResponse comentarioActualizado = comentarioService.actualizarComentario(id, request);
        return ResponseEntity.ok(comentarioActualizado);
    }

    // =======================================================
    // 5. ELIMINAR (DELETE /ID) - Solo ADMIN
    // =======================================================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarComentario(@PathVariable Long id) {
        comentarioService.eliminarComentario(id);
        return ResponseEntity.noContent().build();
    }

    // =======================================================
    // 6. APROBAR COMENTARIO (PUT /ID/aprobar) - Solo ADMIN
    // =======================================================
    @PutMapping("/{id}/aprobar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ComentarioResponse> aprobarComentario(@PathVariable Long id) {
        ComentarioResponse comentarioAprobado = comentarioService.aprobarComentario(id);
        return ResponseEntity.ok(comentarioAprobado);
    }
}