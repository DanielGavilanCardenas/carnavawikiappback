package org.carnavawiky.back.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EdicionResponseTest {

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        EdicionResponse response = new EdicionResponse();
        
        response.setId(1L);
        response.setAnho(2024);
        response.setConcursoId(10L);
        response.setConcursoNombre("COAC");

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(2024, response.getAnho());
        assertEquals(10L, response.getConcursoId());
        assertEquals("COAC", response.getConcursoNombre());
    }

    // =======================================================
    // TESTS PARA COBERTURA DE MÉTODOS DE LOMBOK (@Data)
    // =======================================================

    @Test
    @DisplayName("Debe cubrir todas las ramas de equals y hashCode variando cada campo")
    void testEqualsAndHashCode_Exhaustivo() {
        // Objeto base
        EdicionResponse r1 = new EdicionResponse();
        r1.setId(1L);
        r1.setAnho(2024);
        r1.setConcursoId(10L);
        r1.setConcursoNombre("COAC");

        // Copia exacta
        EdicionResponse r2 = new EdicionResponse();
        r2.setId(1L);
        r2.setAnho(2024);
        r2.setConcursoId(10L);
        r2.setConcursoNombre("COAC");

        // --- IGUALDAD BÁSICA ---
        assertEquals(r1, r1); // Reflexivo
        assertEquals(r1, r2); // Simétrico
        assertEquals(r2, r1);
        assertEquals(r1.hashCode(), r2.hashCode()); // HashCode consistente

        assertNotEquals(r1, null); // No igual a null
        assertNotEquals(r1, new Object()); // No igual a otro tipo

        // --- VARIACIONES CAMPO A CAMPO ---

        // 1. ID
        EdicionResponse rDiffId = copiar(r1); rDiffId.setId(2L);
        assertNotEquals(r1, rDiffId);
        assertNotEquals(r1.hashCode(), rDiffId.hashCode());
        
        EdicionResponse rIdNull = copiar(r1); rIdNull.setId(null);
        assertNotEquals(r1, rIdNull);
        assertNotEquals(rIdNull, r1);

        // 2. Anho
        EdicionResponse rDiffAnho = copiar(r1); rDiffAnho.setAnho(2025);
        assertNotEquals(r1, rDiffAnho);
        assertNotEquals(r1.hashCode(), rDiffAnho.hashCode());
        
        EdicionResponse rAnhoNull = copiar(r1); rAnhoNull.setAnho(null);
        assertNotEquals(r1, rAnhoNull);
        assertNotEquals(rAnhoNull, r1);

        // 3. ConcursoId
        EdicionResponse rDiffConcId = copiar(r1); rDiffConcId.setConcursoId(99L);
        assertNotEquals(r1, rDiffConcId);
        assertNotEquals(r1.hashCode(), rDiffConcId.hashCode());
        
        EdicionResponse rConcIdNull = copiar(r1); rConcIdNull.setConcursoId(null);
        assertNotEquals(r1, rConcIdNull);
        assertNotEquals(rConcIdNull, r1);

        // 4. ConcursoNombre
        EdicionResponse rDiffConcNom = copiar(r1); rDiffConcNom.setConcursoNombre("Otro");
        assertNotEquals(r1, rDiffConcNom);
        assertNotEquals(r1.hashCode(), rDiffConcNom.hashCode());
        
        EdicionResponse rConcNomNull = copiar(r1); rConcNomNull.setConcursoNombre(null);
        assertNotEquals(r1, rConcNomNull);
        assertNotEquals(rConcNomNull, r1);
        
        // 5. ConcursoNombre Null vs Null
        EdicionResponse rConcNomNull2 = copiar(r1); rConcNomNull2.setConcursoNombre(null);
        assertEquals(rConcNomNull, rConcNomNull2);
    }

    // Método auxiliar para clonar
    private EdicionResponse copiar(EdicionResponse original) {
        EdicionResponse copia = new EdicionResponse();
        copia.setId(original.getId());
        copia.setAnho(original.getAnho());
        copia.setConcursoId(original.getConcursoId());
        copia.setConcursoNombre(original.getConcursoNombre());
        return copia;
    }

    @Test
    @DisplayName("Debe generar una representación en String")
    void testToString() {
        EdicionResponse response = new EdicionResponse();
        response.setId(1L);
        response.setAnho(2024);
        
        String s = response.toString();
        assertNotNull(s);
        assertTrue(s.contains("id=1"));
        assertTrue(s.contains("anho=2024"));
    }

    @Test
    @DisplayName("Debe cumplir con canEqual para la simetría de equals")
    void testCanEqual() {
        EdicionResponse r = new EdicionResponse();
        assertTrue(r.canEqual(new EdicionResponse()));
        assertFalse(r.canEqual(new Object()));
    }
}