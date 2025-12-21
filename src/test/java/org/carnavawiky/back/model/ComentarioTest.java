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
        comentario.setId(1L);
        comentario.setContenido("Un comentario de prueba.");
    }

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        LocalDateTime fecha = LocalDateTime.now();

        // Establecer valores
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

    // =======================================================
    // TESTS PARA COBERTURA DE MÉTODOS DE LOMBOK (@Data)
    // =======================================================

    @Test
    @DisplayName("Debe cumplir con el contrato de equals y hashCode")
    void testEqualsAndHashCode() {
        Comentario comentario1 = new Comentario();
        comentario1.setId(1L);
        comentario1.setContenido("Comentario A");

        Comentario comentario2 = new Comentario();
        comentario2.setId(1L);
        comentario2.setContenido("Comentario A");

        Comentario comentario3 = new Comentario();
        comentario3.setId(2L);
        comentario3.setContenido("Comentario B");

        // Reflexividad
        assertEquals(comentario1, comentario1);

        // Simetría
        assertEquals(comentario1, comentario2);
        assertEquals(comentario2, comentario1);

        // Consistencia de hashCode
        assertEquals(comentario1.hashCode(), comentario2.hashCode());

        // Desigualdad
        assertNotEquals(comentario1, comentario3);
        assertNotEquals(comentario1.hashCode(), comentario3.hashCode());

        // Comparación con nulo y otros tipos
        assertNotEquals(null, comentario1);
        assertNotEquals(comentario1, new Object());
    }

    @Test
    @DisplayName("Debe generar una representación en String")
    void testToString() {
        String comentarioString = comentario.toString();
        assertNotNull(comentarioString);
        assertTrue(comentarioString.contains("contenido=Un comentario de prueba."));
        assertTrue(comentarioString.contains("id=1"));
    }

    @Test
    @DisplayName("Debe cumplir con canEqual para la simetría de equals")
    void testCanEqual() {
        Comentario otroComentario = new Comentario();
        assertTrue(comentario.canEqual(otroComentario));
        assertFalse(comentario.canEqual(new Object()));
    }
}