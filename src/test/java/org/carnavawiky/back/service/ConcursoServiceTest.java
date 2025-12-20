package org.carnavawiky.back.service;

import org.carnavawiky.back.dto.ConcursoRequest;
import org.carnavawiky.back.dto.ConcursoResponse;
import org.carnavawiky.back.dto.PageResponse;
import org.carnavawiky.back.exception.ResourceNotFoundException;
import org.carnavawiky.back.mapper.ConcursoMapper;
import org.carnavawiky.back.model.Concurso;
import org.carnavawiky.back.model.Localidad;
import org.carnavawiky.back.repository.ConcursoRepository;
import org.carnavawiky.back.repository.LocalidadRepository;
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
class ConcursoServiceTest {

    @Mock
    private ConcursoRepository concursoRepository;

    @Mock
    private LocalidadRepository localidadRepository;

    @Mock
    private ConcursoMapper concursoMapper;

    @InjectMocks
    private ConcursoService concursoService;

    private Concurso concurso;
    private Localidad localidad;
    private ConcursoRequest concursoRequest;
    private ConcursoResponse concursoResponse;

    @BeforeEach
    void setUp() {
        localidad = new Localidad();
        localidad.setId(1L);
        localidad.setNombre("Cádiz");

        concurso = new Concurso();
        concurso.setId(10L);
        concurso.setNombre("COAC");
        concurso.setEstaActivo(true);
        concurso.setLocalidad(localidad);

        concursoRequest = new ConcursoRequest();
        concursoRequest.setNombre("COAC");
        concursoRequest.setEstaActivo(true);
        concursoRequest.setLocalidadId(1L);

        concursoResponse = new ConcursoResponse();
        concursoResponse.setId(10L);
        concursoResponse.setNombre("COAC");
    }

    @Test
    @DisplayName("Debe crear un concurso correctamente")
    void testCrearConcurso_Exito() {
        // ARRANGE
        when(localidadRepository.findById(1L)).thenReturn(Optional.of(localidad));
        when(concursoMapper.toEntity(any(ConcursoRequest.class), any(Localidad.class))).thenReturn(concurso);
        when(concursoRepository.save(any(Concurso.class))).thenReturn(concurso);
        when(concursoMapper.toResponse(any(Concurso.class))).thenReturn(concursoResponse);

        // ACT
        ConcursoResponse result = concursoService.crearConcurso(concursoRequest);

        // ASSERT
        assertNotNull(result);
        assertEquals("COAC", result.getNombre());
        verify(concursoRepository).save(any(Concurso.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al crear si la localidad no existe")
    void testCrearConcurso_LocalidadNoEncontrada() {
        // ARRANGE
        when(localidadRepository.findById(1L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(ResourceNotFoundException.class, () -> concursoService.crearConcurso(concursoRequest));
        verify(concursoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe obtener todas las ediciones paginadas")
    void testObtenerTodos_Paginado() {
        // ARRANGE
        Pageable pageable = PageRequest.of(0, 10);
        Page<Concurso> page = new PageImpl<>(Collections.singletonList(concurso));

        when(concursoRepository.findAll(pageable)).thenReturn(page);
        when(concursoMapper.toResponse(any(Concurso.class))).thenReturn(concursoResponse);

        // ACT
        PageResponse<ConcursoResponse> result = concursoService.obtenerTodosConcursos(pageable, null);

        // ASSERT
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(concursoRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Debe actualizar un concurso correctamente")
    void testActualizarConcurso_Exito() {
        // ARRANGE
        when(concursoRepository.findById(10L)).thenReturn(Optional.of(concurso));
        when(localidadRepository.findById(1L)).thenReturn(Optional.of(localidad));
        when(concursoRepository.save(any(Concurso.class))).thenReturn(concurso);
        when(concursoMapper.toResponse(any(Concurso.class))).thenReturn(concursoResponse);

        // ACT
        ConcursoResponse result = concursoService.actualizarConcurso(10L, concursoRequest);

        // ASSERT
        assertNotNull(result);
        verify(concursoRepository).save(concurso);
    }

    @Test
    @DisplayName("Debe eliminar un concurso")
    void testEliminarConcurso_Exito() {
        // ARRANGE
        when(concursoRepository.findById(10L)).thenReturn(Optional.of(concurso));
        doNothing().when(concursoRepository).delete(concurso);

        // ACT
        concursoService.eliminarConcurso(10L);

        // ASSERT
        verify(concursoRepository, times(1)).delete(concurso);
    }

    @Test
    @DisplayName("Debe lanzar excepción al eliminar si el concurso no existe")
    void testEliminarConcurso_NotFound() {
        // ARRANGE
        when(concursoRepository.findById(99L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(ResourceNotFoundException.class, () -> concursoService.eliminarConcurso(99L));
        verify(concursoRepository, never()).delete(any());
    }
}