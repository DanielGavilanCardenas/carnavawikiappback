package org.carnavawiky.back.controller;

import jakarta.validation.Valid;
import org.carnavawiky.back.dto.auth.LoginRequest;
import org.carnavawiky.back.dto.auth.PasswordResetRequest;
import org.carnavawiky.back.dto.auth.RegisterRequest;
import org.carnavawiky.back.dto.auth.TokenResponse;
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
    // =======================================================
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return new ResponseEntity<>("Usuario registrado exitosamente. Por favor, revisa tu email para activar la cuenta.", HttpStatus.CREATED);
    }

    // =======================================================
    // 2. LOGIN (POST)
    // =======================================================
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> authenticateUser(@Valid @RequestBody LoginRequest request) {
        TokenResponse tokens = authService.login(request);
        return ResponseEntity.ok(tokens);
    }

    // =======================================================
    // 3. ACTIVACIÓN (GET)
    // =======================================================
    @GetMapping("/activate/{token}")
    public ResponseEntity<String> activateAccount(@PathVariable String token) {
        authService.activateAccount(token);
        return ResponseEntity.ok("Cuenta activada exitosamente. ¡Bienvenido! Ya puedes iniciar sesión.");
    }

    // =======================================================
    // 4. REFRESH TOKEN (POST)
    // =======================================================
    @PostMapping("/refreshtoken")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody TokenResponse request) {
        TokenResponse tokens = authService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(tokens);
    }

    // =======================================================
    // 5. SOLICITUD DE RESETEO (POST)
    // Recibe el email y envía el token.
    // =======================================================
    @PostMapping("/forgot-password")
    public ResponseEntity<String> requestPasswordReset(@RequestBody String email) {
        // Nota: Se asume que el body es solo la cadena del email.
        authService.requestPasswordReset(email);
        return ResponseEntity.ok("Si la dirección de email está registrada, se ha enviado un enlace para restablecer la contraseña.");
    }

    // =======================================================
    // 6. RESETEO FINAL (POST)
    // Recibe el token y la nueva contraseña.
    // =======================================================
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody PasswordResetRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok("Contraseña restablecida exitosamente. Ahora puedes iniciar sesión con tu nueva contraseña.");
    }
}