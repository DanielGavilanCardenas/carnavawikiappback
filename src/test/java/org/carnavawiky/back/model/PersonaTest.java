package org.carnavawiky.back.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonaTest {

    private Persona persona;
    private Localidad localidad;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        localidad = new Localidad(1L, "Cádiz");
        usuario = new Usuario();
        usuario.setId(10L);
        usuario.setUsername("juancarlos");

        persona = new Persona();
        persona.setId(1L);
        persona.setNombreReal("Juan Carlos Aragón Becerra");
        persona.setApodo("El Capitán Veneno");
        persona.setOrigen(localidad);
        persona.setUsuario(usuario);
    }

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        assertNotNull(persona);
        assertEquals(1L, persona.getId());
        assertEquals("Juan Carlos Aragón Becerra", persona.getNombreReal());
        assertEquals("El Capitán Veneno", persona.getApodo());
        assertEquals(localidad, persona.getOrigen());
        assertEquals(usuario, persona.getUsuario());
    }

    @Test
    @DisplayName("Debe funcionar el constructor con todos los argumentos")
    void testAllArgsConstructor() {
        Persona p = new Persona(2L, "Antonio", "Ares", localidad, usuario);
        
        assertEquals(2L, p.getId());
        assertEquals("Antonio", p.getNombreReal());
        assertEquals("Ares", p.getApodo());
        assertEquals(localidad, p.getOrigen());
        assertEquals(usuario, p.getUsuario());
    }

    // =======================================================
    // TESTS PARA COBERTURA DE MÉTODOS DE LOMBOK (@Data)
    // =======================================================

    @Test
    @DisplayName("Debe cubrir todas las ramas de equals y hashCode variando cada campo")
    void testEqualsAndHashCode_Exhaustivo() {
        // 1. Preparación de objetos base
        Localidad loc1 = new Localidad(1L, "Cádiz");
        Localidad loc2 = new Localidad(2L, "Sevilla");
        Usuario user1 = new Usuario(); user1.setId(1L);
        Usuario user2 = new Usuario(); user2.setId(2L);

        Persona p1 = new Persona(1L, "Nombre", "Apodo", loc1, user1);
        Persona p2 = new Persona(1L, "Nombre", "Apodo", loc1, user1);

        // --- IGUALDAD BÁSICA ---
        assertEquals(p1, p1); // Reflexivo
        assertEquals(p1, p2); // Simétrico
        assertEquals(p2, p1);
        assertEquals(p1.hashCode(), p2.hashCode()); // HashCode consistente

        assertNotEquals(p1, null); // No igual a null
        assertNotEquals(p1, new Object()); // No igual a otro tipo

        // --- VARIACIONES CAMPO A CAMPO (Orden probable: id, nombreReal, apodo, origen, usuario) ---

        // 1. ID Diferente
        Persona pDiffId = new Persona(2L, "Nombre", "Apodo", loc1, user1);
        assertNotEquals(p1, pDiffId);
        assertNotEquals(p1.hashCode(), pDiffId.hashCode());

        // 2. ID Null vs No Null
        Persona pIdNull = new Persona(null, "Nombre", "Apodo", loc1, user1);
        assertNotEquals(p1, pIdNull);
        assertNotEquals(pIdNull, p1);

        // 3. NombreReal Diferente
        Persona pDiffNombre = new Persona(1L, "Otro", "Apodo", loc1, user1);
        assertNotEquals(p1, pDiffNombre);
        assertNotEquals(p1.hashCode(), pDiffNombre.hashCode());

        // 4. NombreReal Null vs No Null
        Persona pNombreNull = new Persona(1L, null, "Apodo", loc1, user1);
        assertNotEquals(p1, pNombreNull);
        assertNotEquals(pNombreNull, p1);

        // 5. Apodo Diferente
        Persona pDiffApodo = new Persona(1L, "Nombre", "Otro", loc1, user1);
        assertNotEquals(p1, pDiffApodo);
        assertNotEquals(p1.hashCode(), pDiffApodo.hashCode());

        // 6. Apodo Null vs No Null
        Persona pApodoNull = new Persona(1L, "Nombre", null, loc1, user1);
        assertNotEquals(p1, pApodoNull);
        assertNotEquals(pApodoNull, p1);
        
        // 7. Apodo Null vs Null (Deben ser iguales si el resto coincide)
        Persona pApodoNull2 = new Persona(1L, "Nombre", null, loc1, user1);
        assertEquals(pApodoNull, pApodoNull2);

        // 8. Origen (Localidad) Diferente
        Persona pDiffOrigen = new Persona(1L, "Nombre", "Apodo", loc2, user1);
        assertNotEquals(p1, pDiffOrigen);
        assertNotEquals(p1.hashCode(), pDiffOrigen.hashCode());

        // 9. Origen Null vs No Null
        Persona pOrigenNull = new Persona(1L, "Nombre", "Apodo", null, user1);
        assertNotEquals(p1, pOrigenNull);
        assertNotEquals(pOrigenNull, p1);

        // 10. Usuario Diferente
        Persona pDiffUsuario = new Persona(1L, "Nombre", "Apodo", loc1, user2);
        assertNotEquals(p1, pDiffUsuario);
        assertNotEquals(p1.hashCode(), pDiffUsuario.hashCode());

        // 11. Usuario Null vs No Null
        Persona pUsuarioNull = new Persona(1L, "Nombre", "Apodo", loc1, null);
        assertNotEquals(p1, pUsuarioNull);
        assertNotEquals(pUsuarioNull, p1);
        
        // 12. Usuario Null vs Null
        Persona pUsuarioNull2 = new Persona(1L, "Nombre", "Apodo", loc1, null);
        assertEquals(pUsuarioNull, pUsuarioNull2);
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