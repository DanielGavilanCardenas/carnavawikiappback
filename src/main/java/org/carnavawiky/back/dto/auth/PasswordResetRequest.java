package org.carnavawiky.back.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetRequest {

    @NotBlank(message = "El token de reseteo es obligatorio.")
    private String token;

    @NotBlank(message = "La nueva contraseña es obligatoria.")
    // Normalmente aquí habría validaciones de seguridad de contraseña (min/max length, symbols, etc.)
    private String newPassword;
}