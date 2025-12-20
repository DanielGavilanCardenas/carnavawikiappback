package org.carnavawiky.back.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ComentarioResponseTest {

    @Test
    @DisplayName("Debe verificar que todos los campos se asignan y recuperan correctamente")
    void testComentarioResponse_GettersAndSetters() {
        // Arrange
        ComentarioResponse response = new ComentarioResponse();
        Long id = 100L;
        String contenido = "Me ha encantado la puesta en escena.";
        Integer puntuacion = 5;
        Boolean aprobado = true;
        LocalDateTime ahora = LocalDateTime.now();

        Long userId = 1L;
        String username = "carnavalero_85";

        Long agrupacionId = 50L;
        String agrupacionNombre = "Los Miuras";

        // Act
        response.setId(id);
        response.setContenido(contenido);
        response.setPuntuacion(puntuacion);
        response.setAprobado(aprobado);
        response.setFechaCreacion(ahora);
        response.setUsuarioId(userId);
        response.setUsuarioUsername(username);
        response.setAgrupacionId(agrupacionId);
        response.setAgrupacionNombre(agrupacionNombre);

        // Assert
        assertAll("Verificación de campos de ComentarioResponse",
                () -> assertEquals(id, response.getId()),
                () -> assertEquals(contenido, response.getContenido()),
                () -> assertEquals(puntuacion, response.getPuntuacion()),
                () -> assertEquals(aprobado, response.getAprobado()),
                () -> assertEquals(ahora, response.getFechaCreacion()),
                () -> assertEquals(userId, response.getUsuarioId()),
                () -> assertEquals(username, response.getUsuarioUsername()),
                () -> assertEquals(agrupacionId, response.getAgrupacionId()),
                () -> assertEquals(agrupacionNombre, response.getAgrupacionNombre())
        );
    }

    @Test
    @DisplayName("Debe validar que equals y hashCode funcionan (Contrato de Lombok)")
    void testEqualsAndHashCode() {
        ComentarioResponse res1 = new ComentarioResponse();
        res1.setId(1L);

        ComentarioResponse res2 = new ComentarioResponse();
        res2.setId(1L);

        assertEquals(res1, res2, "Dos respuestas con el mismo ID deben ser iguales");
        assertEquals(res1.hashCode(), res2.hashCode(), "El HashCode debe ser idéntico para objetos iguales");
    }

    @Test
    @DisplayName("Debe validar que el toString incluye información relevante")
    void testToString() {
        ComentarioResponse response = new ComentarioResponse();
        response.setContenido("Comentario de prueba");
        response.setUsuarioUsername("testuser");

        String toStringResult = response.toString();

        assertTrue(toStringResult.contains("Comentario de prueba"));
        assertTrue(toStringResult.contains("testuser"));
    }
}