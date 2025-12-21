package org.carnavawiky.back.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        premio.setId(1L);
        premio.setPuesto(1);
    }

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        // Establecer valores
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

    // =======================================================
    // TESTS PARA COBERTURA DE MÉTODOS DE LOMBOK (@Data)
    // =======================================================

    @Test
    @DisplayName("Debe cumplir con el contrato de equals y hashCode")
    void testEqualsAndHashCode() {
        Premio premio1 = new Premio();
        premio1.setId(1L);
        premio1.setPuesto(1);

        Premio premio2 = new Premio();
        premio2.setId(1L);
        premio2.setPuesto(1);

        Premio premio3 = new Premio();
        premio3.setId(2L);
        premio3.setPuesto(2);

        // Reflexividad
        assertEquals(premio1, premio1);

        // Simetría
        assertEquals(premio1, premio2);
        assertEquals(premio2, premio1);

        // Consistencia de hashCode
        assertEquals(premio1.hashCode(), premio2.hashCode());

        // Desigualdad
        assertNotEquals(premio1, premio3);
        assertNotEquals(premio1.hashCode(), premio3.hashCode());

        // Comparación con nulo y otros tipos
        assertNotEquals(null, premio1);
        assertNotEquals(premio1, new Object());
    }

    @Test
    @DisplayName("Debe generar una representación en String")
    void testToString() {
        String premioString = premio.toString();
        assertNotNull(premioString);
        assertTrue(premioString.contains("puesto=1"));
        assertTrue(premioString.contains("id=1"));
    }

    @Test
    @DisplayName("Debe cumplir con canEqual para la simetría de equals")
    void testCanEqual() {
        Premio otroPremio = new Premio();
        assertTrue(premio.canEqual(otroPremio));
        assertFalse(premio.canEqual(new Object()));
    }
}