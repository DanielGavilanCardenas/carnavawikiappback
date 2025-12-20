package org.carnavawiky.back.service;

import org.carnavawiky.back.model.Usuario;
import org.carnavawiky.back.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        // Configuramos un usuario de prueba que implemente UserDetails (tu modelo Usuario debe hacerlo)
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("dani");
        usuario.setPassword("password123");
    }

    @Test
    @DisplayName("Debe cargar un usuario correctamente cuando el username existe")
    void loadUserByUsername_CuandoExiste_RetornaUserDetails() {
        // ARRANGE
        when(usuarioRepository.findByUsername("dani")).thenReturn(Optional.of(usuario));

        // ACT
        UserDetails result = customUserDetailsService.loadUserByUsername("dani");

        // ASSERT
        assertNotNull(result);
        assertEquals("dani", result.getUsername());
        verify(usuarioRepository, times(1)).findByUsername("dani");
    }

    @Test
    @DisplayName("Debe lanzar UsernameNotFoundException cuando el username no existe")
    void loadUserByUsername_CuandoNoExiste_LanzaExcepcion() {
        // ARRANGE
        when(usuarioRepository.findByUsername("desconocido")).thenReturn(Optional.empty());

        // ACT & ASSERT
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("desconocido");
        });

        assertTrue(exception.getMessage().contains("Usuario no encontrado con username: desconocido"));
        verify(usuarioRepository, times(1)).findByUsername("desconocido");
    }
}