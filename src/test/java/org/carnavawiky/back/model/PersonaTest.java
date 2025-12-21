package org.carnavawiky.back.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PersonaTest {

    private Persona persona;

    @BeforeEach
    void setUp() {
        persona = new Persona();
        persona.setId(1L);
        persona.setNombreReal("Juan Carlos Aragón Becerra");
        persona.setApodo("El Capitán Veneno");
    }

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        LocalDate fechaNacimiento = LocalDate.of(1970, 1, 1);


        // Verificar valores
        assertNotNull(persona);
        assertEquals(1L, persona.getId());
        assertEquals("Juan Carlos Aragón Becerra", persona.getNombreReal());
        assertEquals("El Capitán Veneno", persona.getApodo());
    }

    // =======================================================
    // TESTS PARA COBERTURA DE MÉTODOS DE LOMBOK (@Data)
    // =======================================================

    @Test
    @DisplayName("Debe cumplir con el contrato de equals y hashCode")
    void testEqualsAndHashCode() {
        Persona persona1 = new Persona();
        persona1.setId(1L);
        persona1.setNombreReal("Juan Carlos Aragón Becerra");

        Persona persona2 = new Persona();
        persona2.setId(1L);
        persona2.setNombreReal("Juan Carlos Aragón Becerra");

        Persona persona3 = new Persona();
        persona3.setId(2L);
        persona3.setNombreReal("Antonio Martínez Ares");

        // Reflexividad
        assertEquals(persona1, persona1);

        // Simetría
        assertEquals(persona1, persona2);
        assertEquals(persona2, persona1);

        // Consistencia de hashCode
        assertEquals(persona1.hashCode(), persona2.hashCode());

        // Desigualdad
        assertNotEquals(persona1, persona3);
        assertNotEquals(persona1.hashCode(), persona3.hashCode());

        // Comparación con nulo y otros tipos
        assertNotEquals(null, persona1);
        assertNotEquals(persona1, new Object());
    }

    @Test
    @DisplayName("Debe generar una representación en String")
    void testToString() {
        String personaString = persona.toString();
        assertNotNull(personaString);
        assertTrue(personaString.contains("nombreReal=Juan Carlos Aragón Becerra"));
        assertTrue(personaString.contains("apodo=El Capitán Veneno"));
    }

    @Test
    @DisplayName("Debe cumplir con canEqual para la simetría de equals")
    void testCanEqual() {
        Persona otraPersona = new Persona();
        assertTrue(persona.canEqual(otraPersona));
        assertFalse(persona.canEqual(new Object()));
    }
}