package org.carnavawiky.back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.carnavawiky.back.config.FileStorageProperties;
import org.carnavawiky.back.config.SecurityConfig;
import org.carnavawiky.back.config.WebConfig;
import org.carnavawiky.back.dto.auth.LoginRequest;
import org.carnavawiky.back.dto.auth.PasswordResetRequest;
import org.carnavawiky.back.dto.auth.RegisterRequest;
import org.carnavawiky.back.dto.auth.TokenResponse;
import org.carnavawiky.back.model.Usuario;
import org.carnavawiky.back.repository.RoleRepository;
import org.carnavawiky.back.repository.UsuarioRepository;
import org.carnavawiky.back.security.JwtService;
import org.carnavawiky.back.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = AuthController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {WebConfig.class})
})
@Import({SecurityConfig.class, JwtService.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    // Mocks necesarios para el contexto de seguridad y configuración
    @MockBean
    private FileStorageProperties fileStorageProperties;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private TokenResponse tokenResponse;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("nuevo_usuario");
        registerRequest.setEmail("nuevo@example.com");
        registerRequest.setPassword("password123");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("usuario_existente");
        loginRequest.setPassword("password123");

        tokenResponse = TokenResponse.builder()
                .accessToken("access-token-xyz")
                .refreshToken("refresh-token-abc")
                .tokenType("Bearer")
                .expiresIn(3600L)
                .build();
    }

    // =======================================================
    // 1. REGISTRO (POST)
    // =======================================================
    @Test
    @DisplayName("Debe registrar un usuario correctamente")
    void testRegisterUser_Ok() throws Exception {

        when(authService.register(any(RegisterRequest.class))).thenReturn(new Usuario());

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Usuario registrado exitosamente. Por favor, revisa tu email para activar la cuenta."));
    }

    // =======================================================
    // 2. LOGIN (POST)
    // =======================================================
    @Test
    @DisplayName("Debe autenticar un usuario y devolver tokens")
    void testAuthenticateUser_Ok() throws Exception {
        when(authService.login(any(LoginRequest.class))).thenReturn(tokenResponse);

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token-xyz"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token-abc"));
    }

    // =======================================================
    // 3. ACTIVACIÓN (GET)
    // =======================================================
    @Test
    @DisplayName("Debe activar la cuenta con un token válido")
    void testActivateAccount_Ok() throws Exception {
        doNothing().when(authService).activateAccount(anyString());

        mockMvc.perform(get("/api/auth/activate/token-valido"))
                .andExpect(status().isOk())
                .andExpect(content().string("Cuenta activada exitosamente. ¡Bienvenido! Ya puedes iniciar sesión."));
    }

    // =======================================================
    // 4. REFRESH TOKEN (POST)
    // =======================================================
    @Test
    @DisplayName("Debe refrescar el token de acceso")
    void testRefreshToken_Ok() throws Exception {
        TokenResponse refreshRequest = TokenResponse.builder()
                .refreshToken("refresh-token-abc")
                .build();

        when(authService.refreshToken("refresh-token-abc")).thenReturn(tokenResponse);

        mockMvc.perform(post("/api/auth/refreshtoken")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token-xyz"));
    }

    // =======================================================
    // 5. SOLICITUD DE RESETEO (POST)
    // =======================================================
    @Test
    @DisplayName("Debe solicitar reseteo de contraseña")
    void testRequestPasswordReset_Ok() throws Exception {
        doNothing().when(authService).requestPasswordReset(anyString());

        mockMvc.perform(post("/api/auth/forgot-password")
                        .with(csrf())
                        .contentType(MediaType.TEXT_PLAIN) // El controlador espera un String directo, no JSON
                        .content("usuario@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("Si la dirección de email está registrada, se ha enviado un enlace para restablecer la contraseña."));
    }

    // =======================================================
    // 6. RESETEO FINAL (POST)
    // =======================================================
    @Test
    @DisplayName("Debe restablecer la contraseña correctamente")
    void testResetPassword_Ok() throws Exception {
        PasswordResetRequest resetRequest = new PasswordResetRequest();
        resetRequest.setToken("token-reseteo");
        resetRequest.setNewPassword("nuevaPassword123");

        doNothing().when(authService).resetPassword(any(PasswordResetRequest.class));

        mockMvc.perform(post("/api/auth/reset-password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resetRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Contraseña restablecida exitosamente. Ahora puedes iniciar sesión con tu nueva contraseña."));
    }
}