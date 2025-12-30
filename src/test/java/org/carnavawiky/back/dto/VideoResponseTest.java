package org.carnavawiky.back.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VideoResponseTest {

    @Test
    @DisplayName("Debe verificar que los getters y setters funcionan correctamente")
    void testGettersAndSetters() {
        // Arrange
        VideoResponse response = new VideoResponse();
        Long id = 1L;
        String titulo = "Actuación Final";
        String urlYoutube = "https://youtube.com/watch?v=12345";
        boolean verificado = true;
        Long agrupacionId = 10L;
        String agrupacionNombre = "Los Piratas";

        // Act
        response.setId(id);
        response.setTitulo(titulo);
        response.setUrlYoutube(urlYoutube);
        response.setVerificado(verificado);
        response.setAgrupacionId(agrupacionId);
        response.setAgrupacionNombre(agrupacionNombre);

        // Assert
        assertAll("Verificación de propiedades del DTO",
                () -> assertEquals(id, response.getId()),
                () -> assertEquals(titulo, response.getTitulo()),
                () -> assertEquals(urlYoutube, response.getUrlYoutube()),
                () -> assertTrue(response.isVerificado()),
                () -> assertEquals(agrupacionId, response.getAgrupacionId()),
                () -> assertEquals(agrupacionNombre, response.getAgrupacionNombre())
        );
    }

    @Test
    @DisplayName("Debe funcionar el constructor con todos los argumentos")
    void testAllArgsConstructor() {
        VideoResponse response = new VideoResponse(1L, "Titulo", "URL", true, 10L, "Agrupacion");
        
        assertEquals(1L, response.getId());
        assertEquals("Titulo", response.getTitulo());
        assertEquals("URL", response.getUrlYoutube());
        assertTrue(response.isVerificado());
        assertEquals(10L, response.getAgrupacionId());
        assertEquals("Agrupacion", response.getAgrupacionNombre());
    }

    @Test
    @DisplayName("Debe funcionar el patrón Builder")
    void testBuilder() {
        VideoResponse response = VideoResponse.builder()
                .id(1L)
                .titulo("Titulo")
                .urlYoutube("URL")
                .verificado(true)
                .agrupacionId(10L)
                .agrupacionNombre("Agrupacion")
                .build();

        assertEquals(1L, response.getId());
        assertEquals("Titulo", response.getTitulo());
        assertEquals("URL", response.getUrlYoutube());
        assertTrue(response.isVerificado());
        assertEquals(10L, response.getAgrupacionId());
        assertEquals("Agrupacion", response.getAgrupacionNombre());
    }

    // =======================================================
    // TESTS PARA COBERTURA DE MÉTODOS DE LOMBOK (@Data)
    // =======================================================

    @Test
    @DisplayName("Debe cubrir todas las ramas de equals y hashCode variando cada campo")
    void testEqualsAndHashCode_Exhaustivo() {
        // Objeto base
        VideoResponse r1 = new VideoResponse();
        r1.setId(1L);
        r1.setTitulo("Titulo");
        r1.setUrlYoutube("URL");
        r1.setVerificado(true);
        r1.setAgrupacionId(10L);
        r1.setAgrupacionNombre("Agrupacion");

        // Copia exacta
        VideoResponse r2 = new VideoResponse();
        r2.setId(1L);
        r2.setTitulo("Titulo");
        r2.setUrlYoutube("URL");
        r2.setVerificado(true);
        r2.setAgrupacionId(10L);
        r2.setAgrupacionNombre("Agrupacion");

        // --- IGUALDAD BÁSICA ---
        assertEquals(r1, r1); // Reflexivo
        assertEquals(r1, r2); // Simétrico
        assertEquals(r2, r1);
        assertEquals(r1.hashCode(), r2.hashCode()); // HashCode consistente

        assertNotEquals(r1, null); // No igual a null
        assertNotEquals(r1, new Object()); // No igual a otro tipo

        // --- VARIACIONES CAMPO A CAMPO ---

        // 1. ID
        VideoResponse rDiffId = copiar(r1); rDiffId.setId(2L);
        assertNotEquals(r1, rDiffId);
        assertNotEquals(r1.hashCode(), rDiffId.hashCode());
        
        VideoResponse rIdNull = copiar(r1); rIdNull.setId(null);
        assertNotEquals(r1, rIdNull);
        assertNotEquals(rIdNull, r1);

        // 2. Titulo
        VideoResponse rDiffTitulo = copiar(r1); rDiffTitulo.setTitulo("Otro");
        assertNotEquals(r1, rDiffTitulo);
        assertNotEquals(r1.hashCode(), rDiffTitulo.hashCode());
        
        VideoResponse rTituloNull = copiar(r1); rTituloNull.setTitulo(null);
        assertNotEquals(r1, rTituloNull);
        assertNotEquals(rTituloNull, r1);

        // 3. UrlYoutube
        VideoResponse rDiffUrl = copiar(r1); rDiffUrl.setUrlYoutube("OtraURL");
        assertNotEquals(r1, rDiffUrl);
        assertNotEquals(r1.hashCode(), rDiffUrl.hashCode());
        
        VideoResponse rUrlNull = copiar(r1); rUrlNull.setUrlYoutube(null);
        assertNotEquals(r1, rUrlNull);
        assertNotEquals(rUrlNull, r1);

        // 4. Verificado
        VideoResponse rDiffVerificado = copiar(r1); rDiffVerificado.setVerificado(false);
        assertNotEquals(r1, rDiffVerificado);
        assertNotEquals(r1.hashCode(), rDiffVerificado.hashCode());
        
        // 5. AgrupacionId
        VideoResponse rDiffAgrupId = copiar(r1); rDiffAgrupId.setAgrupacionId(20L);
        assertNotEquals(r1, rDiffAgrupId);
        assertNotEquals(r1.hashCode(), rDiffAgrupId.hashCode());
        
        VideoResponse rAgrupIdNull = copiar(r1); rAgrupIdNull.setAgrupacionId(null);
        assertNotEquals(r1, rAgrupIdNull);
        assertNotEquals(rAgrupIdNull, r1);

        // 6. AgrupacionNombre
        VideoResponse rDiffAgrupNom = copiar(r1); rDiffAgrupNom.setAgrupacionNombre("Otra");
        assertNotEquals(r1, rDiffAgrupNom);
        assertNotEquals(r1.hashCode(), rDiffAgrupNom.hashCode());
        
        VideoResponse rAgrupNomNull = copiar(r1); rAgrupNomNull.setAgrupacionNombre(null);
        assertNotEquals(r1, rAgrupNomNull);
        assertNotEquals(rAgrupNomNull, r1);
        
        // 7. AgrupacionNombre Null vs Null
        VideoResponse rAgrupNomNull2 = copiar(r1); rAgrupNomNull2.setAgrupacionNombre(null);
        assertEquals(rAgrupNomNull, rAgrupNomNull2);
    }

    // Método auxiliar para clonar
    private VideoResponse copiar(VideoResponse original) {
        VideoResponse copia = new VideoResponse();
        copia.setId(original.getId());
        copia.setTitulo(original.getTitulo());
        copia.setUrlYoutube(original.getUrlYoutube());
        copia.setVerificado(original.isVerificado());
        copia.setAgrupacionId(original.getAgrupacionId());
        copia.setAgrupacionNombre(original.getAgrupacionNombre());
        return copia;
    }

    @Test
    @DisplayName("Debe generar una representación en String")
    void testToString() {
        VideoResponse response = new VideoResponse();
        response.setTitulo("Test");
        
        String s = response.toString();
        assertNotNull(s);
        assertTrue(s.contains("titulo=Test"));
        assertTrue(s.contains("VideoResponse"));
    }

    @Test
    @DisplayName("Debe cumplir con canEqual para la simetría de equals")
    void testCanEqual() {
        VideoResponse r = new VideoResponse();
        assertTrue(r.canEqual(new VideoResponse()));
        assertFalse(r.canEqual(new Object()));
    }
}
