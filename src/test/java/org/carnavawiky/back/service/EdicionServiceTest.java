package org.carnavawiky.back.service;

import org.carnavawiky.back.dto.EdicionRequest;
import org.carnavawiky.back.dto.EdicionResponse;
import org.carnavawiky.back.dto.PageResponse;
import org.carnavawiky.back.exception.ResourceNotFoundException;
import org.carnavawiky.back.mapper.EdicionMapper;
import org.carnavawiky.back.model.Concurso;
import org.carnavawiky.back.model.Edicion;
import org.carnavawiky.back.repository.ConcursoRepository;
import org.carnavawiky.back.repository.EdicionRepository;
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
class EdicionServiceTest {

    @Mock
    private EdicionRepository edicionRepository;

    @Mock
    private ConcursoRepository concursoRepository;

    @Mock
    private EdicionMapper edicionMapper;

    @InjectMocks
    private EdicionService edicionService;

    private Edicion edicion;
    private EdicionRequest edicionRequest;
    private EdicionResponse edicionResponse;
    private Concurso concurso;

    @BeforeEach
    void setUp() {
        concurso = new Concurso();
        concurso.setId(1L);
        concurso.setNombre("COAC");

        edicion = new Edicion();
        edicion.setId(10L);
        edicion.setAnho(2024);
        edicion.setConcurso(concurso);

        edicionRequest = new EdicionRequest();
        edicionRequest.setAnho(2024);
        edicionRequest.setConcursoId(1L);

        edicionResponse = new EdicionResponse();
        edicionResponse.setId(10L);
        edicionResponse.setAnho(2024);
    }

    @Test
    @DisplayName("Debe crear una edición correctamente")
    void testCrearEdicion_Exito() {
        // ARRANGE
        when(concursoRepository.findById(1L)).thenReturn(Optional.of(concurso));
        when(edicionMapper.toEntity(any(EdicionRequest.class), any(Concurso.class))).thenReturn(edicion);
        when(edicionRepository.save(any(Edicion.class))).thenReturn(edicion);
        when(edicionMapper.toResponse(any(Edicion.class))).thenReturn(edicionResponse);

        // ACT
        EdicionResponse result = edicionService.crearEdicion(edicionRequest);

        // ASSERT
        assertNotNull(result);
        assertEquals(2024, result.getAnho());
        verify(edicionRepository, times(1)).save(any(Edicion.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al crear edición si el concurso no existe")
    void testCrearEdicion_ConcursoNoEncontrado() {
        // ARRANGE
        when(concursoRepository.findById(1L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(ResourceNotFoundException.class, () -> edicionService.crearEdicion(edicionRequest));
        verify(edicionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe obtener una edición por ID")
    void testObtenerPorId_Exito() {
        // ARRANGE
        when(edicionRepository.findById(10L)).thenReturn(Optional.of(edicion));
        when(edicionMapper.toResponse(edicion)).thenReturn(edicionResponse);

        // ACT
        EdicionResponse result = edicionService.obtenerEdicionPorId(10L);

        // ASSERT
        assertNotNull(result);
        assertEquals(10L, result.getId());
    }

    @Test
    @DisplayName("Debe listar ediciones paginadas")
    void testListarEdiciones_Paginado() {
        // ARRANGE
        Pageable pageable = PageRequest.of(0, 10);
        Page<Edicion> page = new PageImpl<>(Collections.singletonList(edicion));

        when(edicionRepository.findAll(pageable)).thenReturn(page);
        when(edicionMapper.toResponse(any(Edicion.class))).thenReturn(edicionResponse);

        // ACT
        PageResponse<EdicionResponse> result = edicionService.obtenerTodasEdiciones(pageable, null);

        // ASSERT
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(edicionRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Debe actualizar una edición correctamente")
    void testActualizarEdicion_Exito() {
        // ARRANGE
        EdicionRequest updateRequest = new EdicionRequest();
        updateRequest.setAnho(2025);
        updateRequest.setConcursoId(1L);

        when(edicionRepository.findById(10L)).thenReturn(Optional.of(edicion));
        when(concursoRepository.findById(1L)).thenReturn(Optional.of(concurso));
        when(edicionRepository.save(any(Edicion.class))).thenReturn(edicion);
        when(edicionMapper.toResponse(any(Edicion.class))).thenReturn(edicionResponse);

        // ACT
        EdicionResponse result = edicionService.actualizarEdicion(10L, updateRequest);

        // ASSERT
        assertNotNull(result);
        verify(edicionRepository).save(edicion);
        assertEquals(2025, edicion.getAnho());
    }

    @Test
    @DisplayName("Debe eliminar una edición correctamente")
    void testEliminarEdicion_Exito() {
        // ARRANGE
        when(edicionRepository.findById(10L)).thenReturn(Optional.of(edicion));
        doNothing().when(edicionRepository).delete(edicion);

        // ACT
        edicionService.eliminarEdicion(10L);

        // ASSERT
        verify(edicionRepository, times(1)).delete(edicion);
    }

    @Test
    @DisplayName("Debe lanzar excepción al eliminar si la edición no existe")
    void testEliminarEdicion_NoEncontrada() {
        // ARRANGE
        when(edicionRepository.findById(99L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(ResourceNotFoundException.class, () -> edicionService.eliminarEdicion(99L));
        verify(edicionRepository, never()).delete(any());
    }
}