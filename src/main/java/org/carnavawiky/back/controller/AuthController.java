package org.carnavawiky.back.controller;

import jakarta.validation.Valid;
import org.carnavawiky.back.dto.auth.LoginRequest;
import org.carnavawiky.back.dto.auth.RegisterRequest;
import org.carnavawiky.back.dto.auth.TokenResponse;
import org.carnavawiky.back.model.Usuario;
import org.carnavawiky.back.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // =======================================================
    // 1. REGISTRO (POST)
    // Crea un usuario y envía el email de activación.
    // =======================================================
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest request) {

        authService.register(request);

        // Retornamos un 201 Created y un mensaje informativo.
        return new ResponseEntity<>("Usuario registrado exitosamente. Por favor, revisa tu email para activar la cuenta.", HttpStatus.CREATED);
    }

    // =======================================================
    // 2. LOGIN (POST)
    // Genera Access Token y Refresh Token.
    // =======================================================
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> authenticateUser(@Valid @RequestBody LoginRequest request) {

        TokenResponse tokens = authService.login(request);
        return ResponseEntity.ok(tokens);
    }

    // =======================================================
    // 3. ACTIVACIÓN (GET)
    // Endpoint público para que el usuario active su cuenta haciendo clic en el email.
    // =======================================================
    @GetMapping("/activate/{token}")
    public ResponseEntity<String> activateAccount(@PathVariable String token) {

        authService.activateAccount(token);

        // El servicio maneja el 404/400. Si llega aquí, es éxito.
        return ResponseEntity.ok("Cuenta activada exitosamente. ¡Bienvenido! Ya puedes iniciar sesión.");
    }

    // =======================================================
    // 4. REFRESH TOKEN (POST)
    // Usa el Refresh Token para obtener un nuevo Access Token.
    // =======================================================
    @PostMapping("/refreshtoken")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody TokenResponse request) {

        TokenResponse tokens = authService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(tokens);
    }
}