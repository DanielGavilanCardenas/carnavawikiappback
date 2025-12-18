package org.carnavawiky.back.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.carnavawiky.back.model.RolComponente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ComponenteRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Debe ser válido cuando todos los campos están presentes")
    void testComponenteRequest_Valid() {
        // Arrange
        ComponenteRequest request = new ComponenteRequest();
        request.setRol(RolComponente.DIRECTOR);
        request.setPersonaId(1L);
        request.setAgrupacionId(10L);

        // Act
        Set<ConstraintViolation<ComponenteRequest>> violations = validator.validate(request);

        // Assert
        assertTrue(violations.isEmpty(), "No debería haber errores de validación");
    }

    @Test
    @DisplayName("Debe fallar si el rol es nulo")
    void testComponenteRequest_NullRol() {
        ComponenteRequest request = new ComponenteRequest();
        request.setRol(null); // @NotNull fallará
        request.setPersonaId(1L);
        request.setAgrupacionId(10L);

        Set<ConstraintViolation<ComponenteRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("rol")));
    }

    @Test
    @DisplayName("Debe fallar si los IDs de Persona o Agrupación son nulos")
    void testComponenteRequest_NullIds() {
        ComponenteRequest request = new ComponenteRequest();
        request.setRol(RolComponente.CONTRAALTO);
        request.setPersonaId(null); // @NotNull
        request.setAgrupacionId(null); // @NotNull

        Set<ConstraintViolation<ComponenteRequest>> violations = validator.validate(request);

        assertEquals(2, violations.size(), "Debe haber exactamente dos errores de validación");
    }

    @Test
    @DisplayName("Verificar funcionamiento de Lombok (@Data)")
    void testLombokFeatures() {
        ComponenteRequest req1 = new ComponenteRequest();
        req1.setPersonaId(5L);

        ComponenteRequest req2 = new ComponenteRequest();
        req2.setPersonaId(5L);

        assertAll("Lombok Checks",
                () -> assertEquals(5L, req1.getPersonaId()),
                () -> assertEquals(req1, req2),
                () -> assertTrue(req1.toString().contains("personaId=5"))
        );
    }
}