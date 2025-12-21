package org.carnavawiky.back.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PremioTest {

    private Premio premio;
    private Agrupacion agrupacion;
    private Edicion edicion;

    @BeforeEach
    void setUp() {
        // Objetos de apoyo
        agrupacion = new Agrupacion();
        agrupacion.setId(10L);
        agrupacion.setNombre("Los Yesterday");

        edicion = new Edicion();
        edicion.setId(20L);
        edicion.setAnho(1999);

        // Entidad a probar
        premio = new Premio();
    }

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        // Establecer valores
        premio.setId(1L);
        premio.setPuesto(1);
        premio.setModalidad(Modalidad.CHIRIGOTA);
        premio.setAgrupacion(agrupacion);
        premio.setEdicion(edicion);

        // Verificar valores
        assertNotNull(premio);
        assertEquals(1L, premio.getId());
        assertEquals(1, premio.getPuesto());
        assertEquals(Modalidad.CHIRIGOTA, premio.getModalidad());
        assertEquals(agrupacion, premio.getAgrupacion());
        assertEquals(edicion, premio.getEdicion());
    }
}