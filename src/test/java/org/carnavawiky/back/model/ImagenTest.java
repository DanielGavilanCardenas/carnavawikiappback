package org.carnavawiky.back.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImagenTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        Imagen imagen = new Imagen();
        Long id = 1L;
        String nombreFichero = "foto.jpg";
        String rutaAbsoluta = "/var/www/images/foto.jpg";
        String urlPublica = "http://localhost/images/foto.jpg";
        Boolean esPortada = true;
        Agrupacion agrupacion = new Agrupacion();
        agrupacion.setId(10L);

        imagen.setId(id);
        imagen.setNombreFichero(nombreFichero);
        imagen.setRutaAbsoluta(rutaAbsoluta);
        imagen.setUrlPublica(urlPublica);
        imagen.setEsPortada(esPortada);
        imagen.setAgrupacion(agrupacion);

        assertEquals(id, imagen.getId());
        assertEquals(nombreFichero, imagen.getNombreFichero());
        assertEquals(rutaAbsoluta, imagen.getRutaAbsoluta());
        assertEquals(urlPublica, imagen.getUrlPublica());
        assertEquals(esPortada, imagen.getEsPortada());
        assertEquals(agrupacion, imagen.getAgrupacion());
    }

    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        String nombreFichero = "foto.jpg";
        String rutaAbsoluta = "/var/www/images/foto.jpg";
        String urlPublica = "http://localhost/images/foto.jpg";
        Boolean esPortada = true;
        Agrupacion agrupacion = new Agrupacion();
        agrupacion.setId(10L);

        Imagen imagen = new Imagen(id, nombreFichero, rutaAbsoluta, urlPublica, esPortada, agrupacion);

        assertEquals(id, imagen.getId());
        assertEquals(nombreFichero, imagen.getNombreFichero());
        assertEquals(rutaAbsoluta, imagen.getRutaAbsoluta());
        assertEquals(urlPublica, imagen.getUrlPublica());
        assertEquals(esPortada, imagen.getEsPortada());
        assertEquals(agrupacion, imagen.getAgrupacion());
    }

    @Test
    void testEqualsAndHashCode() {
        Agrupacion agrupacion1 = new Agrupacion();
        agrupacion1.setId(1L);

        Imagen i1 = new Imagen(1L, "foto.jpg", "/path/foto.jpg", "http://url/foto.jpg", true, agrupacion1);
        Imagen i2 = new Imagen(1L, "foto.jpg", "/path/foto.jpg", "http://url/foto.jpg", true, agrupacion1);

        // Test igualdad básica
        assertEquals(i1, i2);
        assertEquals(i1.hashCode(), i2.hashCode());
        assertEquals(i1, i1); // Reflexivo

        // Test desigualdad con null y otro tipo
        assertNotEquals(null, i1);
        assertNotEquals("String", i1);

        // Test desigualdad por campos individuales
        Imagen i3 = new Imagen(2L, "foto.jpg", "/path/foto.jpg", "http://url/foto.jpg", true, agrupacion1); // ID diferente
        assertNotEquals(i1, i3);

        i3 = new Imagen(1L, "otra.jpg", "/path/foto.jpg", "http://url/foto.jpg", true, agrupacion1); // Nombre diferente
        assertNotEquals(i1, i3);

        i3 = new Imagen(1L, "foto.jpg", "/path/otra.jpg", "http://url/foto.jpg", true, agrupacion1); // Ruta diferente
        assertNotEquals(i1, i3);

        i3 = new Imagen(1L, "foto.jpg", "/path/foto.jpg", "http://url/otra.jpg", true, agrupacion1); // URL diferente
        assertNotEquals(i1, i3);

        i3 = new Imagen(1L, "foto.jpg", "/path/foto.jpg", "http://url/foto.jpg", false, agrupacion1); // EsPortada diferente
        assertNotEquals(i1, i3);

        Agrupacion agrupacion2 = new Agrupacion();
        agrupacion2.setId(2L);
        i3 = new Imagen(1L, "foto.jpg", "/path/foto.jpg", "http://url/foto.jpg", true, agrupacion2); // Agrupación diferente
        assertNotEquals(i1, i3);
    }

    @Test
    void testToString() {
        Imagen imagen = new Imagen();
        imagen.setId(1L);
        imagen.setNombreFichero("foto.jpg");
        imagen.setEsPortada(true);

        String toString = imagen.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("Imagen"));
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("nombreFichero=foto.jpg"));
        assertTrue(toString.contains("esPortada=true"));
    }

    @Test
    void testCanEqual() {
        Imagen i1 = new Imagen();
        Imagen i2 = new Imagen();

        assertTrue(i1.canEqual(i2));
        assertFalse(i1.canEqual(new Object()));
    }
}