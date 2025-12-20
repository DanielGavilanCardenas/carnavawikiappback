package org.carnavawiky.back.mapper;

import org.carnavawiky.back.dto.ImagenResponse;
import org.carnavawiky.back.model.Agrupacion;
import org.carnavawiky.back.model.Imagen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImagenMapperTest {

    private ImagenMapper imagenMapper;
    private Agrupacion agrupacion;

    @BeforeEach
    void setUp() {
        imagenMapper = new ImagenMapper();

        // Configuración de objetos de apoyo
        agrupacion = new Agrupacion();
        agrupacion.setId(10L);
        agrupacion.setNombre("Los Curanderos");
    }

    @Test
    @DisplayName("Debe mapear de Entidad a Response con todas sus relaciones")
    void testToResponse_Completo() {
        Imagen imagen = new Imagen();
        imagen.setId(1L);
        imagen.setNombreFichero("foto.jpg");
        imagen.setUrlPublica("/images/foto.jpg");
        imagen.setEsPortada(true);
        imagen.setAgrupacion(agrupacion);

        ImagenResponse response = imagenMapper.toResponse(imagen);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("foto.jpg", response.getNombreFichero());
        assertEquals("/images/foto.jpg", response.getUrlPublica());
        assertTrue(response.getEsPortada());

        // Verificación de Agrupación
        assertEquals(10L, response.getAgrupacionId());
        assertEquals("Los Curanderos", response.getAgrupacionNombre());
    }

    @Test
    @DisplayName("Debe manejar nulos en las relaciones al mapear a Response")
    void testToResponse_ConRelacionesNulas() {
        Imagen imagen = new Imagen();
        imagen.setId(5L);
        imagen.setNombreFichero("sin_agrupacion.jpg");
        imagen.setUrlPublica("/images/sin_agrupacion.jpg");
        imagen.setEsPortada(false);
        // Dejamos agrupacion como null

        ImagenResponse response = imagenMapper.toResponse(imagen);

        assertNotNull(response);
        assertEquals(5L, response.getId());
        assertEquals("sin_agrupacion.jpg", response.getNombreFichero());
        assertFalse(response.getEsPortada());
        
        assertNull(response.getAgrupacionId());
        assertNull(response.getAgrupacionNombre());
    }
}