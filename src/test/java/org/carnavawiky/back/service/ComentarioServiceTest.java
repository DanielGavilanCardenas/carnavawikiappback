package org.carnavawiky.back.service;

import org.carnavawiky.back.dto.ComentarioRequest;
import org.carnavawiky.back.dto.ComentarioResponse;
import org.carnavawiky.back.dto.PageResponse;
import org.carnavawiky.back.exception.ResourceNotFoundException;
import org.carnavawiky.back.mapper.ComentarioMapper;
import org.carnavawiky.back.model.Agrupacion;
import org.carnavawiky.back.model.Comentario;
import org.carnavawiky.back.model.Usuario;
import org.carnavawiky.back.repository.AgrupacionRepository;
import org.carnavawiky.back.repository.ComentarioRepository;
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

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComentarioServiceTest {

    @Mock
    private ComentarioRepository comentarioRepository;

    @Mock
    private AgrupacionRepository agrupacionRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ComentarioMapper comentarioMapper;

    @InjectMocks
    private ComentarioService comentarioService;

    private Comentario comentario;
    private Usuario usuario;
    private Agrupacion agrupacion;
    private ComentarioRequest comentarioRequest;
    private ComentarioResponse comentarioResponse;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("dani");

        agrupacion = new Agrupacion();
        agrupacion.setId(10L);
        agrupacion.setNombre("Los Sombrereros");

        comentario = new Comentario();
        comentario.setId(100L);
        comentario.setContenido("¡Qué gran agrupación!");
        comentario.setAprobado(false);
        comentario.setUsuario(usuario);
        comentario.setAgrupacion(agrupacion);

        comentarioRequest = new ComentarioRequest();
        comentarioRequest.setContenido("¡Qué gran agrupación!");
        comentarioRequest.setUsuarioId(1L);
        comentarioRequest.setAgrupacionId(10L);

        comentarioResponse = new ComentarioResponse();
        comentarioResponse.setId(100L);
        comentarioResponse.setContenido("¡Qué gran agrupación!");
        comentarioResponse.setAprobado(false);
    }

    @Test
    @DisplayName("Debe crear un comentario (por defecto no aprobado)")
    void testCrearComentario_Exito() {
        // ARRANGE
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(agrupacionRepository.findById(10L)).thenReturn(Optional.of(agrupacion));
        when(comentarioMapper.toEntity(any(), any(), any())).thenReturn(comentario);
        when(comentarioRepository.save(any(Comentario.class))).thenReturn(comentario);
        when(comentarioMapper.toResponse(any(Comentario.class))).thenReturn(comentarioResponse);

        // ACT
        ComentarioResponse result = comentarioService.crearComentario(comentarioRequest);

        // ASSERT
        assertNotNull(result);
        assertFalse(result.getAprobado());
        verify(comentarioRepository).save(any(Comentario.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción si el usuario no existe al comentar")
    void testCrearComentario_UsuarioNoEncontrado() {
        // ARRANGE
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(ResourceNotFoundException.class, () -> comentarioService.crearComentario(comentarioRequest));
        verify(comentarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe listar comentarios paginados")
    void testListarComentarios_Paginado() {
        // ARRANGE
        Pageable pageable = PageRequest.of(0, 10);
        Page<Comentario> page = new PageImpl<>(Collections.singletonList(comentario));

        when(comentarioRepository.findAll(pageable)).thenReturn(page);
        when(comentarioMapper.toResponse(any())).thenReturn(comentarioResponse);

        // ACT
        PageResponse<ComentarioResponse> result = comentarioService.obtenerTodosComentarios(pageable, null);

        // ASSERT
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(comentarioRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Debe permitir a un ADMIN aprobar un comentario")
    void testAprobarComentario_Exito() {
        // ARRANGE
        when(comentarioRepository.findById(100L)).thenReturn(Optional.of(comentario));

        // Simulamos que al guardar, el estado cambia a aprobado
        ComentarioResponse aprobadoResponse = new ComentarioResponse();
        aprobadoResponse.setId(100L);
        aprobadoResponse.setAprobado(true);

        when(comentarioRepository.save(any(Comentario.class))).thenReturn(comentario);
        when(comentarioMapper.toResponse(any())).thenReturn(aprobadoResponse);

        // ACT
        ComentarioResponse result = comentarioService.aprobarComentario(100L);

        // ASSERT
        assertTrue(result.getAprobado());
        verify(comentarioRepository).save(comentario);
        assertTrue(comentario.getAprobado()); // Verifica que la entidad cambió
    }

    @Test
    @DisplayName("Debe eliminar un comentario correctamente")
    void testEliminarComentario_Exito() {
        // ARRANGE
        when(comentarioRepository.findById(100L)).thenReturn(Optional.of(comentario));
        doNothing().when(comentarioRepository).delete(comentario);

        // ACT
        comentarioService.eliminarComentario(100L);

        // ASSERT
        verify(comentarioRepository, times(1)).delete(comentario);
    }
}