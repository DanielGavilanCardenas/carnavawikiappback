package org.carnavawiky.back.service;

import org.carnavawiky.back.dto.PageResponse;
import org.carnavawiky.back.dto.UsuarioRequest;
import org.carnavawiky.back.dto.UsuarioResponse;
import org.carnavawiky.back.exception.BadRequestException;

import org.carnavawiky.back.mapper.UsuarioMapper;
import org.carnavawiky.back.model.Role;

import org.carnavawiky.back.model.Usuario;
import org.carnavawiky.back.repository.RoleRepository;
import org.carnavawiky.back.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UsuarioMapper usuarioMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioResponse usuarioResponse;
    private UsuarioRequest usuarioRequest;
    private Role userRole;

    @BeforeEach
    void setUp() {
        userRole = new Role();
        userRole.setId(1L);
        userRole.setName(Role.RoleName.ROLE_USER);

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");
        usuario.setEmail("test@test.com");
        usuario.setPassword("encoded_pass");

        usuarioResponse = new UsuarioResponse();
        usuarioResponse.setId(1L);
        usuarioResponse.setUsername("testuser");

        usuarioRequest = new UsuarioRequest();
        usuarioRequest.setUsername("testuser");
        usuarioRequest.setEmail("test@test.com");
        usuarioRequest.setPassword("new_pass");
        usuarioRequest.setRoleIds(Set.of(1L));
        usuarioRequest.setEnabled(true);
    }

    @Test
    @DisplayName("Debe obtener todos los usuarios con paginación y búsqueda")
    void testObtenerTodos() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Usuario> page = new PageImpl<>(List.of(usuario));

        when(usuarioRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(anyString(), anyString(), any()))
                .thenReturn(page);
        when(usuarioMapper.toResponse(any())).thenReturn(usuarioResponse);

        PageResponse<UsuarioResponse> result = usuarioService.obtenerTodosUsuarios(pageable, "test");

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(usuarioRepository).findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(any(), any(), any());
    }

    @Test
    @DisplayName("Debe lanzar excepción al crear si el username ya existe")
    void testCrearUsuario_UsernameExistente() {
        when(usuarioRepository.existsByUsername(anyString())).thenReturn(true);
        assertThrows(BadRequestException.class, () -> usuarioService.crearUsuario(usuarioRequest));
    }

    @Test
    @DisplayName("Debe crear usuario correctamente")
    void testCrearUsuario_Exito() {
        when(usuarioRepository.existsByUsername(anyString())).thenReturn(false);
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(usuarioRepository.save(any())).thenReturn(usuario);
        when(usuarioMapper.toResponse(any())).thenReturn(usuarioResponse);

        UsuarioResponse result = usuarioService.crearUsuario(usuarioRequest);

        assertNotNull(result);
        verify(usuarioRepository).save(any());
        verify(passwordEncoder).encode("new_pass");
    }

    @Test
    @DisplayName("Debe actualizar usuario correctamente incluyendo cambio de password")
    void testActualizarUsuario_Exito() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode(anyString())).thenReturn("new_encoded");
        when(usuarioRepository.save(any())).thenReturn(usuario);
        when(usuarioMapper.toResponse(any())).thenReturn(usuarioResponse);

        UsuarioResponse result = usuarioService.actualizarUsuario(1L, usuarioRequest);

        assertNotNull(result);
        verify(passwordEncoder).encode("new_pass");
        verify(usuarioRepository).save(any());
    }

    @Test
    @DisplayName("Debe eliminar usuario si existe")
    void testEliminarUsuario() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(1L);

        assertDoesNotThrow(() -> usuarioService.eliminarUsuario(1L));
        verify(usuarioRepository).deleteById(1L);
    }
}