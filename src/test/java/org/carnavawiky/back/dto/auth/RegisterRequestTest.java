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

class RegisterRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Debe probar el constructor sin argumentos y getters/setters")
    void testNoArgsConstructorAndAccessors() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("usuarioTest");
        request.setPassword("password123");
        request.setEmail("test@email.com");

        assertEquals("usuarioTest", request.getUsername());
        assertEquals("password123", request.getPassword());
        assertEquals("test@email.com", request.getEmail());
    }

    @Test
    @DisplayName("Debe probar el constructor con todos los argumentos")
    void testAllArgsConstructor() {
        RegisterRequest request = new RegisterRequest("adminUser", "securePass", "admin@email.com");

        assertEquals("adminUser", request.getUsername());
        assertEquals("securePass", request.getPassword());
        assertEquals("admin@email.com", request.getEmail());
    }

    @Test
    @DisplayName("Debe validar equals, hashCode y toString")
    void testEqualsHashCodeToString() {
        RegisterRequest req1 = new RegisterRequest("user", "pass123", "email@test.com");
        RegisterRequest req2 = new RegisterRequest("user", "pass123", "email@test.com");
        RegisterRequest req3 = new RegisterRequest("other", "pass123", "email@test.com");

        assertEquals(req1, req2);
        assertNotEquals(req1, req3);
        assertEquals(req1.hashCode(), req2.hashCode());
        assertNotNull(req1.toString());
    }

    @Test
    @DisplayName("Debe fallar cuando los campos están vacíos (@NotBlank)")
    void testValidationFailsWhenBlank() {
        RegisterRequest request = new RegisterRequest("", "", "");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        // Se esperan múltiples fallos (NotBlank y Size)
        assertFalse(violations.isEmpty());

        boolean usernameBlank = violations.stream()
                .anyMatch(v -> v.getMessage().equals("El nombre de usuario es obligatorio"));
        boolean passwordBlank = violations.stream()
                .anyMatch(v -> v.getMessage().equals("La contraseña es obligatoria"));
        boolean emailBlank = violations.stream()
                .anyMatch(v -> v.getMessage().equals("El email es obligatorio"));

        assertTrue(usernameBlank);
        assertTrue(passwordBlank);
        assertTrue(emailBlank);
    }

    @Test
    @DisplayName("Debe fallar cuando el formato del email es incorrecto")
    void testValidationFailsWhenInvalidEmail() {
        RegisterRequest request = new RegisterRequest("usuarioValido", "passwordValido", "email-sin-formato");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        boolean emailError = violations.stream()
                .anyMatch(v -> v.getMessage().equals("Formato de email incorrecto"));

        assertTrue(emailError);
    }

    @Test
    @DisplayName("Debe fallar cuando el tamaño del usuario o contraseña no es correcto")
    void testValidationFailsWhenSizeIsInvalid() {
        // Usuario muy corto (min 4) y contraseña corta (min 6)
        RegisterRequest request = new RegisterRequest("abc", "123", "test@email.com");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        boolean usernameSizeError = violations.stream()
                .anyMatch(v -> v.getMessage().equals("El usuario debe tener entre 4 y 25 caracteres"));
        boolean passwordSizeError = violations.stream()
                .anyMatch(v -> v.getMessage().equals("La contraseña debe tener al menos 6 caracteres"));

        assertTrue(usernameSizeError);
        assertTrue(passwordSizeError);
    }

    @Test
    @DisplayName("Debe pasar la validación con datos correctos")
    void testValidationSuccess() {
        RegisterRequest request = new RegisterRequest("usuarioValido", "passwordValido", "test@email.com");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }
}