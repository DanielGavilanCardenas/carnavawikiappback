package org.carnavawiky.back.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VideoTest {

    @Test
    void testGettersAndSetters() {
        Video video = new Video();
        Long id = 1L;
        String titulo = "Actuación Final";
        String urlYoutube = "https://youtube.com/watch?v=12345";
        boolean verificado = true;

        video.setId(id);
        video.setTitulo(titulo);
        video.setUrlYoutube(urlYoutube);
        video.setVerificado(verificado);

        assertEquals(id, video.getId());
        assertEquals(titulo, video.getTitulo());
        assertEquals(urlYoutube, video.getUrlYoutube());
        assertTrue(video.isVerificado());
    }

    @Test
    void testEqualsAndHashCode() {
        Video v1 = new Video();
        v1.setId(1L);
        v1.setTitulo("Video 1");
        v1.setUrlYoutube("url1");
        v1.setVerificado(true);

        Video v2 = new Video();
        v2.setId(1L);
        v2.setTitulo("Video 1");
        v2.setUrlYoutube("url1");
        v2.setVerificado(true);

        // Test igualdad básica
        assertEquals(v1, v2);
        assertEquals(v1.hashCode(), v2.hashCode());
        assertEquals(v1, v1); // Reflexivo

        // Test desigualdad con null y otro tipo
        assertNotEquals(null, v1);
        assertNotEquals("String", v1);

        // Test desigualdad por campos individuales
        Video v3 = new Video();
        v3.setId(2L); // ID diferente
        v3.setTitulo("Video 1");
        v3.setUrlYoutube("url1");
        v3.setVerificado(true);
        assertNotEquals(v1, v3);

        v3.setId(1L);
        v3.setTitulo("Otro Titulo"); // Título diferente
        assertNotEquals(v1, v3);

        v3.setTitulo("Video 1");
        v3.setUrlYoutube("url2"); // URL diferente
        assertNotEquals(v1, v3);

        v3.setUrlYoutube("url1");
        v3.setVerificado(false); // Verificado diferente
        assertNotEquals(v1, v3);
    }

    @Test
    void testToString() {
        Video video = new Video();
        video.setId(1L);
        video.setTitulo("Test Video");
        video.setUrlYoutube("http://test.url");
        video.setVerificado(true);

        String toString = video.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("Video"));
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("titulo=Test Video"));
        assertTrue(toString.contains("urlYoutube=http://test.url"));
        assertTrue(toString.contains("verificado=true"));
    }

    @Test
    void testCanEqual() {
        Video v1 = new Video();
        Video v2 = new Video();

        assertTrue(v1.canEqual(v2));
        assertFalse(v1.canEqual(new Object()));
    }
}
