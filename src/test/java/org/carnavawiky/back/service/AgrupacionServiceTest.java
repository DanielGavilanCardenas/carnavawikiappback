package org.carnavawiky.back.service;

import org.carnavawiky.back.dto.AgrupacionRequest;
import org.carnavawiky.back.dto.AgrupacionResponse;
import org.carnavawiky.back.dto.PageResponse;
import org.carnavawiky.back.exception.ResourceNotFoundException;
import org.carnavawiky.back.mapper.AgrupacionMapper;
import org.carnavawiky.back.model.Agrupacion;
import org.carnavawiky.back.model.Localidad;
import org.carnavawiky.back.model.Modalidad;
import org.carnavawiky.back.model.Usuario;
import org.carnavawiky.back.repository.AgrupacionRepository;
import org.carnavawiky.back.repository.LocalidadRepository;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgrupacionServiceTest {

    @Mock
    private AgrupacionRepository agrupacionRepository;
    @Mock
    private LocalidadRepository localidadRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private AgrupacionMapper agrupacionMapper;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private AgrupacionService agrupacionService;

    private Agrupacion agrupacion;
    private Localidad localidad;
    private Usuario usuario;
    private AgrupacionRequest request;
    private AgrupacionResponse response;

    @BeforeEach
    void setUp() {
        localidad = new Localidad();
        localidad.setId(1L);
        localidad.setNombre("Cádiz");

        usuario = new Usuario();
        usuario.setUsername("dani");

        agrupacion = new Agrupacion();
        agrupacion.setId(10L);
        agrupacion.setNombre("Los Piratas");
        agrupacion.setLocalidad(localidad);

        request = new AgrupacionRequest();
        request.setNombre("Los Piratas");
        request.setLocalidadId(1L);
        request.setModalidad(Modalidad.COMPARSA);

        response = new AgrupacionResponse();
        response.setId(10L);
        response.setNombre("Los Piratas");
    }

    @Test
    @DisplayName("Debe crear una agrupación vinculándola al usuario autenticado")
    void testCrearAgrupacion_Exito() {
        // Mock del contexto de seguridad
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("dani");

        when(usuarioRepository.findByUsername("dani")).thenReturn(Optional.of(usuario));
        when(localidadRepository.findById(1L)).thenReturn(Optional.of(localidad));
        when(agrupacionMapper.toEntity(any(), any(), any())).thenReturn(agrupacion);
        when(agrupacionRepository.save(any())).thenReturn(agrupacion);
        when(agrupacionMapper.toResponse(any())).thenReturn(response);

        AgrupacionResponse result = agrupacionService.crearAgrupacion(request);

        assertNotNull(result);
        assertEquals("Los Piratas", result.getNombre());
        verify(agrupacionRepository).save(any());
    }


    @Test
    @DisplayName("Debe obtener todas las agrupaciones paginadas")
    void testObtenerTodas_Paginado() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Agrupacion> page = new PageImpl<>(Collections.singletonList(agrupacion));

        when(agrupacionRepository.findAll(pageable)).thenReturn(page);
        when(agrupacionMapper.toResponse(any())).thenReturn(response);

        PageResponse<AgrupacionResponse> result = agrupacionService.obtenerTodasAgrupaciones(pageable, null);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(agrupacionRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Debe actualizar una agrupación correctamente")
    void testActualizarAgrupacion_Exito() {
        when(agrupacionRepository.findById(10L)).thenReturn(Optional.of(agrupacion));
        when(localidadRepository.findById(1L)).thenReturn(Optional.of(localidad));
        when(agrupacionRepository.save(any())).thenReturn(agrupacion);
        when(agrupacionMapper.toResponse(any())).thenReturn(response);

        AgrupacionResponse result = agrupacionService.actualizarAgrupacion(10L, request);

        assertNotNull(result);
        verify(agrupacionRepository).save(agrupacion);
    }

    @Test
    @DisplayName("Debe eliminar una agrupación")
    void testEliminarAgrupacion_Exito() {
        when(agrupacionRepository.findById(10L)).thenReturn(Optional.of(agrupacion));
        doNothing().when(agrupacionRepository).delete(agrupacion);

        assertDoesNotThrow(() -> agrupacionService.eliminarAgrupacion(10L));
        verify(agrupacionRepository, times(1)).delete(agrupacion);
    }
}