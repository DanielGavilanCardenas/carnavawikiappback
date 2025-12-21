package org.carnavawiky.back.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImagenTest {

    private Imagen imagen;
    private Agrupacion agrupacion;

    @BeforeEach
    void setUp() {
        // Objeto de apoyo
        agrupacion = new Agrupacion();
        agrupacion.setId(10L);
        agrupacion.setNombre("Los Piratas");

        // Entidad a probar
        imagen = new Imagen();
    }

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        // Establecer valores
        imagen.setId(1L);
        imagen.setNombreFichero("foto.jpg");
        imagen.setRutaAbsoluta("/var/www/uploads/foto.jpg");
        imagen.setUrlPublica("http://localhost:8080/images/foto.jpg");
        imagen.setEsPortada(true);
        imagen.setAgrupacion(agrupacion);

        // Verificar valores
        assertNotNull(imagen);
        assertEquals(1L, imagen.getId());
        assertEquals("foto.jpg", imagen.getNombreFichero());
        assertEquals("/var/www/uploads/foto.jpg", imagen.getRutaAbsoluta());
        assertEquals("http://localhost:8080/images/foto.jpg", imagen.getUrlPublica());
        assertTrue(imagen.getEsPortada());
        assertEquals(agrupacion, imagen.getAgrupacion());
    }

    @Test
    @DisplayName("Debe tener el campo 'esPortada' como false por defecto")
    void testDefaultValues() {
        Imagen nuevaImagen = new Imagen();
        assertNotNull(nuevaImagen);
        assertFalse(nuevaImagen.getEsPortada(), "El campo 'esPortada' debería ser false por defecto.");
    }
}