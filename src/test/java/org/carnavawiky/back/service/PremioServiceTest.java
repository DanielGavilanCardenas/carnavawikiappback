package org.carnavawiky.back.service;

import org.carnavawiky.back.dto.PageResponse;
import org.carnavawiky.back.dto.PremioRequest;
import org.carnavawiky.back.dto.PremioResponse;
import org.carnavawiky.back.exception.ResourceNotFoundException;
import org.carnavawiky.back.mapper.PremioMapper;
import org.carnavawiky.back.model.*;
import org.carnavawiky.back.repository.AgrupacionRepository;
import org.carnavawiky.back.repository.EdicionRepository;
import org.carnavawiky.back.repository.PremioRepository;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PremioServiceTest {

    @Mock
    private PremioRepository premioRepository;

    @Mock
    private AgrupacionRepository agrupacionRepository;

    @Mock
    private EdicionRepository edicionRepository;

    @Mock
    private PremioMapper premioMapper;

    @InjectMocks
    private PremioService premioService;

    private Premio premio;
    private PremioRequest premioRequest;
    private PremioResponse premioResponse;
    private Agrupacion agrupacion;
    private Edicion edicion;

    @BeforeEach
    void setUp() {
        agrupacion = new Agrupacion();
        agrupacion.setId(10L);
        agrupacion.setNombre("Los Yesterday");

        edicion = new Edicion();
        edicion.setId(20L);
        edicion.setAnho(1999);

        premio = new Premio();
        premio.setId(1L);
        premio.setPuesto(1);
        premio.setModalidad(Modalidad.CHIRIGOTA);
        premio.setAgrupacion(agrupacion);
        premio.setEdicion(edicion);

        premioRequest = new PremioRequest();
        premioRequest.setPuesto(1);
        premioRequest.setModalidad(Modalidad.CHIRIGOTA);
        premioRequest.setAgrupacionId(10L);
        premioRequest.setEdicionId(20L);

        premioResponse = new PremioResponse();
        premioResponse.setId(1L);
        premioResponse.setPuesto(1);
        premioResponse.setAgrupacionNombre("Los Yesterday");
    }

    // =======================================================
    // 1. CREAR PREMIO
    // =======================================================
    @Test
    @DisplayName("Debe crear un premio correctamente")
    void testCrearPremio_Exito() {
        when(agrupacionRepository.findById(10L)).thenReturn(Optional.of(agrupacion));
        when(edicionRepository.findById(20L)).thenReturn(Optional.of(edicion));
        when(premioMapper.toEntity(any(), any(), any())).thenReturn(premio);
        when(premioRepository.save(any())).thenReturn(premio);
        when(premioMapper.toResponse(any())).thenReturn(premioResponse);

        PremioResponse result = premioService.crearPremio(premioRequest);

        assertNotNull(result);
        assertEquals("Los Yesterday", result.getAgrupacionNombre());
        verify(premioRepository).save(any());
    }

    // =======================================================
    // 2. OBTENER POR ID
    // =======================================================
    @Test
    @DisplayName("Debe lanzar excepción si el premio no existe")
    void testObtenerPorId_NotFound() {
        when(premioRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> premioService.obtenerPremioPorId(1L));
    }

    // =======================================================
    // 3. OBTENER TODOS - LÓGICA DE BÚSQUEDA DUAL
    // =======================================================

    @Test
    @DisplayName("Debe listar todos los premios sin búsqueda (findAll)")
    void testObtenerTodos_SinBusqueda() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Premio> page = new PageImpl<>(List.of(premio));

        when(premioRepository.findAll(pageable)).thenReturn(page);
        when(premioMapper.toResponse(any())).thenReturn(premioResponse);

        PageResponse<PremioResponse> result = premioService.obtenerTodosPremios(pageable, null);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(premioRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Debe buscar por año cuando el parámetro search es un número")
    void testObtenerTodos_BusquedaPorAnho() {
        Pageable pageable = PageRequest.of(0, 10);
        String search = "1999";
        Page<Premio> page = new PageImpl<>(List.of(premio));

        when(premioRepository.findByEdicion_Anho(1999, pageable)).thenReturn(page);
        when(premioMapper.toResponse(any())).thenReturn(premioResponse);

        PageResponse<PremioResponse> result = premioService.obtenerTodosPremios(pageable, search);

        assertNotNull(result);
        verify(premioRepository).findByEdicion_Anho(1999, pageable);
        verify(premioRepository, never()).findByAgrupacion_NombreContainingIgnoreCase(any(), any());
    }

    @Test
    @DisplayName("Debe buscar por nombre de agrupación cuando el parámetro search es texto")
    void testObtenerTodos_BusquedaPorNombreAgrupacion() {
        Pageable pageable = PageRequest.of(0, 10);
        String search = "Yesterday";
        Page<Premio> page = new PageImpl<>(List.of(premio));

        when(premioRepository.findByAgrupacion_NombreContainingIgnoreCase(search, pageable)).thenReturn(page);
        when(premioMapper.toResponse(any())).thenReturn(premioResponse);

        PageResponse<PremioResponse> result = premioService.obtenerTodosPremios(pageable, search);

        assertNotNull(result);
        verify(premioRepository).findByAgrupacion_NombreContainingIgnoreCase(search, pageable);
        // Verificamos que al fallar el Integer.parseInt, entra en el catch y no llama al repo por año
        verify(premioRepository, never()).findByEdicion_Anho(anyInt(), any());
    }

    // =======================================================
    // 4. ACTUALIZAR
    // =======================================================
    @Test
    @DisplayName("Debe actualizar un premio correctamente")
    void testActualizarPremio_Exito() {
        when(premioRepository.findById(1L)).thenReturn(Optional.of(premio));
        when(agrupacionRepository.findById(10L)).thenReturn(Optional.of(agrupacion));
        when(edicionRepository.findById(20L)).thenReturn(Optional.of(edicion));
        when(premioRepository.save(any())).thenReturn(premio);
        when(premioMapper.toResponse(any())).thenReturn(premioResponse);

        PremioResponse result = premioService.actualizarPremio(1L, premioRequest);

        assertNotNull(result);
        verify(premioRepository).save(any());
    }

    // =======================================================
    // 5. ELIMINAR
    // =======================================================
    @Test
    @DisplayName("Debe eliminar un premio si existe")
    void testEliminarPremio_Exito() {
        when(premioRepository.findById(1L)).thenReturn(Optional.of(premio));
        doNothing().when(premioRepository).delete(premio);

        assertDoesNotThrow(() -> premioService.eliminarPremio(1L));
        verify(premioRepository).delete(premio);
    }
}