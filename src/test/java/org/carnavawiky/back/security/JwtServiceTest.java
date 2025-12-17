package org.carnavawiky.back.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class JwtServiceTest {

    private JwtService jwtService;

    // Clave de prueba (debe tener al menos 32 caracteres para HS256)
    private final String SECRET_KEY = "mi_clave_secreta_super_segura_para_el_test_de_carnaval";
    private final Long EXPIRATION_MINUTES = 15L;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        // Inyectamos manualmente los valores de @Value usando ReflectionTestUtils
        ReflectionTestUtils.setField(jwtService, "jwtSecretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtService, "jwtExpirationMinutes", EXPIRATION_MINUTES);
    }

    @Test
    @DisplayName("Debe generar un token JWT válido")
    void testGenerateAccessToken() {
        // ARRANGE
        Authentication auth = Mockito.mock(Authentication.class);
        when(auth.getName()).thenReturn("usuarioTest");
        // Simulamos un rol
        when(auth.getAuthorities()).thenAnswer(invocation ->
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        // ACT
        String token = jwtService.generateAccessToken(auth);

        // ASSERT
        assertNotNull(token);
        assertTrue(token.length() > 0);
        assertTrue(jwtService.validateToken(token));
        assertEquals("usuarioTest", jwtService.getUsernameFromToken(token));
    }

    @Test
    @DisplayName("Debe validar correctamente un token real")
    void testValidateToken_ValidToken() {
        // Generamos un token primero
        Authentication auth = Mockito.mock(Authentication.class);
        when(auth.getName()).thenReturn("admin");
        when(auth.getAuthorities()).thenAnswer(i -> Collections.emptyList());

        String token = jwtService.generateAccessToken(auth);

        // Verificamos validación
        assertTrue(jwtService.validateToken(token));
    }

    @Test
    @DisplayName("Debe fallar al validar un token mal formado o alterado")
    void testValidateToken_InvalidToken() {
        String tokenInvalido = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.token.falso";

        assertFalse(jwtService.validateToken(tokenInvalido));
    }

    @Test
    @DisplayName("Debe extraer el nombre de usuario correctamente del token")
    void testGetUsernameFromToken() {
        // ARRANGE
        String expectedUsername = "carnavalero77";
        Authentication auth = Mockito.mock(Authentication.class);
        when(auth.getName()).thenReturn(expectedUsername);
        when(auth.getAuthorities()).thenAnswer(i -> Collections.emptyList());

        String token = jwtService.generateAccessToken(auth);

        // ACT
        String username = jwtService.getUsernameFromToken(token);

        // ASSERT
        assertEquals(expectedUsername, username);
    }

    @Test
    @DisplayName("Debe calcular los segundos de expiración correctamente")
    void testGetExpirationSeconds() {
        // 15 minutos * 60 segundos = 900
        Long expectedSeconds = 15 * 60L;
        assertEquals(expectedSeconds, jwtService.getExpirationSeconds());
    }

    @Test
    @DisplayName("Debe fallar si el token ha sido manipulado")
    void testValidateToken_TamperedToken() {
        // 1. Generar token original
        Authentication auth = Mockito.mock(Authentication.class);
        when(auth.getName()).thenReturn("user");
        when(auth.getAuthorities()).thenAnswer(i -> Collections.emptyList());
        String token = jwtService.generateAccessToken(auth);

        // 2. Manipular el token (cambiar un caracter de la firma)
        String tamperedToken = token.substring(0, token.length() - 1) + (token.endsWith("a") ? "b" : "a");

        // 3. Verificar que es inválido
        assertFalse(jwtService.validateToken(tamperedToken));
    }
}