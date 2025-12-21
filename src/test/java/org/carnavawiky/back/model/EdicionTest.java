package org.carnavawiky.back.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EdicionTest {

    private Edicion edicion;
    private Concurso concurso;

    @BeforeEach
    void setUp() {
        // Objeto de apoyo
        concurso = new Concurso();
        concurso.setId(1L);
        concurso.setNombre("COAC");

        // Entidad a probar
        edicion = new Edicion();
    }

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        // Establecer valores
        edicion.setId(1L);
        edicion.setAnho(2024);
        edicion.setConcurso(concurso);

        // Verificar valores
        assertNotNull(edicion);
        assertEquals(1L, edicion.getId());
        assertEquals(2024, edicion.getAnho());
        assertEquals(concurso, edicion.getConcurso());
    }
}