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

    // =======================================================
    // TESTS PARA COBERTURA DE MÉTODOS DE LOMBOK (@Data)
    // =======================================================

    @Test
    @DisplayName("Debe cubrir todas las ramas de equals y hashCode variando cada campo")
    void testEqualsAndHashCode_Exhaustivo() {
        LocalDateTime fecha = LocalDateTime.of(2024, 1, 1, 12, 0);
        LocalDateTime expiry = LocalDateTime.of(2024, 1, 2, 12, 0);
        Set<Role> roles1 = new HashSet<>();
        Set<Role> roles2 = new HashSet<>(); roles2.add(roleUser);

        // Objeto base (Usamos setters)
        Usuario u1 = new Usuario();
        u1.setId(1L);
        u1.setUsername("user1");
        u1.setEmail("email1@test.com");
        u1.setPassword("pass1");
        u1.setFechaAlta(fecha);
        u1.setEnabled(true);
        u1.setActivationToken("act-1");
        u1.setResetToken("res-1");
        u1.setResetTokenExpiryDate(expiry);
        u1.setRoles(roles1);

        // Copia exacta
        Usuario u2 = new Usuario();
        u2.setId(1L);
        u2.setUsername("user1");
        u2.setEmail("email1@test.com");
        u2.setPassword("pass1");
        u2.setFechaAlta(fecha);
        u2.setEnabled(true);
        u2.setActivationToken("act-1");
        u2.setResetToken("res-1");
        u2.setResetTokenExpiryDate(expiry);
        u2.setRoles(roles1);

        // --- IGUALDAD BÁSICA ---
        assertEquals(u1, u1); // Reflexivo
        assertEquals(u1, u2); // Simétrico
        assertEquals(u2, u1);
        assertEquals(u1.hashCode(), u2.hashCode()); // HashCode consistente

        assertNotEquals(u1, null); // No igual a null
        assertNotEquals(u1, new Object()); // No igual a otro tipo

        // --- VARIACIONES CAMPO A CAMPO ---

        // 1. ID
        Usuario uDiffId = copiarUsuario(u1); uDiffId.setId(2L);
        assertNotEquals(u1, uDiffId);
        assertNotEquals(u1.hashCode(), uDiffId.hashCode());
        
        Usuario uIdNull = copiarUsuario(u1); uIdNull.setId(null);
        assertNotEquals(u1, uIdNull);
        assertNotEquals(uIdNull, u1);

        // 2. Username
        Usuario uDiffUser = copiarUsuario(u1); uDiffUser.setUsername("otro");
        assertNotEquals(u1, uDiffUser);
        assertNotEquals(u1.hashCode(), uDiffUser.hashCode());
        
        Usuario uUserNull = copiarUsuario(u1); uUserNull.setUsername(null);
        assertNotEquals(u1, uUserNull);
        assertNotEquals(uUserNull, u1);

        // 3. Email
        Usuario uDiffEmail = copiarUsuario(u1); uDiffEmail.setEmail("otro@test.com");
        assertNotEquals(u1, uDiffEmail);
        assertNotEquals(u1.hashCode(), uDiffEmail.hashCode());
        
        Usuario uEmailNull = copiarUsuario(u1); uEmailNull.setEmail(null);
        assertNotEquals(u1, uEmailNull);
        assertNotEquals(uEmailNull, u1);

        // 4. Password
        Usuario uDiffPass = copiarUsuario(u1); uDiffPass.setPassword("otraPass");
        assertNotEquals(u1, uDiffPass);
        assertNotEquals(u1.hashCode(), uDiffPass.hashCode());
        
        Usuario uPassNull = copiarUsuario(u1); uPassNull.setPassword(null);
        assertNotEquals(u1, uPassNull);
        assertNotEquals(uPassNull, u1);

        // 5. FechaAlta
        Usuario uDiffFecha = copiarUsuario(u1); uDiffFecha.setFechaAlta(fecha.plusDays(1));
        assertNotEquals(u1, uDiffFecha);
        assertNotEquals(u1.hashCode(), uDiffFecha.hashCode());
        
        Usuario uFechaNull = copiarUsuario(u1); uFechaNull.setFechaAlta(null);
        assertNotEquals(u1, uFechaNull);
        assertNotEquals(uFechaNull, u1);

        // 6. Enabled (booleano primitivo, no puede ser null, solo true/false)
        Usuario uDiffEnabled = copiarUsuario(u1); uDiffEnabled.setEnabled(false);
        assertNotEquals(u1, uDiffEnabled);
        assertNotEquals(u1.hashCode(), uDiffEnabled.hashCode());

        // 7. ActivationToken
        Usuario uDiffAct = copiarUsuario(u1); uDiffAct.setActivationToken("otro-act");
        assertNotEquals(u1, uDiffAct);
        assertNotEquals(u1.hashCode(), uDiffAct.hashCode());
        
        Usuario uActNull = copiarUsuario(u1); uActNull.setActivationToken(null);
        assertNotEquals(u1, uActNull);
        assertNotEquals(uActNull, u1);

        // 8. ResetToken
        Usuario uDiffRes = copiarUsuario(u1); uDiffRes.setResetToken("otro-res");
        assertNotEquals(u1, uDiffRes);
        assertNotEquals(u1.hashCode(), uDiffRes.hashCode());
        
        Usuario uResNull = copiarUsuario(u1); uResNull.setResetToken(null);
        assertNotEquals(u1, uResNull);
        assertNotEquals(uResNull, u1);

        // 9. ResetTokenExpiryDate
        Usuario uDiffExp = copiarUsuario(u1); uDiffExp.setResetTokenExpiryDate(expiry.plusDays(1));
        assertNotEquals(u1, uDiffExp);
        assertNotEquals(u1.hashCode(), uDiffExp.hashCode());
        
        Usuario uExpNull = copiarUsuario(u1); uExpNull.setResetTokenExpiryDate(null);
        assertNotEquals(u1, uExpNull);
        assertNotEquals(uExpNull, u1);

        // 10. Roles
        Usuario uDiffRoles = copiarUsuario(u1); uDiffRoles.setRoles(roles2);
        assertNotEquals(u1, uDiffRoles);
        assertNotEquals(u1.hashCode(), uDiffRoles.hashCode());
        
        Usuario uRolesNull = copiarUsuario(u1); uRolesNull.setRoles(null);
        assertNotEquals(u1, uRolesNull);
        assertNotEquals(uRolesNull, u1);
        
        // 11. Roles Null vs Null
        Usuario uRolesNull2 = copiarUsuario(u1); uRolesNull2.setRoles(null);
        assertEquals(uRolesNull, uRolesNull2);
    }

    // Método auxiliar para clonar un usuario y modificar solo un campo
    private Usuario copiarUsuario(Usuario original) {
        Usuario copia = new Usuario();
        copia.setId(original.getId());
        copia.setUsername(original.getUsername());
        copia.setEmail(original.getEmail());
        copia.setPassword(original.getPassword());
        copia.setFechaAlta(original.getFechaAlta());
        copia.setEnabled(original.isEnabled());
        copia.setActivationToken(original.getActivationToken());
        copia.setResetToken(original.getResetToken());
        copia.setResetTokenExpiryDate(original.getResetTokenExpiryDate());
        copia.setRoles(original.getRoles()); // Referencia al mismo set (cuidado si se modifica el set)
        return copia;
    }

    @Test
    @DisplayName("Debe generar una representación en String")
    void testToString() {
        String usuarioString = usuario.toString();
        assertNotNull(usuarioString);
        assertTrue(usuarioString.contains("username=testuser"));
        assertTrue(usuarioString.contains("email=test@example.com"));
    }

    @Test
    @DisplayName("Debe cumplir con canEqual para la simetría de equals")
    void testCanEqual() {
        Usuario otroUsuario = new Usuario();
        assertTrue(usuario.canEqual(otroUsuario));
        assertFalse(usuario.canEqual(new Object()));
    }
}