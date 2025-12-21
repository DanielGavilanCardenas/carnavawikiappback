package org.carnavawiky.back.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AgrupacionTest {

    private Agrupacion agrupacion;
    private Usuario usuario;
    private Localidad localidad;

    @BeforeEach
    void setUp() {
        // Objetos de apoyo
        usuario = new Usuario();
        usuario.setId(10L);
        usuario.setUsername("creador");

        localidad = new Localidad();
        localidad.setId(20L);
        localidad.setNombre("Cádiz");

        // Entidad a probar
        agrupacion = new Agrupacion();
        agrupacion.setId(1L);
        agrupacion.setNombre("Los Piratas");
        agrupacion.setAnho(1998);
    }

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        LocalDateTime fecha = LocalDateTime.now();

        // Establecer valores
        agrupacion.setDescripcion("Una comparsa legendaria");
        agrupacion.setModalidad(Modalidad.COMPARSA);
        agrupacion.setFechaAlta(fecha);
        agrupacion.setUsuarioCreador(usuario);
        agrupacion.setLocalidad(localidad);

        // Verificar valores
        assertNotNull(agrupacion);
        assertEquals(1L, agrupacion.getId());
        assertEquals("Los Piratas", agrupacion.getNombre());
        assertEquals(1998, agrupacion.getAnho());
        assertEquals("Una comparsa legendaria", agrupacion.getDescripcion());
        assertEquals(Modalidad.COMPARSA, agrupacion.getModalidad());
        assertEquals(fecha, agrupacion.getFechaAlta());
        assertEquals(usuario, agrupacion.getUsuarioCreador());
        assertEquals(localidad, agrupacion.getLocalidad());
    }

    // =======================================================
    // TESTS PARA COBERTURA DE MÉTODOS DE LOMBOK (@Data)
    // =======================================================

    @Test
    @DisplayName("Debe cumplir con el contrato de equals y hashCode")
    void testEqualsAndHashCode() {
        Agrupacion agrupacion1 = new Agrupacion();
        agrupacion1.setId(1L);
        agrupacion1.setNombre("Los Piratas");

        Agrupacion agrupacion2 = new Agrupacion();
        agrupacion2.setId(1L);
        agrupacion2.setNombre("Los Piratas");

        Agrupacion agrupacion3 = new Agrupacion();
        agrupacion3.setId(2L);
        agrupacion3.setNombre("Los Yesterday");

        // Reflexividad
        assertEquals(agrupacion1, agrupacion1);

        // Simetría
        assertEquals(agrupacion1, agrupacion2);
        assertEquals(agrupacion2, agrupacion1);

        // Consistencia de hashCode
        assertEquals(agrupacion1.hashCode(), agrupacion2.hashCode());

        // Desigualdad
        assertNotEquals(agrupacion1, agrupacion3);
        assertNotEquals(agrupacion1.hashCode(), agrupacion3.hashCode());

        // Comparación con nulo y otros tipos
        assertNotEquals(null, agrupacion1);
        assertNotEquals(agrupacion1, new Object());
    }

    @Test
    @DisplayName("Debe generar una representación en String")
    void testToString() {
        String agrupacionString = agrupacion.toString();
        assertNotNull(agrupacionString);
        assertTrue(agrupacionString.contains("nombre=Los Piratas"));
        assertTrue(agrupacionString.contains("anho=1998"));
    }

    @Test
    @DisplayName("Debe cumplir con canEqual para la simetría de equals")
    void testCanEqual() {
        Agrupacion otraAgrupacion = new Agrupacion();
        assertTrue(agrupacion.canEqual(otraAgrupacion));
        assertFalse(agrupacion.canEqual(new Object()));
    }
}