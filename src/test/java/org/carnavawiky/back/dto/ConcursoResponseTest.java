package org.carnavawiky.back.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConcursoResponseTest {

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        ConcursoResponse response = new ConcursoResponse();
        
        response.setId(1L);
        response.setNombre("COAC");
        response.setEstaActivo(true);
        response.setLocalidadId(10L);
        response.setLocalidadNombre("Cádiz");

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("COAC", response.getNombre());
        assertTrue(response.getEstaActivo());
        assertEquals(10L, response.getLocalidadId());
        assertEquals("Cádiz", response.getLocalidadNombre());
    }

    // =======================================================
    // TESTS PARA COBERTURA DE MÉTODOS DE LOMBOK (@Data)
    // =======================================================

    @Test
    @DisplayName("Debe cubrir todas las ramas de equals y hashCode variando cada campo")
    void testEqualsAndHashCode_Exhaustivo() {
        // Objeto base
        ConcursoResponse r1 = new ConcursoResponse();
        r1.setId(1L);
        r1.setNombre("Nombre");
        r1.setEstaActivo(true);
        r1.setLocalidadId(10L);
        r1.setLocalidadNombre("Loc");

        // Copia exacta
        ConcursoResponse r2 = new ConcursoResponse();
        r2.setId(1L);
        r2.setNombre("Nombre");
        r2.setEstaActivo(true);
        r2.setLocalidadId(10L);
        r2.setLocalidadNombre("Loc");

        // --- IGUALDAD BÁSICA ---
        assertEquals(r1, r1); // Reflexivo
        assertEquals(r1, r2); // Simétrico
        assertEquals(r2, r1);
        assertEquals(r1.hashCode(), r2.hashCode()); // HashCode consistente

        assertNotEquals(r1, null); // No igual a null
        assertNotEquals(r1, new Object()); // No igual a otro tipo

        // --- VARIACIONES CAMPO A CAMPO ---

        // 1. ID
        ConcursoResponse rDiffId = copiar(r1); rDiffId.setId(2L);
        assertNotEquals(r1, rDiffId);
        assertNotEquals(r1.hashCode(), rDiffId.hashCode());
        
        ConcursoResponse rIdNull = copiar(r1); rIdNull.setId(null);
        assertNotEquals(r1, rIdNull);
        assertNotEquals(rIdNull, r1);

        // 2. Nombre
        ConcursoResponse rDiffNombre = copiar(r1); rDiffNombre.setNombre("Otro");
        assertNotEquals(r1, rDiffNombre);
        assertNotEquals(r1.hashCode(), rDiffNombre.hashCode());
        
        ConcursoResponse rNombreNull = copiar(r1); rNombreNull.setNombre(null);
        assertNotEquals(r1, rNombreNull);
        assertNotEquals(rNombreNull, r1);

        // 3. EstaActivo
        ConcursoResponse rDiffActivo = copiar(r1); rDiffActivo.setEstaActivo(false);
        assertNotEquals(r1, rDiffActivo);
        assertNotEquals(r1.hashCode(), rDiffActivo.hashCode());
        
        ConcursoResponse rActivoNull = copiar(r1); rActivoNull.setEstaActivo(null);
        assertNotEquals(r1, rActivoNull);
        assertNotEquals(rActivoNull, r1);

        // 4. LocalidadId
        ConcursoResponse rDiffLocId = copiar(r1); rDiffLocId.setLocalidadId(99L);
        assertNotEquals(r1, rDiffLocId);
        assertNotEquals(r1.hashCode(), rDiffLocId.hashCode());
        
        ConcursoResponse rLocIdNull = copiar(r1); rLocIdNull.setLocalidadId(null);
        assertNotEquals(r1, rLocIdNull);
        assertNotEquals(rLocIdNull, r1);

        // 5. LocalidadNombre
        ConcursoResponse rDiffLocNom = copiar(r1); rDiffLocNom.setLocalidadNombre("Otro");
        assertNotEquals(r1, rDiffLocNom);
        assertNotEquals(r1.hashCode(), rDiffLocNom.hashCode());
        
        ConcursoResponse rLocNomNull = copiar(r1); rLocNomNull.setLocalidadNombre(null);
        assertNotEquals(r1, rLocNomNull);
        assertNotEquals(rLocNomNull, r1);
        
        // 6. LocalidadNombre Null vs Null
        ConcursoResponse rLocNomNull2 = copiar(r1); rLocNomNull2.setLocalidadNombre(null);
        assertEquals(rLocNomNull, rLocNomNull2);
    }

    // Método auxiliar para clonar
    private ConcursoResponse copiar(ConcursoResponse original) {
        ConcursoResponse copia = new ConcursoResponse();
        copia.setId(original.getId());
        copia.setNombre(original.getNombre());
        copia.setEstaActivo(original.getEstaActivo());
        copia.setLocalidadId(original.getLocalidadId());
        copia.setLocalidadNombre(original.getLocalidadNombre());
        return copia;
    }

    @Test
    @DisplayName("Debe generar una representación en String")
    void testToString() {
        ConcursoResponse response = new ConcursoResponse();
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
        ConcursoResponse r = new ConcursoResponse();
        assertTrue(r.canEqual(new ConcursoResponse()));
        assertFalse(r.canEqual(new Object()));
    }
}