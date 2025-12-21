package org.carnavawiky.back.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ComponenteTest {

    private Componente componente;
    private Persona persona;
    private Agrupacion agrupacion;

    @BeforeEach
    void setUp() {
        // Objetos de apoyo
        persona = new Persona();
        persona.setId(100L);
        persona.setNombreReal("Manuel Santander");

        agrupacion = new Agrupacion();
        agrupacion.setId(50L);
        agrupacion.setNombre("La familia Pepperoni");

        // Entidad a probar
        componente = new Componente();
    }

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        // Establecer valores
        componente.setId(1L);
        componente.setRol(RolComponente.DIRECTOR);
        componente.setPersona(persona);
        componente.setAgrupacion(agrupacion);

        // Verificar valores
        assertNotNull(componente);
        assertEquals(1L, componente.getId());
        assertEquals(RolComponente.DIRECTOR, componente.getRol());
        assertEquals(persona, componente.getPersona());
        assertEquals(agrupacion, componente.getAgrupacion());
    }
}