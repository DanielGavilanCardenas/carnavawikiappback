package org.carnavawiky.back.service;

import org.carnavawiky.back.dto.PersonaRequest;
import org.carnavawiky.back.dto.PersonaResponse;
import org.carnavawiky.back.exception.ResourceNotFoundException;
import org.carnavawiky.back.mapper.PersonaMapper;
import org.carnavawiky.back.model.Localidad;
import org.carnavawiky.back.model.Persona;
import org.carnavawiky.back.model.Usuario;
import org.carnavawiky.back.repository.LocalidadRepository;
import org.carnavawiky.back.repository.PersonaRepository;
import org.carnavawiky.back.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonaServiceTest {

    @InjectMocks
    private PersonaService personaService;

    @Mock
    private PersonaRepository personaRepository;

    @Mock
    private LocalidadRepository localidadRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PersonaMapper personaMapper;

    private Persona persona;
    private Localidad localidad;
    private Usuario usuario;
    private PersonaRequest personaRequest;
    private PersonaResponse personaResponse;

    private final Long PERSONA_ID = 1L;
    private final Long LOCALIDAD_ID = 10L;
    private final Long USUARIO_ID = 5L;

    @BeforeEach
    void setUp() {
        // Configuración de Localidad (Obligatoria para Persona)
        localidad = new Localidad();
        localidad.setId(LOCALIDAD_ID);
        localidad.setNombre("Cádiz");

        // Configuración de Usuario (Opcional)
        usuario = new Usuario();
        usuario.setId(USUARIO_ID);
        usuario.setUsername("carnavalero_test");

        // Entidad Persona completa
        persona = new Persona();
        persona.setId(PERSONA_ID);
        persona.setNombreReal("Juan Pérez");
        persona.setApodo("El Juani");
        persona.setOrigen(localidad);
        persona.setUsuario(usuario);

        // Request para pruebas de creación/actualización
        personaRequest = new PersonaRequest();
        personaRequest.setNombreReal("Juan Pérez");
        personaRequest.setApodo("El Juani");
        personaRequest.setLocalidadId(LOCALIDAD_ID);
        personaRequest.setUsuarioId(USUARIO_ID);

        // Response esperada
        personaResponse = new PersonaResponse();
        personaResponse.setId(PERSONA_ID);
        personaResponse.setNombreReal("Juan Pérez");
    }

    @Test
    @DisplayName("Debe crear una persona correctamente buscando localidad y usuario")
    void testCrearPersona_Exito() {
        // ARRANGE
        when(localidadRepository.findById(LOCALIDAD_ID)).thenReturn(Optional.of(localidad));
        when(usuarioRepository.findById(USUARIO_ID)).thenReturn(Optional.of(usuario));

        // PersonaMapper requiere 3 argumentos: request, localidad, usuario
        when(personaMapper.toEntity(personaRequest, localidad, usuario)).thenReturn(persona);
        when(personaRepository.save(any(Persona.class))).thenReturn(persona);
        when(personaMapper.toResponse(persona)).thenReturn(personaResponse);

        // ACT
        PersonaResponse result = personaService.crearPersona(personaRequest);

        // ASSERT
        assertNotNull(result);
        assertEquals(PERSONA_ID, result.getId());
        verify(personaRepository).save(any(Persona.class));
        verify(localidadRepository).findById(LOCALIDAD_ID);
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException si la localidad no existe al crear")
    void testCrearPersona_LocalidadNoEncontrada() {
        // ARRANGE
        when(localidadRepository.findById(LOCALIDAD_ID)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(ResourceNotFoundException.class, () -> personaService.crearPersona(personaRequest));

        verify(personaRepository, never()).save(any());
        verify(personaMapper, never()).toEntity(any(), any(), any());
    }

    @Test
    @DisplayName("Debe obtener persona por ID correctamente")
    void testObtenerPersonaPorId_Exito() {
        // ARRANGE
        when(personaRepository.findById(PERSONA_ID)).thenReturn(Optional.of(persona));
        when(personaMapper.toResponse(persona)).thenReturn(personaResponse);

        // ACT
        PersonaResponse result = personaService.obtenerPersonaPorId(PERSONA_ID);

        // ASSERT
        assertNotNull(result);
        verify(personaRepository).findById(PERSONA_ID);
    }

    @Test
    @DisplayName("Debe eliminar persona si existe (usando findById)")
    void testEliminarPersona_Exito() {
        // ARRANGE
        // Corregido: Usamos findById porque el código real lo usa para validar existencia
        when(personaRepository.findById(PERSONA_ID)).thenReturn(Optional.of(persona));

        // ACT & ASSERT
        assertDoesNotThrow(() -> personaService.eliminarPersona(PERSONA_ID));

        // Verificamos que se llame a delete con la entidad encontrada
        verify(personaRepository, times(1)).delete(persona);
    }

    @Test
    @DisplayName("Debe lanzar excepción al intentar eliminar persona que no existe")
    void testEliminarPersona_NoExiste() {
        // ARRANGE
        // Eliminamos el stub de existsById para evitar UnnecessaryStubbingException
        when(personaRepository.findById(PERSONA_ID)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(ResourceNotFoundException.class, () -> personaService.eliminarPersona(PERSONA_ID));

        verify(personaRepository, never()).delete(any());
    }
}