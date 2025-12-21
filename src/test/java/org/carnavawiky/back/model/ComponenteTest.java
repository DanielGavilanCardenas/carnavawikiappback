package org.carnavawiky.back.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        componente.setId(1L);
        componente.setRol(RolComponente.DIRECTOR);
    }

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        // Establecer valores
        componente.setPersona(persona);
        componente.setAgrupacion(agrupacion);

        // Verificar valores
        assertNotNull(componente);
        assertEquals(1L, componente.getId());
        assertEquals(RolComponente.DIRECTOR, componente.getRol());
        assertEquals(persona, componente.getPersona());
        assertEquals(agrupacion, componente.getAgrupacion());
    }

    // =======================================================
    // TESTS PARA COBERTURA DE MÉTODOS DE LOMBOK (@Data)
    // =======================================================

    @Test
    @DisplayName("Debe cumplir con el contrato de equals y hashCode")
    void testEqualsAndHashCode() {
        Componente componente1 = new Componente();
        componente1.setId(1L);
        componente1.setRol(RolComponente.DIRECTOR);

        Componente componente2 = new Componente();
        componente2.setId(1L);
        componente2.setRol(RolComponente.DIRECTOR);

        Componente componente3 = new Componente();
        componente3.setId(2L);
        componente3.setRol(RolComponente.GUITARRA);

        // Reflexividad
        assertEquals(componente1, componente1);

        // Simetría
        assertEquals(componente1, componente2);
        assertEquals(componente2, componente1);

        // Consistencia de hashCode
        assertEquals(componente1.hashCode(), componente2.hashCode());

        // Desigualdad
        assertNotEquals(componente1, componente3);
        assertNotEquals(componente1.hashCode(), componente3.hashCode());

        // Comparación con nulo y otros tipos
        assertNotEquals(null, componente1);
        assertNotEquals(componente1, new Object());
    }

    @Test
    @DisplayName("Debe generar una representación en String")
    void testToString() {
        String componenteString = componente.toString();
        assertNotNull(componenteString);
        assertTrue(componenteString.contains("rol=DIRECTOR"));
        assertTrue(componenteString.contains("id=1"));
    }

    @Test
    @DisplayName("Debe cumplir con canEqual para la simetría de equals")
    void testCanEqual() {
        Componente otroComponente = new Componente();
        assertTrue(componente.canEqual(otroComponente));
        assertFalse(componente.canEqual(new Object()));
    }
}