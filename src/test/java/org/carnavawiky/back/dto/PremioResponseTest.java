package org.carnavawiky.back.dto;

import org.carnavawiky.back.model.Modalidad;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PremioResponseTest {

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        PremioResponse response = new PremioResponse();
        
        response.setId(1L);
        response.setPuesto(1);
        response.setModalidad(Modalidad.COMPARSA);
        response.setAgrupacionId(10L);
        response.setAgrupacionNombre("Los Piratas");
        response.setEdicionId(20L);
        response.setEdicionAnho(1998);
        response.setConcursoNombre("COAC");

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(1, response.getPuesto());
        assertEquals(Modalidad.COMPARSA, response.getModalidad());
        assertEquals(10L, response.getAgrupacionId());
        assertEquals("Los Piratas", response.getAgrupacionNombre());
        assertEquals(20L, response.getEdicionId());
        assertEquals(1998, response.getEdicionAnho());
        assertEquals("COAC", response.getConcursoNombre());
    }

    // =======================================================
    // TESTS PARA COBERTURA DE MÉTODOS DE LOMBOK (@Data)
    // =======================================================

    @Test
    @DisplayName("Debe cubrir todas las ramas de equals y hashCode variando cada campo")
    void testEqualsAndHashCode_Exhaustivo() {
        // Objeto base
        PremioResponse r1 = new PremioResponse();
        r1.setId(1L);
        r1.setPuesto(1);
        r1.setModalidad(Modalidad.CHIRIGOTA);
        r1.setAgrupacionId(10L);
        r1.setAgrupacionNombre("NombreA");
        r1.setEdicionId(20L);
        r1.setEdicionAnho(2000);
        r1.setConcursoNombre("ConcursoA");

        // Copia exacta
        PremioResponse r2 = new PremioResponse();
        r2.setId(1L);
        r2.setPuesto(1);
        r2.setModalidad(Modalidad.CHIRIGOTA);
        r2.setAgrupacionId(10L);
        r2.setAgrupacionNombre("NombreA");
        r2.setEdicionId(20L);
        r2.setEdicionAnho(2000);
        r2.setConcursoNombre("ConcursoA");

        // --- IGUALDAD BÁSICA ---
        assertEquals(r1, r1); // Reflexivo
        assertEquals(r1, r2); // Simétrico
        assertEquals(r2, r1);
        assertEquals(r1.hashCode(), r2.hashCode()); // HashCode consistente

        assertNotEquals(r1, null); // No igual a null
        assertNotEquals(r1, new Object()); // No igual a otro tipo

        // --- VARIACIONES CAMPO A CAMPO ---

        // 1. ID
        PremioResponse rDiffId = copiar(r1); rDiffId.setId(2L);
        assertNotEquals(r1, rDiffId);
        assertNotEquals(r1.hashCode(), rDiffId.hashCode());
        
        PremioResponse rIdNull = copiar(r1); rIdNull.setId(null);
        assertNotEquals(r1, rIdNull);
        assertNotEquals(rIdNull, r1);

        // 2. Puesto
        PremioResponse rDiffPuesto = copiar(r1); rDiffPuesto.setPuesto(2);
        assertNotEquals(r1, rDiffPuesto);
        assertNotEquals(r1.hashCode(), rDiffPuesto.hashCode());
        
        PremioResponse rPuestoNull = copiar(r1); rPuestoNull.setPuesto(null);
        assertNotEquals(r1, rPuestoNull);
        assertNotEquals(rPuestoNull, r1);

        // 3. Modalidad
        PremioResponse rDiffMod = copiar(r1); rDiffMod.setModalidad(Modalidad.CORO);
        assertNotEquals(r1, rDiffMod);
        assertNotEquals(r1.hashCode(), rDiffMod.hashCode());
        
        PremioResponse rModNull = copiar(r1); rModNull.setModalidad(null);
        assertNotEquals(r1, rModNull);
        assertNotEquals(rModNull, r1);

        // 4. AgrupacionId
        PremioResponse rDiffAgrId = copiar(r1); rDiffAgrId.setAgrupacionId(99L);
        assertNotEquals(r1, rDiffAgrId);
        assertNotEquals(r1.hashCode(), rDiffAgrId.hashCode());
        
        PremioResponse rAgrIdNull = copiar(r1); rAgrIdNull.setAgrupacionId(null);
        assertNotEquals(r1, rAgrIdNull);
        assertNotEquals(rAgrIdNull, r1);

        // 5. AgrupacionNombre
        PremioResponse rDiffAgrNom = copiar(r1); rDiffAgrNom.setAgrupacionNombre("Otro");
        assertNotEquals(r1, rDiffAgrNom);
        assertNotEquals(r1.hashCode(), rDiffAgrNom.hashCode());
        
        PremioResponse rAgrNomNull = copiar(r1); rAgrNomNull.setAgrupacionNombre(null);
        assertNotEquals(r1, rAgrNomNull);
        assertNotEquals(rAgrNomNull, r1);

        // 6. EdicionId
        PremioResponse rDiffEdId = copiar(r1); rDiffEdId.setEdicionId(99L);
        assertNotEquals(r1, rDiffEdId);
        assertNotEquals(r1.hashCode(), rDiffEdId.hashCode());
        
        PremioResponse rEdIdNull = copiar(r1); rEdIdNull.setEdicionId(null);
        assertNotEquals(r1, rEdIdNull);
        assertNotEquals(rEdIdNull, r1);

        // 7. EdicionAnho
        PremioResponse rDiffEdAnho = copiar(r1); rDiffEdAnho.setEdicionAnho(2025);
        assertNotEquals(r1, rDiffEdAnho);
        assertNotEquals(r1.hashCode(), rDiffEdAnho.hashCode());
        
        PremioResponse rEdAnhoNull = copiar(r1); rEdAnhoNull.setEdicionAnho(null);
        assertNotEquals(r1, rEdAnhoNull);
        assertNotEquals(rEdAnhoNull, r1);

        // 8. ConcursoNombre
        PremioResponse rDiffConc = copiar(r1); rDiffConc.setConcursoNombre("OtroConcurso");
        assertNotEquals(r1, rDiffConc);
        assertNotEquals(r1.hashCode(), rDiffConc.hashCode());
        
        PremioResponse rConcNull = copiar(r1); rConcNull.setConcursoNombre(null);
        assertNotEquals(r1, rConcNull);
        assertNotEquals(rConcNull, r1);
        
        // 9. ConcursoNombre Null vs Null
        PremioResponse rConcNull2 = copiar(r1); rConcNull2.setConcursoNombre(null);
        assertEquals(rConcNull, rConcNull2);
    }

    // Método auxiliar para clonar
    private PremioResponse copiar(PremioResponse original) {
        PremioResponse copia = new PremioResponse();
        copia.setId(original.getId());
        copia.setPuesto(original.getPuesto());
        copia.setModalidad(original.getModalidad());
        copia.setAgrupacionId(original.getAgrupacionId());
        copia.setAgrupacionNombre(original.getAgrupacionNombre());
        copia.setEdicionId(original.getEdicionId());
        copia.setEdicionAnho(original.getEdicionAnho());
        copia.setConcursoNombre(original.getConcursoNombre());
        return copia;
    }

    @Test
    @DisplayName("Debe generar una representación en String")
    void testToString() {
        PremioResponse response = new PremioResponse();
        response.setId(1L);
        response.setAgrupacionNombre("Test");
        
        String s = response.toString();
        assertNotNull(s);
        assertTrue(s.contains("id=1"));
        assertTrue(s.contains("agrupacionNombre=Test"));
    }

    @Test
    @DisplayName("Debe cumplir con canEqual para la simetría de equals")
    void testCanEqual() {
        PremioResponse r = new PremioResponse();
        assertTrue(r.canEqual(new PremioResponse()));
        assertFalse(r.canEqual(new Object()));
    }
}