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
        componente.setPersona(persona);
        componente.setAgrupacion(agrupacion);
    }

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        assertNotNull(componente);
        assertEquals(1L, componente.getId());
        assertEquals(RolComponente.DIRECTOR, componente.getRol());
        assertEquals(persona, componente.getPersona());
        assertEquals(agrupacion, componente.getAgrupacion());
    }

    @Test
    @DisplayName("Debe funcionar el constructor con todos los argumentos")
    void testAllArgsConstructor() {
        Componente c = new Componente(2L, RolComponente.CAJA, persona, agrupacion);
        
        assertEquals(2L, c.getId());
        assertEquals(RolComponente.CAJA, c.getRol());
        assertEquals(persona, c.getPersona());
        assertEquals(agrupacion, c.getAgrupacion());
    }

    // =======================================================
    // TESTS PARA COBERTURA DE MÉTODOS DE LOMBOK (@Data)
    // =======================================================

    @Test
    @DisplayName("Debe cubrir todas las ramas de equals y hashCode variando cada campo")
    void testEqualsAndHashCode_Exhaustivo() {
        // Objetos base para relaciones
        Persona p1 = new Persona(); p1.setId(1L);
        Persona p2 = new Persona(); p2.setId(2L);
        
        Agrupacion a1 = new Agrupacion(); a1.setId(1L);
        Agrupacion a2 = new Agrupacion(); a2.setId(2L);

        // Objeto base
        Componente c1 = new Componente(1L, RolComponente.DIRECTOR, p1, a1);
        // Copia exacta
        Componente c2 = new Componente(1L, RolComponente.DIRECTOR, p1, a1);

        // --- IGUALDAD BÁSICA ---
        assertEquals(c1, c1); // Reflexivo
        assertEquals(c1, c2); // Simétrico
        assertEquals(c2, c1);
        assertEquals(c1.hashCode(), c2.hashCode()); // HashCode consistente

        assertNotEquals(c1, null); // No igual a null
        assertNotEquals(c1, new Object()); // No igual a otro tipo

        // --- VARIACIONES CAMPO A CAMPO ---

        // 1. ID Diferente
        Componente cDiffId = new Componente(2L, RolComponente.DIRECTOR, p1, a1);
        assertNotEquals(c1, cDiffId);
        assertNotEquals(c1.hashCode(), cDiffId.hashCode());

        // 2. ID Null vs No Null
        Componente cIdNull = new Componente(null, RolComponente.DIRECTOR, p1, a1);
        assertNotEquals(c1, cIdNull);
        assertNotEquals(cIdNull, c1);

        // 3. Rol Diferente
        Componente cDiffRol = new Componente(1L, RolComponente.BOMBO, p1, a1);
        assertNotEquals(c1, cDiffRol);
        assertNotEquals(c1.hashCode(), cDiffRol.hashCode());

        // 4. Rol Null vs No Null
        Componente cRolNull = new Componente(1L, null, p1, a1);
        assertNotEquals(c1, cRolNull);
        assertNotEquals(cRolNull, c1);

        // 5. Persona Diferente
        Componente cDiffPersona = new Componente(1L, RolComponente.DIRECTOR, p2, a1);
        assertNotEquals(c1, cDiffPersona);
        assertNotEquals(c1.hashCode(), cDiffPersona.hashCode());

        // 6. Persona Null vs No Null
        Componente cPersonaNull = new Componente(1L, RolComponente.DIRECTOR, null, a1);
        assertNotEquals(c1, cPersonaNull);
        assertNotEquals(cPersonaNull, c1);

        // 7. Agrupacion Diferente
        Componente cDiffAgrupacion = new Componente(1L, RolComponente.DIRECTOR, p1, a2);
        assertNotEquals(c1, cDiffAgrupacion);
        assertNotEquals(c1.hashCode(), cDiffAgrupacion.hashCode());

        // 8. Agrupacion Null vs No Null
        Componente cAgrupacionNull = new Componente(1L, RolComponente.DIRECTOR, p1, null);
        assertNotEquals(c1, cAgrupacionNull);
        assertNotEquals(cAgrupacionNull, c1);
        
        // 9. Agrupacion Null vs Null (Iguales si el resto coincide)
        Componente cAgrupacionNull2 = new Componente(1L, RolComponente.DIRECTOR, p1, null);
        assertEquals(cAgrupacionNull, cAgrupacionNull2);
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