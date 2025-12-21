package org.carnavawiky.back.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RefreshTokenTest {

    private RefreshToken refreshToken;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        // Objeto de apoyo
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("usuario_test");

        // Entidad a probar
        refreshToken = new RefreshToken();
    }

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        Instant expiryDate = Instant.now().plusSeconds(3600);
        String tokenString = "token-uuid-12345";

        // Establecer valores
        refreshToken.setId(10L);
        refreshToken.setToken(tokenString);
        refreshToken.setExpiryDate(expiryDate);
        refreshToken.setUsuario(usuario);

        // Verificar valores
        assertNotNull(refreshToken);
        assertEquals(10L, refreshToken.getId());
        assertEquals(tokenString, refreshToken.getToken());
        assertEquals(expiryDate, refreshToken.getExpiryDate());
        assertEquals(usuario, refreshToken.getUsuario());
    }
}