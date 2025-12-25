package org.carnavawiky.back.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PremioTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        Premio premio = new Premio();
        Long id = 1L;
        Integer puesto = 1;
        Modalidad modalidad = Modalidad.COMPARSA;
        Agrupacion agrupacion = new Agrupacion();
        agrupacion.setId(10L);
        Edicion edicion = new Edicion();
        edicion.setId(20L);

        premio.setId(id);
        premio.setPuesto(puesto);
        premio.setModalidad(modalidad);
        premio.setAgrupacion(agrupacion);
        premio.setEdicion(edicion);

        assertEquals(id, premio.getId());
        assertEquals(puesto, premio.getPuesto());
        assertEquals(modalidad, premio.getModalidad());
        assertEquals(agrupacion, premio.getAgrupacion());
        assertEquals(edicion, premio.getEdicion());
    }

    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        Integer puesto = 1;
        Modalidad modalidad = Modalidad.COMPARSA;
        Agrupacion agrupacion = new Agrupacion();
        agrupacion.setId(10L);
        Edicion edicion = new Edicion();
        edicion.setId(20L);

        Premio premio = new Premio(id, puesto, modalidad, agrupacion, edicion);

        assertEquals(id, premio.getId());
        assertEquals(puesto, premio.getPuesto());
        assertEquals(modalidad, premio.getModalidad());
        assertEquals(agrupacion, premio.getAgrupacion());
        assertEquals(edicion, premio.getEdicion());
    }

    @Test
    void testEqualsAndHashCode() {
        Agrupacion agrupacion1 = new Agrupacion();
        agrupacion1.setId(1L);
        Edicion edicion1 = new Edicion();
        edicion1.setId(1L);

        Premio p1 = new Premio(1L, 1, Modalidad.COMPARSA, agrupacion1, edicion1);
        Premio p2 = new Premio(1L, 1, Modalidad.COMPARSA, agrupacion1, edicion1);

        // Test igualdad básica
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
        assertEquals(p1, p1); // Reflexivo

        // Test desigualdad con null y otro tipo
        assertNotEquals(null, p1);
        assertNotEquals("String", p1);

        // Test desigualdad por campos individuales
        Premio p3 = new Premio(2L, 1, Modalidad.COMPARSA, agrupacion1, edicion1); // ID diferente
        assertNotEquals(p1, p3);

        p3 = new Premio(1L, 2, Modalidad.COMPARSA, agrupacion1, edicion1); // Puesto diferente
        assertNotEquals(p1, p3);

        p3 = new Premio(1L, 1, Modalidad.CHIRIGOTA, agrupacion1, edicion1); // Modalidad diferente
        assertNotEquals(p1, p3);

        Agrupacion agrupacion2 = new Agrupacion();
        agrupacion2.setId(2L);
        p3 = new Premio(1L, 1, Modalidad.COMPARSA, agrupacion2, edicion1); // Agrupación diferente
        assertNotEquals(p1, p3);

        Edicion edicion2 = new Edicion();
        edicion2.setId(2L);
        p3 = new Premio(1L, 1, Modalidad.COMPARSA, agrupacion1, edicion2); // Edición diferente
        assertNotEquals(p1, p3);
    }

    @Test
    void testToString() {
        Premio premio = new Premio();
        premio.setId(1L);
        premio.setPuesto(1);
        premio.setModalidad(Modalidad.COMPARSA);

        String toString = premio.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("Premio"));
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("puesto=1"));
        assertTrue(toString.contains("modalidad=COMPARSA"));
    }

    @Test
    void testCanEqual() {
        Premio p1 = new Premio();
        Premio p2 = new Premio();

        assertTrue(p1.canEqual(p2));
        assertFalse(p1.canEqual(new Object()));
    }
}