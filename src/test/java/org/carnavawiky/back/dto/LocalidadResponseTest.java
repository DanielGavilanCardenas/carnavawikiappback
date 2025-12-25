package org.carnavawiky.back.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocalidadResponseTest {

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        LocalidadResponse response = new LocalidadResponse();
        
        response.setId(1L);
        response.setNombre("Cádiz");

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Cádiz", response.getNombre());
    }

    // =======================================================
    // TESTS PARA COBERTURA DE MÉTODOS DE LOMBOK (@Data)
    // =======================================================

    @Test
    @DisplayName("Debe cubrir todas las ramas de equals y hashCode variando cada campo")
    void testEqualsAndHashCode_Exhaustivo() {
        // Objeto base
        LocalidadResponse r1 = new LocalidadResponse();
        r1.setId(1L);
        r1.setNombre("Nombre");

        // Copia exacta
        LocalidadResponse r2 = new LocalidadResponse();
        r2.setId(1L);
        r2.setNombre("Nombre");

        // --- IGUALDAD BÁSICA ---
        assertEquals(r1, r1); // Reflexivo
        assertEquals(r1, r2); // Simétrico
        assertEquals(r2, r1);
        assertEquals(r1.hashCode(), r2.hashCode()); // HashCode consistente

        assertNotEquals(r1, null); // No igual a null
        assertNotEquals(r1, new Object()); // No igual a otro tipo

        // --- VARIACIONES CAMPO A CAMPO ---

        // 1. ID
        LocalidadResponse rDiffId = copiar(r1); rDiffId.setId(2L);
        assertNotEquals(r1, rDiffId);
        assertNotEquals(r1.hashCode(), rDiffId.hashCode());
        
        LocalidadResponse rIdNull = copiar(r1); rIdNull.setId(null);
        assertNotEquals(r1, rIdNull);
        assertNotEquals(rIdNull, r1);

        // 2. Nombre
        LocalidadResponse rDiffNombre = copiar(r1); rDiffNombre.setNombre("Otro");
        assertNotEquals(r1, rDiffNombre);
        assertNotEquals(r1.hashCode(), rDiffNombre.hashCode());
        
        LocalidadResponse rNombreNull = copiar(r1); rNombreNull.setNombre(null);
        assertNotEquals(r1, rNombreNull);
        assertNotEquals(rNombreNull, r1);
        
        // 3. Nombre Null vs Null
        LocalidadResponse rNombreNull2 = copiar(r1); rNombreNull2.setNombre(null);
        assertEquals(rNombreNull, rNombreNull2);
    }

    // Método auxiliar para clonar
    private LocalidadResponse copiar(LocalidadResponse original) {
        LocalidadResponse copia = new LocalidadResponse();
        copia.setId(original.getId());
        copia.setNombre(original.getNombre());
        return copia;
    }

    @Test
    @DisplayName("Debe generar una representación en String")
    void testToString() {
        LocalidadResponse response = new LocalidadResponse();
        response.setId(1L);
        response.setNombre("Test");
        
        String s = response.toString();
        assertNotNull(s);
        assertTrue(s.contains("id=1"));
        assertTrue(s.contains("nombre=Test"));
    }

    @Test
    @DisplayName("Debe cumplir con canEqual para la simetría de equals")
    void testCanEqual() {
        LocalidadResponse r = new LocalidadResponse();
        assertTrue(r.canEqual(new LocalidadResponse()));
        assertFalse(r.canEqual(new Object()));
    }
}