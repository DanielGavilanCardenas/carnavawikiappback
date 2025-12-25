package org.carnavawiky.back.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonaTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        Persona persona = new Persona();
        Long id = 1L;
        String nombreReal = "Juan Pérez";
        String apodo = "El Juani";
        Localidad localidad = new Localidad(1L, "Cádiz");
        Usuario usuario = new Usuario();
        usuario.setId(10L);

        persona.setId(id);
        persona.setNombreReal(nombreReal);
        persona.setApodo(apodo);
        persona.setOrigen(localidad);
        persona.setUsuario(usuario);

        assertEquals(id, persona.getId());
        assertEquals(nombreReal, persona.getNombreReal());
        assertEquals(apodo, persona.getApodo());
        assertEquals(localidad, persona.getOrigen());
        assertEquals(usuario, persona.getUsuario());
    }

    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        String nombreReal = "Juan Pérez";
        String apodo = "El Juani";
        Localidad localidad = new Localidad(1L, "Cádiz");
        Usuario usuario = new Usuario();
        usuario.setId(10L);

        Persona persona = new Persona(id, nombreReal, apodo, localidad, usuario);

        assertEquals(id, persona.getId());
        assertEquals(nombreReal, persona.getNombreReal());
        assertEquals(apodo, persona.getApodo());
        assertEquals(localidad, persona.getOrigen());
        assertEquals(usuario, persona.getUsuario());
    }

    @Test
    void testEqualsAndHashCode() {
        Localidad localidad1 = new Localidad(1L, "Cádiz");
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);

        Persona p1 = new Persona(1L, "Juan", "Juani", localidad1, usuario1);
        Persona p2 = new Persona(1L, "Juan", "Juani", localidad1, usuario1);

        // Test igualdad básica
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
        assertEquals(p1, p1); // Reflexivo

        // Test desigualdad con null y otro tipo
        assertNotEquals(null, p1);
        assertNotEquals("String", p1);

        // Test desigualdad por campos individuales
        Persona p3 = new Persona(2L, "Juan", "Juani", localidad1, usuario1); // ID diferente
        assertNotEquals(p1, p3);

        p3 = new Persona(1L, "Pedro", "Juani", localidad1, usuario1); // Nombre diferente
        assertNotEquals(p1, p3);

        p3 = new Persona(1L, "Juan", "Perico", localidad1, usuario1); // Apodo diferente
        assertNotEquals(p1, p3);

        Localidad localidad2 = new Localidad(2L, "Sevilla");
        p3 = new Persona(1L, "Juan", "Juani", localidad2, usuario1); // Localidad diferente
        assertNotEquals(p1, p3);

        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        p3 = new Persona(1L, "Juan", "Juani", localidad1, usuario2); // Usuario diferente
        assertNotEquals(p1, p3);
    }

    @Test
    void testToString() {
        Persona persona = new Persona();
        persona.setId(1L);
        persona.setNombreReal("Juan");
        persona.setApodo("Juani");

        String toString = persona.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("Persona"));
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("nombreReal=Juan"));
        assertTrue(toString.contains("apodo=Juani"));
    }

    @Test
    void testCanEqual() {
        Persona p1 = new Persona();
        Persona p2 = new Persona();

        assertTrue(p1.canEqual(p2));
        assertFalse(p1.canEqual(new Object()));
    }
}