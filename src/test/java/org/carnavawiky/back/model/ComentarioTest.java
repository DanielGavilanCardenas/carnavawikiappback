package org.carnavawiky.back.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ComentarioTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        Comentario comentario = new Comentario();
        Long id = 1L;
        String contenido = "Comentario de prueba";
        Integer puntuacion = 5;
        Boolean aprobado = true;
        LocalDateTime fechaCreacion = LocalDateTime.now();
        Usuario usuario = new Usuario();
        usuario.setId(10L);
        Agrupacion agrupacion = new Agrupacion();
        agrupacion.setId(20L);

        comentario.setId(id);
        comentario.setContenido(contenido);
        comentario.setPuntuacion(puntuacion);
        comentario.setAprobado(aprobado);
        comentario.setFechaCreacion(fechaCreacion);
        comentario.setUsuario(usuario);
        comentario.setAgrupacion(agrupacion);

        assertEquals(id, comentario.getId());
        assertEquals(contenido, comentario.getContenido());
        assertEquals(puntuacion, comentario.getPuntuacion());
        assertEquals(aprobado, comentario.getAprobado());
        assertEquals(fechaCreacion, comentario.getFechaCreacion());
        assertEquals(usuario, comentario.getUsuario());
        assertEquals(agrupacion, comentario.getAgrupacion());
    }

    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        String contenido = "Comentario de prueba";
        Integer puntuacion = 5;
        Boolean aprobado = true;
        LocalDateTime fechaCreacion = LocalDateTime.now();
        Usuario usuario = new Usuario();
        usuario.setId(10L);
        Agrupacion agrupacion = new Agrupacion();
        agrupacion.setId(20L);

        Comentario comentario = new Comentario(id, contenido, puntuacion, aprobado, fechaCreacion, usuario, agrupacion);

        assertEquals(id, comentario.getId());
        assertEquals(contenido, comentario.getContenido());
        assertEquals(puntuacion, comentario.getPuntuacion());
        assertEquals(aprobado, comentario.getAprobado());
        assertEquals(fechaCreacion, comentario.getFechaCreacion());
        assertEquals(usuario, comentario.getUsuario());
        assertEquals(agrupacion, comentario.getAgrupacion());
    }

    @Test
    void testEqualsAndHashCode() {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        Agrupacion agrupacion1 = new Agrupacion();
        agrupacion1.setId(1L);
        LocalDateTime fecha1 = LocalDateTime.of(2024, 1, 1, 10, 0);

        Comentario c1 = new Comentario(1L, "Contenido", 5, true, fecha1, usuario1, agrupacion1);
        Comentario c2 = new Comentario(1L, "Contenido", 5, true, fecha1, usuario1, agrupacion1);

        // Test igualdad básica
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
        assertEquals(c1, c1); // Reflexivo

        // Test desigualdad con null y otro tipo
        assertNotEquals(null, c1);
        assertNotEquals("String", c1);

        // Test desigualdad por campos individuales
        Comentario c3 = new Comentario(2L, "Contenido", 5, true, fecha1, usuario1, agrupacion1); // ID diferente
        assertNotEquals(c1, c3);

        c3 = new Comentario(1L, "Otro Contenido", 5, true, fecha1, usuario1, agrupacion1); // Contenido diferente
        assertNotEquals(c1, c3);

        c3 = new Comentario(1L, "Contenido", 4, true, fecha1, usuario1, agrupacion1); // Puntuación diferente
        assertNotEquals(c1, c3);

        c3 = new Comentario(1L, "Contenido", 5, false, fecha1, usuario1, agrupacion1); // Aprobado diferente
        assertNotEquals(c1, c3);

        c3 = new Comentario(1L, "Contenido", 5, true, LocalDateTime.now(), usuario1, agrupacion1); // Fecha diferente
        assertNotEquals(c1, c3);

        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        c3 = new Comentario(1L, "Contenido", 5, true, fecha1, usuario2, agrupacion1); // Usuario diferente
        assertNotEquals(c1, c3);

        Agrupacion agrupacion2 = new Agrupacion();
        agrupacion2.setId(2L);
        c3 = new Comentario(1L, "Contenido", 5, true, fecha1, usuario1, agrupacion2); // Agrupación diferente
        assertNotEquals(c1, c3);
    }

    @Test
    void testToString() {
        Comentario comentario = new Comentario();
        comentario.setId(1L);
        comentario.setContenido("Test");
        comentario.setPuntuacion(10);
        comentario.setAprobado(false);

        String toString = comentario.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("Comentario"));
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("contenido=Test"));
        assertTrue(toString.contains("puntuacion=10"));
        assertTrue(toString.contains("aprobado=false"));
    }

    @Test
    void testCanEqual() {
        Comentario c1 = new Comentario();
        Comentario c2 = new Comentario();

        assertTrue(c1.canEqual(c2));
        assertFalse(c1.canEqual(new Object()));
    }
}