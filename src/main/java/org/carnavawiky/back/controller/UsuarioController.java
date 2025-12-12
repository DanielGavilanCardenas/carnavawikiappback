package org.carnavawiky.back.controller;

import jakarta.validation.Valid;
import org.carnavawiky.back.dto.UsuarioRequest;
import org.carnavawiky.back.dto.UsuarioResponse;
import org.carnavawiky.back.dto.PageResponse; // <-- NUEVA IMPORTACIÓN
import org.carnavawiky.back.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable; // <-- NUEVA IMPORTACIÓN
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
// TODO: Considerar si el admin puede obtener la lista de usuarios.
@PreAuthorize("hasRole('ADMIN')")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // =======================================================
    // 1. CREAR (POST) - Solo para ADMIN
    // =======================================================
    @PostMapping
    public ResponseEntity<UsuarioResponse> crearUsuario(
            @Valid @RequestBody UsuarioRequest request) {

        UsuarioResponse nuevoUsuario = usuarioService.crearUsuario(request);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    // =======================================================
    // 2. OBTENER TODOS (GET) - CON PAGINACIÓN Y BÚSQUEDA - Solo para ADMIN
    // MODIFICADO para usar Pageable, search y devolver PageResponse
    // =======================================================
    @GetMapping
    public ResponseEntity<PageResponse<UsuarioResponse>> obtenerTodosUsuarios( // << TIPO DE RETORNO MODIFICADO
                                                                               Pageable pageable, // Spring inyecta Pageable
                                                                               @RequestParam(required = false) String search) { // Parámetro de búsqueda

        PageResponse<UsuarioResponse> usuariosPage = usuarioService.obtenerTodosUsuarios(pageable, search);
        return ResponseEntity.ok(usuariosPage);
    }

    // =======================================================
    // 3. OBTENER POR ID (GET /ID) - Solo para ADMIN
    // =======================================================
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> obtenerUsuarioPorId(@PathVariable Long id) {
        UsuarioResponse usuario = usuarioService.obtenerUsuarioPorId(id);
        return ResponseEntity.ok(usuario);
    }

    // =======================================================
    // 4. ACTUALIZAR (PUT /ID) - Solo para ADMIN
    // =======================================================
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> actualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequest request) {

        UsuarioResponse usuarioActualizado = usuarioService.actualizarUsuario(id, request);
        return ResponseEntity.ok(usuarioActualizado);
    }

    // =======================================================
    // 5. ELIMINAR (DELETE /ID) - Solo para ADMIN
    // =======================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}