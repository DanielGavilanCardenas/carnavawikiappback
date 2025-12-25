package org.carnavawiky.back.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioRequestTest {

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        UsuarioRequest request = new UsuarioRequest();
        Set<Long> roles = new HashSet<>();
        roles.add(1L);
        
        request.setUsername("user");
        request.setPassword("pass");
        request.setEmail("email@test.com");
        request.setRoleIds(roles);
        request.setEnabled(false);

        assertNotNull(request);
        assertEquals("user", request.getUsername());
        assertEquals("pass", request.getPassword());
        assertEquals("email@test.com", request.getEmail());
        assertEquals(roles, request.getRoleIds());
        assertFalse(request.getEnabled());
    }

    @Test
    @DisplayName("Debe tener enabled como true por defecto")
    void testDefaultValues() {
        UsuarioRequest request = new UsuarioRequest();
        assertTrue(request.getEnabled());
    }

    // =======================================================
    // TESTS PARA COBERTURA DE MÉTODOS DE LOMBOK (@Data)
    // =======================================================

    @Test
    @DisplayName("Debe cubrir todas las ramas de equals y hashCode variando cada campo")
    void testEqualsAndHashCode_Exhaustivo() {
        Set<Long> roles1 = new HashSet<>(); roles1.add(1L);
        Set<Long> roles2 = new HashSet<>(); roles2.add(2L);

        // Objeto base
        UsuarioRequest r1 = new UsuarioRequest();
        r1.setUsername("user");
        r1.setPassword("pass");
        r1.setEmail("email");
        r1.setRoleIds(roles1);
        r1.setEnabled(true);

        // Copia exacta
        UsuarioRequest r2 = new UsuarioRequest();
        r2.setUsername("user");
        r2.setPassword("pass");
        r2.setEmail("email");
        r2.setRoleIds(roles1);
        r2.setEnabled(true);

        // --- IGUALDAD BÁSICA ---
        assertEquals(r1, r1); // Reflexivo
        assertEquals(r1, r2); // Simétrico
        assertEquals(r2, r1);
        assertEquals(r1.hashCode(), r2.hashCode()); // HashCode consistente

        assertNotEquals(r1, null); // No igual a null
        assertNotEquals(r1, new Object()); // No igual a otro tipo

        // --- VARIACIONES CAMPO A CAMPO ---

        // 1. Username
        UsuarioRequest rDiffUser = copiar(r1); rDiffUser.setUsername("otro");
        assertNotEquals(r1, rDiffUser);
        assertNotEquals(r1.hashCode(), rDiffUser.hashCode());
        
        UsuarioRequest rUserNull = copiar(r1); rUserNull.setUsername(null);
        assertNotEquals(r1, rUserNull);
        assertNotEquals(rUserNull, r1);

        // 2. Password
        UsuarioRequest rDiffPass = copiar(r1); rDiffPass.setPassword("otra");
        assertNotEquals(r1, rDiffPass);
        assertNotEquals(r1.hashCode(), rDiffPass.hashCode());
        
        UsuarioRequest rPassNull = copiar(r1); rPassNull.setPassword(null);
        assertNotEquals(r1, rPassNull);
        assertNotEquals(rPassNull, r1);

        // 3. Email
        UsuarioRequest rDiffEmail = copiar(r1); rDiffEmail.setEmail("otro");
        assertNotEquals(r1, rDiffEmail);
        assertNotEquals(r1.hashCode(), rDiffEmail.hashCode());
        
        UsuarioRequest rEmailNull = copiar(r1); rEmailNull.setEmail(null);
        assertNotEquals(r1, rEmailNull);
        assertNotEquals(rEmailNull, r1);

        // 4. RoleIds
        UsuarioRequest rDiffRoles = copiar(r1); rDiffRoles.setRoleIds(roles2);
        assertNotEquals(r1, rDiffRoles);
        assertNotEquals(r1.hashCode(), rDiffRoles.hashCode());
        
        UsuarioRequest rRolesNull = copiar(r1); rRolesNull.setRoleIds(null);
        assertNotEquals(r1, rRolesNull);
        assertNotEquals(rRolesNull, r1);

        // 5. Enabled
        UsuarioRequest rDiffEnabled = copiar(r1); rDiffEnabled.setEnabled(false);
        assertNotEquals(r1, rDiffEnabled);
        assertNotEquals(r1.hashCode(), rDiffEnabled.hashCode());
        
        UsuarioRequest rEnabledNull = copiar(r1); rEnabledNull.setEnabled(null);
        assertNotEquals(r1, rEnabledNull);
        assertNotEquals(rEnabledNull, r1);
        
        // 6. Enabled Null vs Null
        UsuarioRequest rEnabledNull2 = copiar(r1); rEnabledNull2.setEnabled(null);
        assertEquals(rEnabledNull, rEnabledNull2);
    }

    // Método auxiliar para clonar
    private UsuarioRequest copiar(UsuarioRequest original) {
        UsuarioRequest copia = new UsuarioRequest();
        copia.setUsername(original.getUsername());
        copia.setPassword(original.getPassword());
        copia.setEmail(original.getEmail());
        copia.setRoleIds(original.getRoleIds()); // Referencia al mismo set
        copia.setEnabled(original.getEnabled());
        return copia;
    }

    @Test
    @DisplayName("Debe generar una representación en String")
    void testToString() {
        UsuarioRequest request = new UsuarioRequest();
        request.setUsername("Test");
        
        String s = request.toString();
        assertNotNull(s);
        assertTrue(s.contains("username=Test"));
    }

    @Test
    @DisplayName("Debe cumplir con canEqual para la simetría de equals")
    void testCanEqual() {
        UsuarioRequest r = new UsuarioRequest();
        assertTrue(r.canEqual(new UsuarioRequest()));
        assertFalse(r.canEqual(new Object()));
    }
}