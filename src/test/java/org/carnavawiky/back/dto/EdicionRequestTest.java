package org.carnavawiky.back.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EdicionRequestTest {

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        EdicionRequest request = new EdicionRequest();
        
        request.setAnho(2024);
        request.setConcursoId(10L);

        assertNotNull(request);
        assertEquals(2024, request.getAnho());
        assertEquals(10L, request.getConcursoId());
    }

    // =======================================================
    // TESTS PARA COBERTURA DE MÉTODOS DE LOMBOK (@Data)
    // =======================================================

    @Test
    @DisplayName("Debe cubrir todas las ramas de equals y hashCode variando cada campo")
    void testEqualsAndHashCode_Exhaustivo() {
        // Objeto base
        EdicionRequest r1 = new EdicionRequest();
        r1.setAnho(2024);
        r1.setConcursoId(10L);

        // Copia exacta
        EdicionRequest r2 = new EdicionRequest();
        r2.setAnho(2024);
        r2.setConcursoId(10L);

        // --- IGUALDAD BÁSICA ---
        assertEquals(r1, r1); // Reflexivo
        assertEquals(r1, r2); // Simétrico
        assertEquals(r2, r1);
        assertEquals(r1.hashCode(), r2.hashCode()); // HashCode consistente

        assertNotEquals(r1, null); // No igual a null
        assertNotEquals(r1, new Object()); // No igual a otro tipo

        // --- VARIACIONES CAMPO A CAMPO ---

        // 1. Anho
        EdicionRequest rDiffAnho = copiar(r1); rDiffAnho.setAnho(2025);
        assertNotEquals(r1, rDiffAnho);
        assertNotEquals(r1.hashCode(), rDiffAnho.hashCode());
        
        EdicionRequest rAnhoNull = copiar(r1); rAnhoNull.setAnho(null);
        assertNotEquals(r1, rAnhoNull);
        assertNotEquals(rAnhoNull, r1);

        // 2. ConcursoId
        EdicionRequest rDiffConc = copiar(r1); rDiffConc.setConcursoId(99L);
        assertNotEquals(r1, rDiffConc);
        assertNotEquals(r1.hashCode(), rDiffConc.hashCode());
        
        EdicionRequest rConcNull = copiar(r1); rConcNull.setConcursoId(null);
        assertNotEquals(r1, rConcNull);
        assertNotEquals(rConcNull, r1);
        
        // 3. ConcursoId Null vs Null
        EdicionRequest rConcNull2 = copiar(r1); rConcNull2.setConcursoId(null);
        assertEquals(rConcNull, rConcNull2);
    }

    // Método auxiliar para clonar
    private EdicionRequest copiar(EdicionRequest original) {
        EdicionRequest copia = new EdicionRequest();
        copia.setAnho(original.getAnho());
        copia.setConcursoId(original.getConcursoId());
        return copia;
    }

    @Test
    @DisplayName("Debe generar una representación en String")
    void testToString() {
        EdicionRequest request = new EdicionRequest();
        request.setAnho(2024);
        
        String s = request.toString();
        assertNotNull(s);
        assertTrue(s.contains("anho=2024"));
    }

    @Test
    @DisplayName("Debe cumplir con canEqual para la simetría de equals")
    void testCanEqual() {
        EdicionRequest r = new EdicionRequest();
        assertTrue(r.canEqual(new EdicionRequest()));
        assertFalse(r.canEqual(new Object()));
    }
}