package org.carnavawiky.back.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioResponseTest {

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        UsuarioResponse response = new UsuarioResponse();
        LocalDateTime fecha = LocalDateTime.now();
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");
        
        response.setId(1L);
        response.setUsername("user");
        response.setEmail("email@test.com");
        response.setEnabled(true);
        response.setFechaAlta(fecha);
        response.setRoles(roles);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("user", response.getUsername());
        assertEquals("email@test.com", response.getEmail());
        assertTrue(response.isEnabled());
        assertEquals(fecha, response.getFechaAlta());
        assertEquals(roles, response.getRoles());
    }

    // =======================================================
    // TESTS PARA COBERTURA DE MÉTODOS DE LOMBOK (@Data)
    // =======================================================

    @Test
    @DisplayName("Debe cubrir todas las ramas de equals y hashCode variando cada campo")
    void testEqualsAndHashCode_Exhaustivo() {
        LocalDateTime fecha = LocalDateTime.of(2024, 1, 1, 12, 0);
        Set<String> roles1 = new HashSet<>(); roles1.add("A");
        Set<String> roles2 = new HashSet<>(); roles2.add("B");

        // Objeto base
        UsuarioResponse r1 = new UsuarioResponse();
        r1.setId(1L);
        r1.setUsername("user");
        r1.setEmail("email");
        r1.setEnabled(true);
        r1.setFechaAlta(fecha);
        r1.setRoles(roles1);

        // Copia exacta
        UsuarioResponse r2 = new UsuarioResponse();
        r2.setId(1L);
        r2.setUsername("user");
        r2.setEmail("email");
        r2.setEnabled(true);
        r2.setFechaAlta(fecha);
        r2.setRoles(roles1);

        // --- IGUALDAD BÁSICA ---
        assertEquals(r1, r1); // Reflexivo
        assertEquals(r1, r2); // Simétrico
        assertEquals(r2, r1);
        assertEquals(r1.hashCode(), r2.hashCode()); // HashCode consistente

        assertNotEquals(r1, null); // No igual a null
        assertNotEquals(r1, new Object()); // No igual a otro tipo

        // --- VARIACIONES CAMPO A CAMPO ---

        // 1. ID
        UsuarioResponse rDiffId = copiar(r1); rDiffId.setId(2L);
        assertNotEquals(r1, rDiffId);
        assertNotEquals(r1.hashCode(), rDiffId.hashCode());
        
        UsuarioResponse rIdNull = copiar(r1); rIdNull.setId(null);
        assertNotEquals(r1, rIdNull);
        assertNotEquals(rIdNull, r1);

        // 2. Username
        UsuarioResponse rDiffUser = copiar(r1); rDiffUser.setUsername("otro");
        assertNotEquals(r1, rDiffUser);
        assertNotEquals(r1.hashCode(), rDiffUser.hashCode());
        
        UsuarioResponse rUserNull = copiar(r1); rUserNull.setUsername(null);
        assertNotEquals(r1, rUserNull);
        assertNotEquals(rUserNull, r1);

        // 3. Email
        UsuarioResponse rDiffEmail = copiar(r1); rDiffEmail.setEmail("otro");
        assertNotEquals(r1, rDiffEmail);
        assertNotEquals(r1.hashCode(), rDiffEmail.hashCode());
        
        UsuarioResponse rEmailNull = copiar(r1); rEmailNull.setEmail(null);
        assertNotEquals(r1, rEmailNull);
        assertNotEquals(rEmailNull, r1);

        // 4. Enabled (primitivo boolean, no puede ser null)
        UsuarioResponse rDiffEnabled = copiar(r1); rDiffEnabled.setEnabled(false);
        assertNotEquals(r1, rDiffEnabled);
        assertNotEquals(r1.hashCode(), rDiffEnabled.hashCode());

        // 5. FechaAlta
        UsuarioResponse rDiffFecha = copiar(r1); rDiffFecha.setFechaAlta(fecha.plusDays(1));
        assertNotEquals(r1, rDiffFecha);
        assertNotEquals(r1.hashCode(), rDiffFecha.hashCode());
        
        UsuarioResponse rFechaNull = copiar(r1); rFechaNull.setFechaAlta(null);
        assertNotEquals(r1, rFechaNull);
        assertNotEquals(rFechaNull, r1);

        // 6. Roles
        UsuarioResponse rDiffRoles = copiar(r1); rDiffRoles.setRoles(roles2);
        assertNotEquals(r1, rDiffRoles);
        assertNotEquals(r1.hashCode(), rDiffRoles.hashCode());
        
        UsuarioResponse rRolesNull = copiar(r1); rRolesNull.setRoles(null);
        assertNotEquals(r1, rRolesNull);
        assertNotEquals(rRolesNull, r1);
        
        // 7. Roles Null vs Null
        UsuarioResponse rRolesNull2 = copiar(r1); rRolesNull2.setRoles(null);
        assertEquals(rRolesNull, rRolesNull2);
    }

    // Método auxiliar para clonar
    private UsuarioResponse copiar(UsuarioResponse original) {
        UsuarioResponse copia = new UsuarioResponse();
        copia.setId(original.getId());
        copia.setUsername(original.getUsername());
        copia.setEmail(original.getEmail());
        copia.setEnabled(original.isEnabled());
        copia.setFechaAlta(original.getFechaAlta());
        copia.setRoles(original.getRoles()); // Referencia al mismo set
        return copia;
    }

    @Test
    @DisplayName("Debe generar una representación en String")
    void testToString() {
        UsuarioResponse response = new UsuarioResponse();
        response.setId(1L);
        response.setUsername("Test");
        
        String s = response.toString();
        assertNotNull(s);
        assertTrue(s.contains("id=1"));
        assertTrue(s.contains("username=Test"));
    }

    @Test
    @DisplayName("Debe cumplir con canEqual para la simetría de equals")
    void testCanEqual() {
        UsuarioResponse r = new UsuarioResponse();
        assertTrue(r.canEqual(new UsuarioResponse()));
        assertFalse(r.canEqual(new Object()));
    }
}