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
        imagen.setId(1L);
        imagen.setNombreFichero("foto.jpg");
    }

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        // Establecer valores
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

    // =======================================================
    // TESTS PARA COBERTURA DE MÉTODOS DE LOMBOK (@Data)
    // =======================================================

    @Test
    @DisplayName("Debe cumplir con el contrato de equals y hashCode")
    void testEqualsAndHashCode() {
        Imagen imagen1 = new Imagen();
        imagen1.setId(1L);
        imagen1.setNombreFichero("foto1.jpg");

        Imagen imagen2 = new Imagen();
        imagen2.setId(1L);
        imagen2.setNombreFichero("foto1.jpg");

        Imagen imagen3 = new Imagen();
        imagen3.setId(2L);
        imagen3.setNombreFichero("foto2.jpg");

        // Reflexividad
        assertEquals(imagen1, imagen1);

        // Simetría
        assertEquals(imagen1, imagen2);
        assertEquals(imagen2, imagen1);

        // Consistencia de hashCode
        assertEquals(imagen1.hashCode(), imagen2.hashCode());

        // Desigualdad
        assertNotEquals(imagen1, imagen3);
        assertNotEquals(imagen1.hashCode(), imagen3.hashCode());

        // Comparación con nulo y otros tipos
        assertNotEquals(null, imagen1);
        assertNotEquals(imagen1, new Object());
    }

    @Test
    @DisplayName("Debe generar una representación en String")
    void testToString() {
        String imagenString = imagen.toString();
        assertNotNull(imagenString);
        assertTrue(imagenString.contains("nombreFichero=foto.jpg"));
        assertTrue(imagenString.contains("id=1"));
    }

    @Test
    @DisplayName("Debe cumplir con canEqual para la simetría de equals")
    void testCanEqual() {
        Imagen otraImagen = new Imagen();
        assertTrue(imagen.canEqual(otraImagen));
        assertFalse(imagen.canEqual(new Object()));
    }
}