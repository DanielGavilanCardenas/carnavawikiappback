package org.carnavawiky.back.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConcursoRequestTest {

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        ConcursoRequest request = new ConcursoRequest();
        
        request.setNombre("COAC");
        request.setEstaActivo(true);
        request.setLocalidadId(10L);

        assertNotNull(request);
        assertEquals("COAC", request.getNombre());
        assertTrue(request.getEstaActivo());
        assertEquals(10L, request.getLocalidadId());
    }

    // =======================================================
    // TESTS PARA COBERTURA DE MÉTODOS DE LOMBOK (@Data)
    // =======================================================

    @Test
    @DisplayName("Debe cubrir todas las ramas de equals y hashCode variando cada campo")
    void testEqualsAndHashCode_Exhaustivo() {
        // Objeto base
        ConcursoRequest r1 = new ConcursoRequest();
        r1.setNombre("Nombre");
        r1.setEstaActivo(true);
        r1.setLocalidadId(10L);

        // Copia exacta
        ConcursoRequest r2 = new ConcursoRequest();
        r2.setNombre("Nombre");
        r2.setEstaActivo(true);
        r2.setLocalidadId(10L);

        // --- IGUALDAD BÁSICA ---
        assertEquals(r1, r1); // Reflexivo
        assertEquals(r1, r2); // Simétrico
        assertEquals(r2, r1);
        assertEquals(r1.hashCode(), r2.hashCode()); // HashCode consistente

        assertNotEquals(r1, null); // No igual a null
        assertNotEquals(r1, new Object()); // No igual a otro tipo

        // --- VARIACIONES CAMPO A CAMPO ---

        // 1. Nombre
        ConcursoRequest rDiffNombre = copiar(r1); rDiffNombre.setNombre("Otro");
        assertNotEquals(r1, rDiffNombre);
        assertNotEquals(r1.hashCode(), rDiffNombre.hashCode());
        
        ConcursoRequest rNombreNull = copiar(r1); rNombreNull.setNombre(null);
        assertNotEquals(r1, rNombreNull);
        assertNotEquals(rNombreNull, r1);

        // 2. EstaActivo
        ConcursoRequest rDiffActivo = copiar(r1); rDiffActivo.setEstaActivo(false);
        assertNotEquals(r1, rDiffActivo);
        assertNotEquals(r1.hashCode(), rDiffActivo.hashCode());
        
        ConcursoRequest rActivoNull = copiar(r1); rActivoNull.setEstaActivo(null);
        assertNotEquals(r1, rActivoNull);
        assertNotEquals(rActivoNull, r1);

        // 3. LocalidadId
        ConcursoRequest rDiffLoc = copiar(r1); rDiffLoc.setLocalidadId(99L);
        assertNotEquals(r1, rDiffLoc);
        assertNotEquals(r1.hashCode(), rDiffLoc.hashCode());
        
        ConcursoRequest rLocNull = copiar(r1); rLocNull.setLocalidadId(null);
        assertNotEquals(r1, rLocNull);
        assertNotEquals(rLocNull, r1);
        
        // 4. LocalidadId Null vs Null
        ConcursoRequest rLocNull2 = copiar(r1); rLocNull2.setLocalidadId(null);
        assertEquals(rLocNull, rLocNull2);
    }

    // Método auxiliar para clonar
    private ConcursoRequest copiar(ConcursoRequest original) {
        ConcursoRequest copia = new ConcursoRequest();
        copia.setNombre(original.getNombre());
        copia.setEstaActivo(original.getEstaActivo());
        copia.setLocalidadId(original.getLocalidadId());
        return copia;
    }

    @Test
    @DisplayName("Debe generar una representación en String")
    void testToString() {
        ConcursoRequest request = new ConcursoRequest();
        request.setNombre("Test");
        
        String s = request.toString();
        assertNotNull(s);
        assertTrue(s.contains("nombre=Test"));
    }

    @Test
    @DisplayName("Debe cumplir con canEqual para la simetría de equals")
    void testCanEqual() {
        ConcursoRequest r = new ConcursoRequest();
        assertTrue(r.canEqual(new ConcursoRequest()));
        assertFalse(r.canEqual(new Object()));
    }
}