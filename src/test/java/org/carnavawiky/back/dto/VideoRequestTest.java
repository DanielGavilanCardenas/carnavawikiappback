package org.carnavawiky.back.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VideoRequestTest {

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        VideoRequest request = new VideoRequest();
        
        request.setTitulo("Actuación Final");
        request.setUrlYoutube("https://youtube.com/watch?v=12345");
        request.setAgrupacionId(10L);

        assertNotNull(request);
        assertEquals("Actuación Final", request.getTitulo());
        assertEquals("https://youtube.com/watch?v=12345", request.getUrlYoutube());
        assertEquals(10L, request.getAgrupacionId());
    }

    @Test
    @DisplayName("Debe funcionar el constructor con todos los argumentos")
    void testAllArgsConstructor() {
        VideoRequest request = new VideoRequest("Actuación Final", "https://youtube.com/watch?v=12345", 10L);
        
        assertEquals("Actuación Final", request.getTitulo());
        assertEquals("https://youtube.com/watch?v=12345", request.getUrlYoutube());
        assertEquals(10L, request.getAgrupacionId());
    }

    // =======================================================
    // TESTS PARA COBERTURA DE MÉTODOS DE LOMBOK (@Data)
    // =======================================================

    @Test
    @DisplayName("Debe cubrir todas las ramas de equals y hashCode variando cada campo")
    void testEqualsAndHashCode_Exhaustivo() {
        // Objeto base
        VideoRequest r1 = new VideoRequest();
        r1.setTitulo("Titulo");
        r1.setUrlYoutube("URL");
        r1.setAgrupacionId(10L);

        // Copia exacta
        VideoRequest r2 = new VideoRequest();
        r2.setTitulo("Titulo");
        r2.setUrlYoutube("URL");
        r2.setAgrupacionId(10L);

        // --- IGUALDAD BÁSICA ---
        assertEquals(r1, r1); // Reflexivo
        assertEquals(r1, r2); // Simétrico
        assertEquals(r2, r1);
        assertEquals(r1.hashCode(), r2.hashCode()); // HashCode consistente

        assertNotEquals(r1, null); // No igual a null
        assertNotEquals(r1, new Object()); // No igual a otro tipo

        // --- VARIACIONES CAMPO A CAMPO ---

        // 1. Titulo
        VideoRequest rDiffTitulo = copiar(r1); rDiffTitulo.setTitulo("Otro");
        assertNotEquals(r1, rDiffTitulo);
        assertNotEquals(r1.hashCode(), rDiffTitulo.hashCode());
        
        VideoRequest rTituloNull = copiar(r1); rTituloNull.setTitulo(null);
        assertNotEquals(r1, rTituloNull);
        assertNotEquals(rTituloNull, r1);

        // 2. UrlYoutube
        VideoRequest rDiffUrl = copiar(r1); rDiffUrl.setUrlYoutube("OtraURL");
        assertNotEquals(r1, rDiffUrl);
        assertNotEquals(r1.hashCode(), rDiffUrl.hashCode());
        
        VideoRequest rUrlNull = copiar(r1); rUrlNull.setUrlYoutube(null);
        assertNotEquals(r1, rUrlNull);
        assertNotEquals(rUrlNull, r1);
        
        // 3. AgrupacionId
        VideoRequest rDiffAgrup = copiar(r1); rDiffAgrup.setAgrupacionId(20L);
        assertNotEquals(r1, rDiffAgrup);
        assertNotEquals(r1.hashCode(), rDiffAgrup.hashCode());
        
        VideoRequest rAgrupNull = copiar(r1); rAgrupNull.setAgrupacionId(null);
        assertNotEquals(r1, rAgrupNull);
        assertNotEquals(rAgrupNull, r1);
        
        // 4. AgrupacionId Null vs Null
        VideoRequest rAgrupNull2 = copiar(r1); rAgrupNull2.setAgrupacionId(null);
        assertEquals(rAgrupNull, rAgrupNull2);
    }

    // Método auxiliar para clonar
    private VideoRequest copiar(VideoRequest original) {
        VideoRequest copia = new VideoRequest();
        copia.setTitulo(original.getTitulo());
        copia.setUrlYoutube(original.getUrlYoutube());
        copia.setAgrupacionId(original.getAgrupacionId());
        return copia;
    }

    @Test
    @DisplayName("Debe generar una representación en String")
    void testToString() {
        VideoRequest request = new VideoRequest();
        request.setTitulo("Test");
        
        String s = request.toString();
        assertNotNull(s);
        assertTrue(s.contains("titulo=Test"));
    }

    @Test
    @DisplayName("Debe cumplir con canEqual para la simetría de equals")
    void testCanEqual() {
        VideoRequest r = new VideoRequest();
        assertTrue(r.canEqual(new VideoRequest()));
        assertFalse(r.canEqual(new Object()));
    }
}
