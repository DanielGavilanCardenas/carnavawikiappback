package org.carnavawiky.back.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ErrorDetailsTest {

    @Test
    @DisplayName("Debe crear una instancia con el constructor sin argumentos y usar setters")
    void testNoArgsConstructorAndSetters() {
        ErrorDetails errorDetails = new ErrorDetails();
        
        LocalDateTime now = LocalDateTime.now();
        errorDetails.setTimestamp(now);
        errorDetails.setMessage("Error de prueba");
        errorDetails.setDetails("Detalles del error");
        errorDetails.setStatus(404);

        assertEquals(now, errorDetails.getTimestamp());
        assertEquals("Error de prueba", errorDetails.getMessage());
        assertEquals("Detalles del error", errorDetails.getDetails());
        assertEquals(404, errorDetails.getStatus());
    }

    @Test
    @DisplayName("Debe crear una instancia con el constructor con todos los argumentos")
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        ErrorDetails errorDetails = new ErrorDetails(now, "Mensaje", "Detalles", 500);

        assertEquals(now, errorDetails.getTimestamp());
        assertEquals("Mensaje", errorDetails.getMessage());
        assertEquals("Detalles", errorDetails.getDetails());
        assertEquals(500, errorDetails.getStatus());
    }

    @Test
    @DisplayName("Debe verificar los métodos equals y hashCode (generados por @Data)")
    void testEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();
        ErrorDetails error1 = new ErrorDetails(now, "Msg", "Det", 400);
        ErrorDetails error2 = new ErrorDetails(now, "Msg", "Det", 400);
        ErrorDetails error3 = new ErrorDetails(now, "Otro", "Det", 400);

        assertEquals(error1, error2);
        assertEquals(error1.hashCode(), error2.hashCode());
        assertNotEquals(error1, error3);
    }

    @Test
    @DisplayName("Debe verificar el método toString (generado por @Data)")
    void testToString() {
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);
        ErrorDetails errorDetails = new ErrorDetails(now, "Error", "uri=/api", 400);
        
        String stringRepresentation = errorDetails.toString();
        
        assertTrue(stringRepresentation.contains("ErrorDetails"));
        assertTrue(stringRepresentation.contains("timestamp="));
        assertTrue(stringRepresentation.contains("Error"));
        assertTrue(stringRepresentation.contains("uri=/api"));
        assertTrue(stringRepresentation.contains("400"));
    }
}