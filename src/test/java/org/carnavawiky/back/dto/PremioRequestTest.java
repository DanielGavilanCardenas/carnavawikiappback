package org.carnavawiky.back.dto;

import org.carnavawiky.back.model.Modalidad;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PremioRequestTest {

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        PremioRequest request = new PremioRequest();
        
        request.setPuesto(1);
        request.setModalidad(Modalidad.COMPARSA);
        request.setAgrupacionId(10L);
        request.setEdicionId(20L);

        assertNotNull(request);
        assertEquals(1, request.getPuesto());
        assertEquals(Modalidad.COMPARSA, request.getModalidad());
        assertEquals(10L, request.getAgrupacionId());
        assertEquals(20L, request.getEdicionId());
    }

    // =======================================================
    // TESTS PARA COBERTURA DE MÉTODOS DE LOMBOK (@Data)
    // =======================================================

    @Test
    @DisplayName("Debe cubrir todas las ramas de equals y hashCode variando cada campo")
    void testEqualsAndHashCode_Exhaustivo() {
        // Objeto base
        PremioRequest r1 = new PremioRequest();
        r1.setPuesto(1);
        r1.setModalidad(Modalidad.CHIRIGOTA);
        r1.setAgrupacionId(10L);
        r1.setEdicionId(20L);

        // Copia exacta
        PremioRequest r2 = new PremioRequest();
        r2.setPuesto(1);
        r2.setModalidad(Modalidad.CHIRIGOTA);
        r2.setAgrupacionId(10L);
        r2.setEdicionId(20L);

        // --- IGUALDAD BÁSICA ---
        assertEquals(r1, r1); // Reflexivo
        assertEquals(r1, r2); // Simétrico
        assertEquals(r2, r1);
        assertEquals(r1.hashCode(), r2.hashCode()); // HashCode consistente

        assertNotEquals(r1, null); // No igual a null
        assertNotEquals(r1, new Object()); // No igual a otro tipo

        // --- VARIACIONES CAMPO A CAMPO ---

        // 1. Puesto
        PremioRequest rDiffPuesto = copiar(r1); rDiffPuesto.setPuesto(2);
        assertNotEquals(r1, rDiffPuesto);
        assertNotEquals(r1.hashCode(), rDiffPuesto.hashCode());
        
        PremioRequest rPuestoNull = copiar(r1); rPuestoNull.setPuesto(null);
        assertNotEquals(r1, rPuestoNull);
        assertNotEquals(rPuestoNull, r1);

        // 2. Modalidad
        PremioRequest rDiffMod = copiar(r1); rDiffMod.setModalidad(Modalidad.CORO);
        assertNotEquals(r1, rDiffMod);
        assertNotEquals(r1.hashCode(), rDiffMod.hashCode());
        
        PremioRequest rModNull = copiar(r1); rModNull.setModalidad(null);
        assertNotEquals(r1, rModNull);
        assertNotEquals(rModNull, r1);

        // 3. AgrupacionId
        PremioRequest rDiffAgr = copiar(r1); rDiffAgr.setAgrupacionId(99L);
        assertNotEquals(r1, rDiffAgr);
        assertNotEquals(r1.hashCode(), rDiffAgr.hashCode());
        
        PremioRequest rAgrNull = copiar(r1); rAgrNull.setAgrupacionId(null);
        assertNotEquals(r1, rAgrNull);
        assertNotEquals(rAgrNull, r1);

        // 4. EdicionId
        PremioRequest rDiffEd = copiar(r1); rDiffEd.setEdicionId(99L);
        assertNotEquals(r1, rDiffEd);
        assertNotEquals(r1.hashCode(), rDiffEd.hashCode());
        
        PremioRequest rEdNull = copiar(r1); rEdNull.setEdicionId(null);
        assertNotEquals(r1, rEdNull);
        assertNotEquals(rEdNull, r1);
        
        // 5. EdicionId Null vs Null
        PremioRequest rEdNull2 = copiar(r1); rEdNull2.setEdicionId(null);
        assertEquals(rEdNull, rEdNull2);
    }

    // Método auxiliar para clonar
    private PremioRequest copiar(PremioRequest original) {
        PremioRequest copia = new PremioRequest();
        copia.setPuesto(original.getPuesto());
        copia.setModalidad(original.getModalidad());
        copia.setAgrupacionId(original.getAgrupacionId());
        copia.setEdicionId(original.getEdicionId());
        return copia;
    }

    @Test
    @DisplayName("Debe generar una representación en String")
    void testToString() {
        PremioRequest request = new PremioRequest();
        request.setPuesto(1);
        
        String s = request.toString();
        assertNotNull(s);
        assertTrue(s.contains("puesto=1"));
    }

    @Test
    @DisplayName("Debe cumplir con canEqual para la simetría de equals")
    void testCanEqual() {
        PremioRequest r = new PremioRequest();
        assertTrue(r.canEqual(new PremioRequest()));
        assertFalse(r.canEqual(new Object()));
    }
}