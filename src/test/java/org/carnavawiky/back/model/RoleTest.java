package org.carnavawiky.back.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RoleTest {

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        // Entidad a probar
        Role role = new Role();

        // Establecer valores
        role.setId(1L);
        role.setName(Role.RoleName.ROLE_ADMIN);

        // Verificar valores
        assertNotNull(role);
        assertEquals(1L, role.getId());
        assertEquals(Role.RoleName.ROLE_ADMIN, role.getName());
    }
}