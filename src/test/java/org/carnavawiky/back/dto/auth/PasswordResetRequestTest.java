package org.carnavawiky.back.dto.auth;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PasswordResetRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Debe probar el constructor sin argumentos y getters/setters")
    void testNoArgsConstructorAndAccessors() {
        PasswordResetRequest request = new PasswordResetRequest();
        request.setToken("token-123");
        request.setNewPassword("password123");

        assertEquals("token-123", request.getToken());
        assertEquals("password123", request.getNewPassword());
    }

    @Test
    @DisplayName("Debe probar el constructor con todos los argumentos")
    void testAllArgsConstructor() {
        PasswordResetRequest request = new PasswordResetRequest("mi-token", "nueva-clave");

        assertEquals("mi-token", request.getToken());
        assertEquals("nueva-clave", request.getNewPassword());
    }

    @Test
    @DisplayName("Debe validar equals, hashCode y toString")
    void testEqualsHashCodeToString() {
        PasswordResetRequest req1 = new PasswordResetRequest("token", "pass");
        PasswordResetRequest req2 = new PasswordResetRequest("token", "pass");
        PasswordResetRequest req3 = new PasswordResetRequest("distinto", "pass");

        assertEquals(req1, req2);
        assertNotEquals(req1, req3);
        assertEquals(req1.hashCode(), req2.hashCode());
        assertNotNull(req1.toString());
    }

    @Test
    @DisplayName("Debe fallar cuando los campos son nulos o vacíos (@NotBlank)")
    void testValidationFailsWhenBlank() {
        PasswordResetRequest request = new PasswordResetRequest("", " ");

        Set<ConstraintViolation<PasswordResetRequest>> violations = validator.validate(request);

        // Debe haber 2 violaciones
        assertEquals(2, violations.size());

        boolean tokenError = violations.stream()
                .anyMatch(v -> v.getMessage().equals("El token de reseteo es obligatorio."));
        boolean passwordError = violations.stream()
                .anyMatch(v -> v.getMessage().equals("La nueva contraseña es obligatoria."));

        assertTrue(tokenError);
        assertTrue(passwordError);
    }

    @Test
    @DisplayName("Debe pasar la validación con datos correctos")
    void testValidationSuccess() {
        PasswordResetRequest request = new PasswordResetRequest("valid-token", "securePass123");

        Set<ConstraintViolation<PasswordResetRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }
}