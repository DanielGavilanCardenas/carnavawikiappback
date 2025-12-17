package org.carnavawiky.back.security;

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
class UserDetailsServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setUsername("admin");
        usuario.setPassword("password_encriptada");
        usuario.setEnabled(true);
    }

    @Test
    @DisplayName("Debe cargar UserDetails correctamente si el usuario existe")
    void testLoadUserByUsername_Success() {
        // ARRANGE
        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.of(usuario));

        // ACT
        UserDetails resultado = userDetailsService.loadUserByUsername("admin");

        // ASSERT
        assertNotNull(resultado);
        assertEquals("admin", resultado.getUsername());
        verify(usuarioRepository, times(1)).findByUsername("admin");
    }

    @Test
    @DisplayName("Debe lanzar UsernameNotFoundException si el usuario no existe")
    void testLoadUserByUsername_NotFound() {
        // ARRANGE
        when(usuarioRepository.findByUsername("user_falso")).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("user_falso");
        });

        verify(usuarioRepository, times(1)).findByUsername("user_falso");
    }
}