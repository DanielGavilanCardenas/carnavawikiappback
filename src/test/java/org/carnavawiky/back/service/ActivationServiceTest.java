package org.carnavawiky.back.service;

import org.carnavawiky.back.exception.BadRequestException;
import org.carnavawiky.back.exception.ResourceNotFoundException;
import org.carnavawiky.back.model.Usuario;
import org.carnavawiky.back.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivationServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ActivationService activationService;

    private Usuario usuario;
    private static final String TOKENPRUEBA = "token-uuid-123";

    @BeforeEach
    void setUp() {
        // Inyectamos la URL base manualmente
        ReflectionTestUtils.setField(activationService, "baseUrl", "http://localhost:8083");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("carnavalero");
        usuario.setEmail("test@carnavawiky.org");
        usuario.setEnabled(false);
        usuario.setActivationToken(TOKENPRUEBA);
    }

    // =======================================================
    // TESTS PARA generateAndSendActivationEmail
    // =======================================================

    @Test
    @DisplayName("Debe generar token y enviar email correctamente")
    void testGenerateAndSendActivationEmail_Success() {
        // ACT
        activationService.generateAndSendActivationEmail(usuario);

        // ASSERT
        assertNotNull(usuario.getActivationToken(), "Se debe haber asignado un token al usuario.");
        verify(usuarioRepository, times(1)).save(usuario);
        verify(emailService, times(1)).sendEmail(
                eq(usuario.getEmail()),
                contains("Activación"),
                contains(usuario.getActivationToken())
        );
    }

    // =======================================================
    // TESTS PARA activateUser
    // =======================================================

    @Test
    @DisplayName("Debe activar al usuario y limpiar el token si el token es válido")
    void testActivateUser_Success() {
        // ARRANGE
        when(usuarioRepository.findByActivationToken(TOKENPRUEBA)).thenReturn(Optional.of(usuario));

        // ACT
        activationService.activateUser(TOKENPRUEBA);

        // ASSERT
        assertTrue(usuario.isEnabled(), "El usuario debería estar habilitado.");
        assertNull(usuario.getActivationToken(), "El token debería haberse limpiado.");
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException si el token no existe")
    void testActivateUser_TokenNotFound() {
        // ARRANGE
        when(usuarioRepository.findByActivationToken("token_falso")).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(ResourceNotFoundException.class, () -> {
            activationService.activateUser("token_falso");
        });

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar BadRequestException si el usuario ya estaba activo")
    void testActivateUser_AlreadyEnabled() {
        // ARRANGE
        usuario.setEnabled(true); // Usuario ya activo
        when(usuarioRepository.findByActivationToken(TOKENPRUEBA)).thenReturn(Optional.of(usuario));

        // ACT & ASSERT
        assertThrows(BadRequestException.class, () -> {
            activationService.activateUser(TOKENPRUEBA);
        });

        verify(usuarioRepository, never()).save(any());
    }
}