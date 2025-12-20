package org.carnavawiky.back.dto.error;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    @DisplayName("Debe probar el constructor sin argumentos y los getters/setters")
    void testNoArgsConstructorAndAccessors() {
        // ARRANGE
        ErrorResponse response = new ErrorResponse();
        Instant now = Instant.now();

        // ACT
        response.setMessage("Error de prueba");
        response.setStatus(404);
        response.setError("NOT_FOUND");
        response.setTimestamp(now);

        // ASSERT
        assertEquals("Error de prueba", response.getMessage());
        assertEquals(404, response.getStatus());
        assertEquals("NOT_FOUND", response.getError());
        assertEquals(now, response.getTimestamp());
    }

    @Test
    @DisplayName("Debe probar el constructor con todos los argumentos")
    void testAllArgsConstructor() {
        // ARRANGE
        Instant now = Instant.now();

        // ACT
        ErrorResponse response = new ErrorResponse("Mensaje", 500, "INTERNAL_SERVER_ERROR", now);

        // ASSERT
        assertEquals("Mensaje", response.getMessage());
        assertEquals(500, response.getStatus());
        assertEquals("INTERNAL_SERVER_ERROR", response.getError());
        assertEquals(now, response.getTimestamp());
    }

    @Test
    @DisplayName("Debe validar equals, hashCode y toString")
    void testEqualsHashCodeToString() {
        // ARRANGE
        Instant now = Instant.now();
        ErrorResponse res1 = new ErrorResponse("Error", 400, "BAD_REQUEST", now);
        ErrorResponse res2 = new ErrorResponse("Error", 400, "BAD_REQUEST", now);
        ErrorResponse res3 = new ErrorResponse("Diferente", 400, "BAD_REQUEST", now);

        // ASSERT & ACT
        assertEquals(res1, res2); // Prueba equals
        assertNotEquals(res1, res3); // Prueba equals con objeto distinto
        assertEquals(res1.hashCode(), res2.hashCode()); // Prueba hashCode

        String toStringResult = res1.toString();
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("message=Error"));
        assertTrue(toStringResult.contains("status=400"));
    }
}