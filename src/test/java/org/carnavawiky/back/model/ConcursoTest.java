package org.carnavawiky.back.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConcursoTest {

    private Concurso concurso;
    private Localidad localidad;

    @BeforeEach
    void setUp() {
        // Objeto de apoyo
        localidad = new Localidad();
        localidad.setId(1L);
        localidad.setNombre("Cádiz");

        // Entidad a probar
        concurso = new Concurso();
    }

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        // Establecer valores
        concurso.setId(1L);
        concurso.setNombre("COAC");
        concurso.setEstaActivo(true);
        concurso.setLocalidad(localidad);

        // Verificar valores
        assertNotNull(concurso);
        assertEquals(1L, concurso.getId());
        assertEquals("COAC", concurso.getNombre());
        assertTrue(concurso.getEstaActivo());
        assertEquals(localidad, concurso.getLocalidad());
    }

    @Test
    @DisplayName("Debe tener el campo 'estaActivo' como true por defecto")
    void testDefaultValues() {
        Concurso nuevoConcurso = new Concurso();
        assertNotNull(nuevoConcurso);
        assertTrue(nuevoConcurso.getEstaActivo(), "El campo 'estaActivo' debería ser true por defecto.");
    }
}