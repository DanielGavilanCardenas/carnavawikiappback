package org.carnavawiky.back.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AgrupacionTest {

    @Test
    void testGettersAndSetters() {
        Agrupacion agrupacion = new Agrupacion();
        Long id = 1L;
        String nombre = "Los Carnavales";
        Integer anho = 2024;
        String descripcion = "Una gran agrupación";
        Modalidad modalidad = Modalidad.CHIRIGOTA;
        LocalDateTime fechaAlta = LocalDateTime.now();
        Usuario usuario = new Usuario();
        usuario.setId(10L);
        Localidad localidad = new Localidad(5L, "Cádiz");

        agrupacion.setId(id);
        agrupacion.setNombre(nombre);
        agrupacion.setAnho(anho);
        agrupacion.setDescripcion(descripcion);
        agrupacion.setModalidad(modalidad);
        agrupacion.setFechaAlta(fechaAlta);
        agrupacion.setUsuarioCreador(usuario);
        agrupacion.setLocalidad(localidad);

        assertEquals(id, agrupacion.getId());
        assertEquals(nombre, agrupacion.getNombre());
        assertEquals(anho, agrupacion.getAnho());
        assertEquals(descripcion, agrupacion.getDescripcion());
        assertEquals(modalidad, agrupacion.getModalidad());
        assertEquals(fechaAlta, agrupacion.getFechaAlta());
        assertEquals(usuario, agrupacion.getUsuarioCreador());
        assertEquals(localidad, agrupacion.getLocalidad());
    }

    @Test
    void testEqualsAndHashCode() {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        Localidad localidad1 = new Localidad(1L, "Cádiz");
        LocalDateTime fecha1 = LocalDateTime.of(2024, 1, 1, 10, 0);

        Agrupacion a1 = new Agrupacion();
        a1.setId(1L);
        a1.setNombre("Nombre");
        a1.setAnho(2024);
        a1.setDescripcion("Desc");
        a1.setModalidad(Modalidad.COMPARSA);
        a1.setFechaAlta(fecha1);
        a1.setUsuarioCreador(usuario1);
        a1.setLocalidad(localidad1);

        Agrupacion a2 = new Agrupacion();
        a2.setId(1L);
        a2.setNombre("Nombre");
        a2.setAnho(2024);
        a2.setDescripcion("Desc");
        a2.setModalidad(Modalidad.COMPARSA);
        a2.setFechaAlta(fecha1);
        a2.setUsuarioCreador(usuario1);
        a2.setLocalidad(localidad1);

        // Test igualdad básica
        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
        assertEquals(a1, a1); // Reflexivo

        // Test desigualdad con null y otro tipo
        assertNotEquals(null, a1);
        assertNotEquals("String", a1);

        // Test desigualdad por campos individuales
        Agrupacion a3 = new Agrupacion();
        a3.setId(2L); // ID diferente
        a3.setNombre("Nombre");
        a3.setAnho(2024);
        a3.setDescripcion("Desc");
        a3.setModalidad(Modalidad.COMPARSA);
        a3.setFechaAlta(fecha1);
        a3.setUsuarioCreador(usuario1);
        a3.setLocalidad(localidad1);
        assertNotEquals(a1, a3);

        a3.setId(1L);
        a3.setNombre("Otro Nombre"); // Nombre diferente
        assertNotEquals(a1, a3);

        a3.setNombre("Nombre");
        a3.setAnho(2023); // Año diferente
        assertNotEquals(a1, a3);

        a3.setAnho(2024);
        a3.setDescripcion("Otra Desc"); // Descripción diferente
        assertNotEquals(a1, a3);

        a3.setDescripcion("Desc");
        a3.setModalidad(Modalidad.CORO); // Modalidad diferente
        assertNotEquals(a1, a3);

        a3.setModalidad(Modalidad.COMPARSA);
        a3.setFechaAlta(LocalDateTime.now()); // Fecha diferente
        assertNotEquals(a1, a3);

        a3.setFechaAlta(fecha1);
        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        a3.setUsuarioCreador(usuario2); // Usuario diferente
        assertNotEquals(a1, a3);

        a3.setUsuarioCreador(usuario1);
        Localidad localidad2 = new Localidad(2L, "Sevilla");
        a3.setLocalidad(localidad2); // Localidad diferente
        assertNotEquals(a1, a3);
    }

    @Test
    void testToString() {
        Agrupacion agrupacion = new Agrupacion();
        agrupacion.setId(1L);
        agrupacion.setNombre("Test Agrupacion");
        agrupacion.setAnho(2024);
        agrupacion.setModalidad(Modalidad.CHIRIGOTA);

        String toString = agrupacion.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("Agrupacion"));
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("nombre=Test Agrupacion"));
        assertTrue(toString.contains("anho=2024"));
        assertTrue(toString.contains("modalidad=CHIRIGOTA"));
    }

    @Test
    void testCanEqual() {
        Agrupacion a1 = new Agrupacion();
        Agrupacion a2 = new Agrupacion();

        assertTrue(a1.canEqual(a2));
        assertFalse(a1.canEqual(new Object()));
    }
}