package org.carnavawiky.back.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        Role role = new Role();
        role.setId(1L);
        role.setName(Role.RoleName.ROLE_ADMIN);

        assertNotNull(role);
        assertEquals(1L, role.getId());
        assertEquals(Role.RoleName.ROLE_ADMIN, role.getName());
    }

    // =======================================================
    // TESTS PARA COBERTURA DE MÉTODOS DE LOMBOK (@Data)
    // =======================================================

    @Test
    @DisplayName("Debe cubrir todas las ramas de equals y hashCode variando cada campo")
    void testEqualsAndHashCode_Exhaustivo() {
        // Objeto base (Usamos setters porque no hay @AllArgsConstructor)
        Role r1 = new Role();
        r1.setId(1L);
        r1.setName(Role.RoleName.ROLE_USER);

        // Copia exacta
        Role r2 = new Role();
        r2.setId(1L);
        r2.setName(Role.RoleName.ROLE_USER);

        // --- IGUALDAD BÁSICA ---
        assertEquals(r1, r1); // Reflexivo
        assertEquals(r1, r2); // Simétrico
        assertEquals(r2, r1);
        assertEquals(r1.hashCode(), r2.hashCode()); // HashCode consistente

        assertNotEquals(r1, null); // No igual a null
        assertNotEquals(r1, new Object()); // No igual a otro tipo

        // --- VARIACIONES CAMPO A CAMPO ---

        // 1. ID Diferente
        Role rDiffId = new Role();
        rDiffId.setId(2L);
        rDiffId.setName(Role.RoleName.ROLE_USER);
        assertNotEquals(r1, rDiffId);
        assertNotEquals(r1.hashCode(), rDiffId.hashCode());

        // 2. ID Null vs No Null
        Role rIdNull = new Role();
        rIdNull.setId(null);
        rIdNull.setName(Role.RoleName.ROLE_USER);
        assertNotEquals(r1, rIdNull);
        assertNotEquals(rIdNull, r1);

        // 3. Name Diferente
        Role rDiffName = new Role();
        rDiffName.setId(1L);
        rDiffName.setName(Role.RoleName.ROLE_ADMIN);
        assertNotEquals(r1, rDiffName);
        assertNotEquals(r1.hashCode(), rDiffName.hashCode());

        // 4. Name Null vs No Null
        Role rNameNull = new Role();
        rNameNull.setId(1L);
        rNameNull.setName(null);
        assertNotEquals(r1, rNameNull);
        assertNotEquals(rNameNull, r1);
        
        // 5. Name Null vs Null (Iguales si el resto coincide)
        Role rNameNull2 = new Role();
        rNameNull2.setId(1L);
        rNameNull2.setName(null);
        assertEquals(rNameNull, rNameNull2);
    }

    @Test
    @DisplayName("Debe generar una representación en String")
    void testToString() {
        Role role = new Role();
        role.setId(1L);
        role.setName(Role.RoleName.ROLE_ESPECIALISTO);
        
        String roleString = role.toString();
        assertNotNull(roleString);
        assertTrue(roleString.contains("name=ROLE_ESPECIALISTO"));
        assertTrue(roleString.contains("id=1"));
    }

    @Test
    @DisplayName("Debe cumplir con canEqual para la simetría de equals")
    void testCanEqual() {
        Role role = new Role();
        Role otroRole = new Role();
        assertTrue(role.canEqual(otroRole));
        assertFalse(role.canEqual(new Object()));
    }
    
    @Test
    @DisplayName("Debe cubrir los valores del Enum RoleName")
    void testEnumValues() {
        assertEquals("ROLE_ADMIN", Role.RoleName.ROLE_ADMIN.name());
        assertEquals("ROLE_USER", Role.RoleName.ROLE_USER.name());
        assertEquals("ROLE_ESPECIALISTO", Role.RoleName.ROLE_ESPECIALISTO.name());
        
        // Verificar valueOf
        assertEquals(Role.RoleName.ROLE_ADMIN, Role.RoleName.valueOf("ROLE_ADMIN"));
        
        // Verificar values()
        Role.RoleName[] values = Role.RoleName.values();
        assertEquals(3, values.length);
    }
}