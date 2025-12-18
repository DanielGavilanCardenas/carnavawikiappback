package org.carnavawiky.back.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.carnavawiky.back.model.Modalidad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AgrupacionRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Debe ser válido cuando todos los campos son correctos")
    void testAgrupacionRequest_Valid() {
        AgrupacionRequest request = new AgrupacionRequest();
        request.setNombre("Los Piratas");
        request.setModalidad(Modalidad.COMPARSA);
        request.setAnho(1998);
        request.setLocalidadId(1L);

        Set<ConstraintViolation<AgrupacionRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }

    @Test
    @DisplayName("Debe fallar si el nombre está en blanco")
    void testAgrupacionRequest_InvalidNombre() {
        AgrupacionRequest request = new AgrupacionRequest();
        request.setNombre(""); // @NotBlank fallará
        request.setModalidad(Modalidad.CHIRIGOTA);
        request.setAnho(2023);
        request.setLocalidadId(1L);

        Set<ConstraintViolation<AgrupacionRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nombre")));
    }

    @Test
    @DisplayName("Debe fallar si el año es menor a 1800")
    void testAgrupacionRequest_InvalidAnho() {
        AgrupacionRequest request = new AgrupacionRequest();
        request.setNombre("Agrupación Test");
        request.setModalidad(Modalidad.CORO);
        request.setAnho(1750); // Suponiendo @Min(1800)
        request.setLocalidadId(1L);

        Set<ConstraintViolation<AgrupacionRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("anho")));
    }

    @Test
    @DisplayName("Debe fallar si la localidadId es nula")
    void testAgrupacionRequest_NullLocalidad() {
        AgrupacionRequest request = new AgrupacionRequest();
        request.setNombre("Los Prisioneros");
        request.setModalidad(Modalidad.COMPARSA);
        request.setAnho(1991);
        request.setLocalidadId(null); // @NotNull fallará

        Set<ConstraintViolation<AgrupacionRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("localidadId")));
    }

    @Test
    @DisplayName("Probar getters y setters (opcional)")
    void testGettersAndSetters() {
        AgrupacionRequest request = new AgrupacionRequest();
        request.setNombre("Test");
        request.setDescripcion("Desc");

        assertEquals("Test", request.getNombre());
        assertEquals("Desc", request.getDescripcion());
    }
}