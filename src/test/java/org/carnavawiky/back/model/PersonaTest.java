package org.carnavawiky.back.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PersonaTest {

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        // Entidad a probar
        Persona persona = new Persona();
        LocalDate fechaNacimiento = LocalDate.of(1970, 1, 1);

        // Establecer valores
        persona.setId(1L);
        persona.setNombreReal("Juan Carlos Aragón Becerra");
        persona.setApodo("El Capitán Veneno");

        // Verificar valores
        assertNotNull(persona);
        assertEquals(1L, persona.getId());
        assertEquals("Juan Carlos Aragón Becerra", persona.getNombreReal());
        assertEquals("El Capitán Veneno", persona.getApodo());
        }
}