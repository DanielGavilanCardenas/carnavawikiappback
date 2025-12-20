package org.carnavawiky.back.service;

import jakarta.persistence.EntityManager;
import org.carnavawiky.back.exception.TokenRefreshException;
import org.carnavawiky.back.model.RefreshToken;
import org.carnavawiky.back.model.Usuario;
import org.carnavawiky.back.repository.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    private Usuario usuario;
    private RefreshToken refreshToken;

    @BeforeEach
    void setUp() {
        // Inyectamos el valor de expiración (7 días)
        ReflectionTestUtils.setField(refreshTokenService, "refreshTokenDurationDays", 7L);

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");

        refreshToken = new RefreshToken();
        refreshToken.setToken("token-unico-123");
        refreshToken.setUsuario(usuario);
        refreshToken.setExpiryDate(Instant.now().plusSeconds(600)); // Válido por 10 min
    }

    @Test
    @DisplayName("Debe crear un nuevo Refresh Token y eliminar el antiguo si existe")
    void testCreateRefreshToken_Success() {
        // ARRANGE
        // Simulamos que ya existe un token previo para este usuario
        RefreshToken oldToken = new RefreshToken();
        when(refreshTokenRepository.findByUsuario(usuario)).thenReturn(Optional.of(oldToken));

        // El save devuelve el token creado (usamos any() para capturar el nuevo objeto)
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(i -> i.getArguments()[0]);

        // ACT
        RefreshToken result = refreshTokenService.createRefreshToken(usuario);

        // ASSERT
        assertNotNull(result);
        assertNotNull(result.getToken());
        assertEquals(usuario, result.getUsuario());

        // Verificamos que se realizó la limpieza necesaria
        verify(refreshTokenRepository).delete(oldToken);
        verify(entityManager).flush(); // Crucial para evitar Duplicate Entry
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    @DisplayName("Debe buscar un token correctamente")
    void testFindByToken() {
        when(refreshTokenRepository.findByToken("token-123")).thenReturn(Optional.of(refreshToken));

        Optional<RefreshToken> result = refreshTokenService.findByToken("token-123");

        assertTrue(result.isPresent());
        assertEquals("token-unico-123", result.get().getToken());
    }

    @Test
    @DisplayName("verifyExpiration debe devolver el token si no ha expirado")
    void testVerifyExpiration_Valid() {
        RefreshToken result = refreshTokenService.verifyExpiration(refreshToken);
        assertEquals(refreshToken, result);
        verify(refreshTokenRepository, never()).delete(any());
    }

    @Test
    @DisplayName("verifyExpiration debe lanzar excepción y borrar el token si ha expirado")
    void testVerifyExpiration_Expired() {
        // Forzamos expiración poniendo la fecha en el pasado
        refreshToken.setExpiryDate(Instant.now().minusSeconds(10));

        assertThrows(TokenRefreshException.class, () -> {
            refreshTokenService.verifyExpiration(refreshToken);
        });

        verify(refreshTokenRepository).delete(refreshToken);
    }
}