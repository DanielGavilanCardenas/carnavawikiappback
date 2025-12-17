package org.carnavawiky.back.service;

import org.carnavawiky.back.dto.auth.LoginRequest;
import org.carnavawiky.back.dto.auth.PasswordResetRequest;
import org.carnavawiky.back.dto.auth.RegisterRequest;
import org.carnavawiky.back.dto.auth.TokenResponse;
import org.carnavawiky.back.exception.BadRequestException;
import org.carnavawiky.back.model.Role;
import org.carnavawiky.back.model.Usuario;
import org.carnavawiky.back.repository.RoleRepository;
import org.carnavawiky.back.repository.UsuarioRepository;
import org.carnavawiky.back.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UsuarioRepository usuarioRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private JwtService jwtService;
    @Mock private RefreshTokenService refreshTokenService;
    @Mock private ActivationService activationService;
    @Mock private EmailService emailService;

    @InjectMocks
    private AuthService authService;

    private Usuario usuario;
    private Role userRole;

    @BeforeEach
    void setUp() {
        // Inyectar base-url manualmente ya que viene de @Value
        ReflectionTestUtils.setField(authService, "baseUrl", "http://localhost:8080");

        userRole = new Role();
        userRole.setName(Role.RoleName.ROLE_USER);

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");
        usuario.setEmail("test@test.com");
        usuario.setPassword("encodedPassword");
    }

    // =======================================================
    // TESTS DE REGISTRO
    // =======================================================

    @Test
    @DisplayName("Registro exitoso de un nuevo usuario")
    void testRegister_Success() {
        RegisterRequest request = new RegisterRequest("testuser", "test@test.com", "password123");

        when(usuarioRepository.existsByUsername(anyString())).thenReturn(false);
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(roleRepository.findByName(Role.RoleName.ROLE_USER)).thenReturn(Optional.of(userRole));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = authService.register(request);

        assertNotNull(resultado);
        verify(activationService, times(1)).generateAndSendActivationEmail(any(Usuario.class));
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Error al registrar si el nombre de usuario ya existe")
    void testRegister_UsernameExists() {
        RegisterRequest request = new RegisterRequest("testuser", "test@test.com", "password123");
        when(usuarioRepository.existsByUsername("testuser")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.register(request));
        verify(usuarioRepository, never()).save(any());
    }

    // =======================================================
    // TESTS DE LOGIN
    // =======================================================

    @Test
    @DisplayName("Login exitoso devuelve tokens")
    void testLogin_Success() {
        LoginRequest request = new LoginRequest("testuser", "password123");
        Authentication auth = mock(Authentication.class);

        // Mock de RefreshToken para evitar NullPointerException en el servicio
        org.carnavawiky.back.model.RefreshToken rt = new org.carnavawiky.back.model.RefreshToken();
        rt.setToken("refresh-token-123");

        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));
        when(jwtService.generateAccessToken(auth)).thenReturn("access-token");
        when(refreshTokenService.createRefreshToken(usuario)).thenReturn(rt);
        when(jwtService.getExpirationSeconds()).thenReturn(900L);

        TokenResponse response = authService.login(request);

        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token-123", response.getRefreshToken());
    }

    // =======================================================
    // TESTS DE RECUPERACIÓN DE CONTRASEÑA
    // =======================================================

    @Test
    @DisplayName("Solicitud de reseteo envía email")
    void testRequestPasswordReset_Success() {
        String email = "test@test.com";
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        authService.requestPasswordReset(email);

        verify(usuarioRepository).save(usuario);
        assertNotNull(usuario.getResetToken());
        verify(emailService).sendEmail(eq(email), anyString(), anyString());
    }

    @Test
    @DisplayName("Reset de contraseña exitoso")
    void testResetPassword_Success() {
        PasswordResetRequest request = new PasswordResetRequest("token-valido", "newPass123");
        usuario.setResetToken("token-valido");
        usuario.setResetTokenExpiryDate(LocalDateTime.now().plusHours(1));

        when(usuarioRepository.findByResetToken("token-valido")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode("newPass123")).thenReturn("newEncodedPass");

        authService.resetPassword(request);

        assertEquals("newEncodedPass", usuario.getPassword());
        assertNull(usuario.getResetToken()); // Debe limpiarse tras el uso
        verify(usuarioRepository).save(usuario);
    }

    @Test
    @DisplayName("Error al resetear con token expirado")
    void testResetPassword_ExpiredToken() {
        PasswordResetRequest request = new PasswordResetRequest("token-expirado", "newPass");
        usuario.setResetToken("token-expirado");
        usuario.setResetTokenExpiryDate(LocalDateTime.now().minusMinutes(1)); // Expirado

        when(usuarioRepository.findByResetToken("token-expirado")).thenReturn(Optional.of(usuario));

        assertThrows(BadRequestException.class, () -> authService.resetPassword(request));
    }
}