package org.carnavawiky.back.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.carnavawiky.back.dto.auth.RegisterRequest;
import org.carnavawiky.back.dto.auth.TokenResponse;
import org.carnavawiky.back.model.Usuario;
import org.carnavawiky.back.dto.auth.LoginRequest;
import org.carnavawiky.back.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Endpoints para el registro, login y manejo de tokens.")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    @Autowired
    private AuthService authService;

    /**
     * Endpoint para registrar un nuevo usuario en la base de datos.
     * La cuenta se crea deshabilitada (enabled=false) para requerir activación por email.
     * @param request Datos de registro (username, email, password).
     * @return El objeto Usuario creado (sin el token de activación en la respuesta).
     */
    @Operation(summary = "Registrar un nuevo usuario",
            description = "Crea un usuario con ROLE_USER y genera un token de activación.")
    @PostMapping("/register")
    public ResponseEntity<Usuario> registerUser(@Valid @RequestBody RegisterRequest request) {
        Usuario usuario = authService.register(request);

        // CRÍTICO DE SEGURIDAD: Limpiamos el token de activación antes de devolver el objeto
        // para que no sea visible en la respuesta HTTP. El token está guardado en la DB.
        usuario.setActivationToken(null);

        // TODO: Aquí iría la lógica para enviar el email de activación.

        logger.info("Usuario registrado " + request.getUsername());
        return new ResponseEntity<>(usuario, HttpStatus.CREATED);
    }

    /**
     * Endpoint para iniciar sesión y obtener los tokens JWT.
     * @param request Credenciales de login (username/email y password).
     * @return Objeto TokenResponse con accessToken y refreshToken.
     */
    @Operation(summary = "Iniciar sesión",
            description = "Autentica al usuario y devuelve el JWT y el Refresh Token.")
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> loginUser(@Valid @RequestBody LoginRequest request) {
        TokenResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    // =======================================================
    // ENDPOINTS DE PRUEBA DE SEGURIDAD Y ROLES
    // =======================================================

    /**
     * Endpoint temporal para verificar que solo los usuarios con ROLE_ADMIN pueden acceder.
     */
    @Operation(summary = "Endpoint de prueba ADMIN",
            description = "Solo accesible por usuarios autenticados con ROLE_ADMIN.")
    @GetMapping("/admin/test")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminTest() {
        return ResponseEntity.ok("Acceso concedido al ADMINISTRADOR.");
    }

    /**
     * Endpoint temporal para verificar que cualquier usuario autenticado (ROLE_USER, ADMIN, etc.) puede acceder.
     */
    @Operation(summary = "Endpoint de prueba AUTHENTICADO",
            description = "Accesible por cualquier usuario con un JWT válido.")
    @GetMapping("/user/test")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> userTest() {
        return ResponseEntity.ok("Acceso concedido al usuario autenticado.");
    }

    // TODO: Los endpoints para refreshToken y activationToken se añadirán en pasos posteriores.
}