package org.carnavawiky.back.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComentarioRequestTest {

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        ComentarioRequest request = new ComentarioRequest();
        
        request.setContenido("Comentario");
        request.setPuntuacion(5);
        request.setAgrupacionId(10L);
        request.setUsuarioId(20L);

        assertNotNull(request);
        assertEquals("Comentario", request.getContenido());
        assertEquals(5, request.getPuntuacion());
        assertEquals(10L, request.getAgrupacionId());
        assertEquals(20L, request.getUsuarioId());
    }

    // =======================================================
    // TESTS PARA COBERTURA DE MÉTODOS DE LOMBOK (@Data)
    // =======================================================

    @Test
    @DisplayName("Debe cubrir todas las ramas de equals y hashCode variando cada campo")
    void testEqualsAndHashCode_Exhaustivo() {
        // Objeto base
        ComentarioRequest r1 = new ComentarioRequest();
        r1.setContenido("Contenido");
        r1.setPuntuacion(5);
        r1.setAgrupacionId(10L);
        r1.setUsuarioId(20L);

        // Copia exacta
        ComentarioRequest r2 = new ComentarioRequest();
        r2.setContenido("Contenido");
        r2.setPuntuacion(5);
        r2.setAgrupacionId(10L);
        r2.setUsuarioId(20L);

        // --- IGUALDAD BÁSICA ---
        assertEquals(r1, r1); // Reflexivo
        assertEquals(r1, r2); // Simétrico
        assertEquals(r2, r1);
        assertEquals(r1.hashCode(), r2.hashCode()); // HashCode consistente

        assertNotEquals(r1, null); // No igual a null
        assertNotEquals(r1, new Object()); // No igual a otro tipo

        // --- VARIACIONES CAMPO A CAMPO ---

        // 1. Contenido
        ComentarioRequest rDiffCont = copiar(r1); rDiffCont.setContenido("Otro");
        assertNotEquals(r1, rDiffCont);
        assertNotEquals(r1.hashCode(), rDiffCont.hashCode());
        
        ComentarioRequest rContNull = copiar(r1); rContNull.setContenido(null);
        assertNotEquals(r1, rContNull);
        assertNotEquals(rContNull, r1);

        // 2. Puntuacion
        ComentarioRequest rDiffPunt = copiar(r1); rDiffPunt.setPuntuacion(1);
        assertNotEquals(r1, rDiffPunt);
        assertNotEquals(r1.hashCode(), rDiffPunt.hashCode());
        
        ComentarioRequest rPuntNull = copiar(r1); rPuntNull.setPuntuacion(null);
        assertNotEquals(r1, rPuntNull);
        assertNotEquals(rPuntNull, r1);

        // 3. AgrupacionId
        ComentarioRequest rDiffAgr = copiar(r1); rDiffAgr.setAgrupacionId(99L);
        assertNotEquals(r1, rDiffAgr);
        assertNotEquals(r1.hashCode(), rDiffAgr.hashCode());
        
        ComentarioRequest rAgrNull = copiar(r1); rAgrNull.setAgrupacionId(null);
        assertNotEquals(r1, rAgrNull);
        assertNotEquals(rAgrNull, r1);

        // 4. UsuarioId
        ComentarioRequest rDiffUser = copiar(r1); rDiffUser.setUsuarioId(99L);
        assertNotEquals(r1, rDiffUser);
        assertNotEquals(r1.hashCode(), rDiffUser.hashCode());
        
        ComentarioRequest rUserNull = copiar(r1); rUserNull.setUsuarioId(null);
        assertNotEquals(r1, rUserNull);
        assertNotEquals(rUserNull, r1);
        
        // 5. UsuarioId Null vs Null
        ComentarioRequest rUserNull2 = copiar(r1); rUserNull2.setUsuarioId(null);
        assertEquals(rUserNull, rUserNull2);
    }

    // Método auxiliar para clonar
    private ComentarioRequest copiar(ComentarioRequest original) {
        ComentarioRequest copia = new ComentarioRequest();
        copia.setContenido(original.getContenido());
        copia.setPuntuacion(original.getPuntuacion());
        copia.setAgrupacionId(original.getAgrupacionId());
        copia.setUsuarioId(original.getUsuarioId());
        return copia;
    }

    @Test
    @DisplayName("Debe generar una representación en String")
    void testToString() {
        ComentarioRequest request = new ComentarioRequest();
        request.setContenido("Test");
        
        String s = request.toString();
        assertNotNull(s);
        assertTrue(s.contains("contenido=Test"));
    }

    @Test
    @DisplayName("Debe cumplir con canEqual para la simetría de equals")
    void testCanEqual() {
        ComentarioRequest r = new ComentarioRequest();
        assertTrue(r.canEqual(new ComentarioRequest()));
        assertFalse(r.canEqual(new Object()));
    }
}