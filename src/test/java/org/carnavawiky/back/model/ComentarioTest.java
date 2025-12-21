package org.carnavawiky.back.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ComentarioTest {

    private Comentario comentario;
    private Usuario usuario;
    private Agrupacion agrupacion;

    @BeforeEach
    void setUp() {
        // Objetos de apoyo
        usuario = new Usuario();
        usuario.setId(10L);
        usuario.setUsername("testuser");

        agrupacion = new Agrupacion();
        agrupacion.setId(20L);
        agrupacion.setNombre("Los Piratas");

        // Entidad a probar
        comentario = new Comentario();
    }

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        LocalDateTime fecha = LocalDateTime.now();

        // Establecer valores
        comentario.setId(1L);
        comentario.setContenido("Un comentario de prueba.");
        comentario.setPuntuacion(5);
        comentario.setAprobado(true);
        comentario.setFechaCreacion(fecha);
        comentario.setUsuario(usuario);
        comentario.setAgrupacion(agrupacion);

        // Verificar valores
        assertNotNull(comentario);
        assertEquals(1L, comentario.getId());
        assertEquals("Un comentario de prueba.", comentario.getContenido());
        assertEquals(5, comentario.getPuntuacion());
        assertTrue(comentario.getAprobado());
        assertEquals(fecha, comentario.getFechaCreacion());
        assertEquals(usuario, comentario.getUsuario());
        assertEquals(agrupacion, comentario.getAgrupacion());
    }

    @Test
    @DisplayName("Debe tener el campo 'aprobado' como false por defecto")
    void testDefaultValues() {
        Comentario nuevoComentario = new Comentario();
        assertNotNull(nuevoComentario);
        assertFalse(nuevoComentario.getAprobado(), "El campo 'aprobado' debería ser false por defecto.");
    }
}