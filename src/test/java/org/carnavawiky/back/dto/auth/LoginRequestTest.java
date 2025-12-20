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

class LoginRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Debe crear una instancia con el constructor vacío y getters/setters")
    void testNoArgsConstructorAndAccessors() {
        LoginRequest request = new LoginRequest();
        request.setUsername("userTest");
        request.setPassword("pass123");

        assertEquals("userTest", request.getUsername());
        assertEquals("pass123", request.getPassword());
    }

    @Test
    @DisplayName("Debe crear una instancia con el constructor AllArgs")
    void testAllArgsConstructor() {
        LoginRequest request = new LoginRequest("admin", "secret");

        assertEquals("admin", request.getUsername());
        assertEquals("secret", request.getPassword());
    }

    @Test
    @DisplayName("Debe validar correctamente equals, hashCode y toString")
    void testEqualsHashCodeToString() {
        LoginRequest req1 = new LoginRequest("user", "pass");
        LoginRequest req2 = new LoginRequest("user", "pass");
        LoginRequest req3 = new LoginRequest("other", "pass");

        assertEquals(req1, req2);
        assertNotEquals(req1, req3);
        assertEquals(req1.hashCode(), req2.hashCode());
        assertNotNull(req1.toString());
    }

    @Test
    @DisplayName("Debe fallar la validación si los campos están vacíos")
    void testValidationFailsWhenFieldsAreBlank() {
        // Objeto con campos en blanco o nulos
        LoginRequest request = new LoginRequest("", " ");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        // Esperamos 2 violaciones (username y password)
        assertEquals(2, violations.size());

        boolean usernameError = violations.stream()
                .anyMatch(v -> v.getMessage().equals("El nombre de usuario es obligatorio"));
        boolean passwordError = violations.stream()
                .anyMatch(v -> v.getMessage().equals("La contraseña es obligatoria"));

        assertTrue(usernameError);
        assertTrue(passwordError);
    }

    @Test
    @DisplayName("Debe pasar la validación con datos correctos")
    void testValidationSuccess() {
        LoginRequest request = new LoginRequest("validUser", "validPass");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty(), "No debería haber errores de validación");
    }
}