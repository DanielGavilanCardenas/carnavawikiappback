package org.carnavawiky.back.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    private Usuario usuario;
    private Role roleUser;
    private Role roleAdmin;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");
        usuario.setEmail("test@example.com");
        usuario.setPassword("password123");
        usuario.setEnabled(true);

        roleUser = new Role();
        roleUser.setId(1L);
        roleUser.setName(Role.RoleName.ROLE_USER);

        roleAdmin = new Role();
        roleAdmin.setId(2L);
        roleAdmin.setName(Role.RoleName.ROLE_ADMIN);
    }

    @Test
    @DisplayName("Debe devolver las autoridades correctas basadas en los roles")
    void testGetAuthorities() {
        Set<Role> roles = new HashSet<>();
        roles.add(roleUser);
        roles.add(roleAdmin);
        usuario.setRoles(roles);

        Collection<? extends GrantedAuthority> authorities = usuario.getAuthorities();

        assertNotNull(authorities);
        assertEquals(2, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    @DisplayName("Debe devolver una lista vacía de autoridades si no tiene roles")
    void testGetAuthorities_SinRoles() {
        usuario.setRoles(new HashSet<>());

        Collection<? extends GrantedAuthority> authorities = usuario.getAuthorities();

        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }

    @Test
    @DisplayName("Debe cumplir con los métodos de UserDetails")
    void testUserDetailsMethods() {
        assertTrue(usuario.isAccountNonExpired());
        assertTrue(usuario.isAccountNonLocked());
        assertTrue(usuario.isCredentialsNonExpired());
        assertTrue(usuario.isEnabled()); // Verifica el getter generado por Lombok para el campo boolean
    }

    @Test
    @DisplayName("Debe manejar correctamente los campos de token de reseteo")
    void testResetTokenFields() {
        String token = "reset-token-123";
        LocalDateTime expiry = LocalDateTime.now().plusHours(1);

        usuario.setResetToken(token);
        usuario.setResetTokenExpiryDate(expiry);

        assertEquals(token, usuario.getResetToken());
        assertEquals(expiry, usuario.getResetTokenExpiryDate());
    }

    @Test
    @DisplayName("Debe manejar correctamente el token de activación")
    void testActivationTokenField() {
        String token = "activation-token-xyz";
        usuario.setActivationToken(token);
        assertEquals(token, usuario.getActivationToken());
    }

    @Test
    @DisplayName("Debe tener fecha de alta por defecto al instanciar")
    void testFechaAltaDefault() {
        Usuario nuevoUsuario = new Usuario();
        assertNotNull(nuevoUsuario.getFechaAlta());
        // Verificamos que sea reciente (margen de 1 segundo)
        assertTrue(nuevoUsuario.getFechaAlta().isAfter(LocalDateTime.now().minusSeconds(1)));
        assertTrue(nuevoUsuario.getFechaAlta().isBefore(LocalDateTime.now().plusSeconds(1)));
    }
}