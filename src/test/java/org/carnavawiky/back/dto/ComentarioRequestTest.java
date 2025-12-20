package org.carnavawiky.back.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ComentarioRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Debe ser válido cuando todos los campos cumplen las restricciones")
    void testComentarioRequest_Valid() {
        ComentarioRequest request = new ComentarioRequest();
        request.setContenido("Excelente agrupación, muy buena letra.");
        request.setPuntuacion(5);
        request.setAgrupacionId(1L);
        request.setUsuarioId(1L);

        Set<ConstraintViolation<ComentarioRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
    }

    @Test
    @DisplayName("Debe fallar si el contenido está en blanco o excede el máximo")
    void testComentarioRequest_InvalidContenido() {
        ComentarioRequest request = new ComentarioRequest();
        request.setContenido(""); // Error: @NotBlank
        request.setAgrupacionId(1L);
        request.setUsuarioId(1L);

        Set<ConstraintViolation<ComentarioRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("obligatorio")));

        // Probar límite de caracteres
        request.setContenido("a".repeat(1001)); // Error: @Size(max=1000)
        violations = validator.validate(request);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("1000 caracteres")));
    }

    @Test
    @DisplayName("Debe fallar si la puntuación está fuera del rango 1-5")
    void testComentarioRequest_InvalidPuntuacion() {
        ComentarioRequest request = new ComentarioRequest();
        request.setContenido("Contenido válido");
        request.setAgrupacionId(1L);
        request.setUsuarioId(1L);

        request.setPuntuacion(6); // Error: @Max(5)
        Set<ConstraintViolation<ComentarioRequest>> violations = validator.validate(request);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("máxima es 5")));

        request.setPuntuacion(0); // Error: @Min(1)
        violations = validator.validate(request);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("mínima es 1")));
    }

    @Test
    @DisplayName("Debe fallar si faltan los IDs obligatorios")
    void testComentarioRequest_NullIds() {
        ComentarioRequest request = new ComentarioRequest();
        request.setContenido("Contenido válido");
        // No seteamos agrupacionId ni usuarioId (@NotNull)

        Set<ConstraintViolation<ComentarioRequest>> violations = validator.validate(request);
        assertEquals(2, violations.size(), "Debería haber 2 violaciones por los IDs nulos");
    }

    @Test
    @DisplayName("Debe ser válido si la puntuación es nula (es opcional según la clase)")
    void testComentarioRequest_NullPuntuacion_Ok() {
        ComentarioRequest request = new ComentarioRequest();
        request.setContenido("Comentario sin nota");
        request.setPuntuacion(null); // Opcional
        request.setAgrupacionId(1L);
        request.setUsuarioId(1L);

        Set<ConstraintViolation<ComentarioRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "La puntuación nula debería ser permitida");
    }
}