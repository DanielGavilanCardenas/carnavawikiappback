package org.carnavawiky.back.mapper;

import org.carnavawiky.back.dto.UsuarioResponse;
import org.carnavawiky.back.model.Role;
import org.carnavawiky.back.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioMapperTest {

    private UsuarioMapper usuarioMapper;

    @BeforeEach
    void setUp() {
        usuarioMapper = new UsuarioMapper();
    }

    @Test
    @DisplayName("Debe convertir Usuario Entity a UsuarioResponse correctamente")
    void testToResponse_DebeConvertirEntityAResponse() {
        // ARRANGE
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");
        usuario.setEmail("test@email.com");
        usuario.setEnabled(true);
        usuario.setFechaAlta(LocalDateTime.now());

        Role adminRole = new Role();
        adminRole.setName(Role.RoleName.ROLE_ADMIN);
        usuario.setRoles(Set.of(adminRole));

        // ACT
        UsuarioResponse response = usuarioMapper.toResponse(usuario);

        // ASSERT
        assertNotNull(response);
        assertEquals(usuario.getId(), response.getId());
        assertEquals(usuario.getUsername(), response.getUsername());
        assertEquals(usuario.getEmail(), response.getEmail());
        assertTrue(response.isEnabled());
        assertTrue(response.getRoles().contains("ROLE_ADMIN"));
        assertEquals(1, response.getRoles().size());
    }

    @Test
    @DisplayName("Debe devolver null si la entidad Usuario es nula")
    void testToResponse_EntityNula_DebeDevolverNulo() {
        // ACT
        UsuarioResponse response = usuarioMapper.toResponse(null);

        // ASSERT
        assertNull(response, "Debe devolver null si la entrada es null");
    }
}