package org.carnavawiky.back.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    }

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        LocalDateTime fecha = LocalDateTime.now();

        // Establecer valores
        agrupacion.setId(1L);
        agrupacion.setNombre("Los Piratas");
        agrupacion.setAnho(1998);
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
}