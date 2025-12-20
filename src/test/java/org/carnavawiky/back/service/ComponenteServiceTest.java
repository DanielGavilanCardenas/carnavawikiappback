package org.carnavawiky.back.service;

import org.carnavawiky.back.dto.ComponenteRequest;
import org.carnavawiky.back.dto.ComponenteResponse;
import org.carnavawiky.back.dto.PageResponse;
import org.carnavawiky.back.exception.ResourceNotFoundException;
import org.carnavawiky.back.mapper.ComponenteMapper;
import org.carnavawiky.back.model.Agrupacion;
import org.carnavawiky.back.model.Componente;
import org.carnavawiky.back.model.Persona;
import org.carnavawiky.back.model.RolComponente;
import org.carnavawiky.back.repository.AgrupacionRepository;
import org.carnavawiky.back.repository.ComponenteRepository;
import org.carnavawiky.back.repository.PersonaRepository;
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
class ComponenteServiceTest {

    @Mock
    private ComponenteRepository componenteRepository;

    @Mock
    private AgrupacionRepository agrupacionRepository;

    @Mock
    private PersonaRepository personaRepository;

    @Mock
    private ComponenteMapper componenteMapper;

    @InjectMocks
    private ComponenteService componenteService;

    private Componente componente;
    private Persona persona;
    private Agrupacion agrupacion;
    private ComponenteRequest componenteRequest;
    private ComponenteResponse componenteResponse;

    @BeforeEach
    void setUp() {
        persona = new Persona();
        persona.setId(1L);
        persona.setNombreReal("Juan");

        agrupacion = new Agrupacion();
        agrupacion.setId(10L);
        agrupacion.setNombre("Los Piratas");

        componente = new Componente();
        componente.setId(100L);
        componente.setRol(RolComponente.AUTOR_LETRA);
        componente.setPersona(persona);
        componente.setAgrupacion(agrupacion);

        componenteRequest = new ComponenteRequest();
        componenteRequest.setRol(RolComponente.AUTOR_LETRA);
        componenteRequest.setPersonaId(1L);
        componenteRequest.setAgrupacionId(10L);

        componenteResponse = new ComponenteResponse();
        componenteResponse.setId(100L);
        componenteResponse.setRol(RolComponente.AUTOR_LETRA);
    }

    @Test
    @DisplayName("Debe crear un componente correctamente")
    void testCrearComponente_Exito() {
        // ARRANGE
        when(personaRepository.findById(1L)).thenReturn(Optional.of(persona));
        when(agrupacionRepository.findById(10L)).thenReturn(Optional.of(agrupacion));
        when(componenteMapper.toEntity(any(), any(), any())).thenReturn(componente);
        when(componenteRepository.save(any(Componente.class))).thenReturn(componente);
        when(componenteMapper.toResponse(any(Componente.class))).thenReturn(componenteResponse);

        // ACT
        ComponenteResponse result = componenteService.crearComponente(componenteRequest);

        // ASSERT
        assertNotNull(result);
        assertEquals(RolComponente.AUTOR_LETRA, result.getRol());
        verify(componenteRepository).save(any(Componente.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción si la persona no existe al crear")
    void testCrearComponente_PersonaNoEncontrada() {
        // ARRANGE
        when(personaRepository.findById(1L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(ResourceNotFoundException.class, () -> componenteService.crearComponente(componenteRequest));
        verify(componenteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe listar componentes paginados")
    void testListarComponentes_Paginado() {
        // ARRANGE
        Pageable pageable = PageRequest.of(0, 10);
        Page<Componente> page = new PageImpl<>(Collections.singletonList(componente));

        when(componenteRepository.findAll(pageable)).thenReturn(page);
        when(componenteMapper.toResponse(any())).thenReturn(componenteResponse);

        // ACT
        PageResponse<ComponenteResponse> result = componenteService.obtenerTodosComponentes(pageable, null);

        // ASSERT
        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
        verify(componenteRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Debe actualizar un componente correctamente")
    void testActualizarComponente_Exito() {
        // ARRANGE
        when(componenteRepository.findById(100L)).thenReturn(Optional.of(componente));
        when(personaRepository.findById(1L)).thenReturn(Optional.of(persona));
        when(agrupacionRepository.findById(10L)).thenReturn(Optional.of(agrupacion));
        when(componenteRepository.save(any())).thenReturn(componente);
        when(componenteMapper.toResponse(any())).thenReturn(componenteResponse);

        // ACT
        ComponenteResponse result = componenteService.actualizarComponente(100L, componenteRequest);

        // ASSERT
        assertNotNull(result);
        verify(componenteRepository).save(componente);
    }

    @Test
    @DisplayName("Debe eliminar un componente correctamente")
    void testEliminarComponente_Exito() {
        // ARRANGE
        when(componenteRepository.findById(100L)).thenReturn(Optional.of(componente));
        doNothing().when(componenteRepository).delete(componente);

        // ACT
        componenteService.eliminarComponente(100L);

        // ASSERT
        verify(componenteRepository, times(1)).delete(componente);
    }

    @Test
    @DisplayName("Debe lanzar excepción al eliminar si el componente no existe")
    void testEliminarComponente_NotFound() {
        // ARRANGE
        when(componenteRepository.findById(999L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(ResourceNotFoundException.class, () -> componenteService.eliminarComponente(999L));
        verify(componenteRepository, never()).delete(any());
    }
}