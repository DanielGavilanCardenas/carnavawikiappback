package org.carnavawiky.back.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImagenRequestTest {

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        ImagenRequest request = new ImagenRequest();
        
        request.setAgrupacionId(10L);
        request.setEsPortada(true);

        assertNotNull(request);
        assertEquals(10L, request.getAgrupacionId());
        assertTrue(request.getEsPortada());
    }

    // =======================================================
    // TESTS PARA COBERTURA DE MÉTODOS DE LOMBOK (@Data)
    // =======================================================

    @Test
    @DisplayName("Debe cubrir todas las ramas de equals y hashCode variando cada campo")
    void testEqualsAndHashCode_Exhaustivo() {
        // Objeto base
        ImagenRequest r1 = new ImagenRequest();
        r1.setAgrupacionId(10L);
        r1.setEsPortada(true);

        // Copia exacta
        ImagenRequest r2 = new ImagenRequest();
        r2.setAgrupacionId(10L);
        r2.setEsPortada(true);

        // --- IGUALDAD BÁSICA ---
        assertEquals(r1, r1); // Reflexivo
        assertEquals(r1, r2); // Simétrico
        assertEquals(r2, r1);
        assertEquals(r1.hashCode(), r2.hashCode()); // HashCode consistente

        assertNotEquals(r1, null); // No igual a null
        assertNotEquals(r1, new Object()); // No igual a otro tipo

        // --- VARIACIONES CAMPO A CAMPO ---

        // 1. AgrupacionId
        ImagenRequest rDiffAgr = copiar(r1); rDiffAgr.setAgrupacionId(99L);
        assertNotEquals(r1, rDiffAgr);
        assertNotEquals(r1.hashCode(), rDiffAgr.hashCode());
        
        ImagenRequest rAgrNull = copiar(r1); rAgrNull.setAgrupacionId(null);
        assertNotEquals(r1, rAgrNull);
        assertNotEquals(rAgrNull, r1);

        // 2. EsPortada
        ImagenRequest rDiffPortada = copiar(r1); rDiffPortada.setEsPortada(false);
        assertNotEquals(r1, rDiffPortada);
        assertNotEquals(r1.hashCode(), rDiffPortada.hashCode());
        
        ImagenRequest rPortadaNull = copiar(r1); rPortadaNull.setEsPortada(null);
        assertNotEquals(r1, rPortadaNull);
        assertNotEquals(rPortadaNull, r1);
        
        // 3. EsPortada Null vs Null
        ImagenRequest rPortadaNull2 = copiar(r1); rPortadaNull2.setEsPortada(null);
        assertEquals(rPortadaNull, rPortadaNull2);
    }

    // Método auxiliar para clonar
    private ImagenRequest copiar(ImagenRequest original) {
        ImagenRequest copia = new ImagenRequest();
        copia.setAgrupacionId(original.getAgrupacionId());
        copia.setEsPortada(original.getEsPortada());
        return copia;
    }

    @Test
    @DisplayName("Debe generar una representación en String")
    void testToString() {
        ImagenRequest request = new ImagenRequest();
        request.setAgrupacionId(1L);
        
        String s = request.toString();
        assertNotNull(s);
        assertTrue(s.contains("agrupacionId=1"));
    }

    @Test
    @DisplayName("Debe cumplir con canEqual para la simetría de equals")
    void testCanEqual() {
        ImagenRequest r = new ImagenRequest();
        assertTrue(r.canEqual(new ImagenRequest()));
        assertFalse(r.canEqual(new Object()));
    }
}