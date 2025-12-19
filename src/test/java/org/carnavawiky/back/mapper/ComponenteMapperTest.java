package org.carnavawiky.back.mapper;

import org.carnavawiky.back.dto.ComponenteRequest;
import org.carnavawiky.back.dto.ComponenteResponse;
import org.carnavawiky.back.model.Agrupacion;
import org.carnavawiky.back.model.Componente;
import org.carnavawiky.back.model.Persona;
import org.carnavawiky.back.model.RolComponente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComponenteMapperTest {

    private ComponenteMapper componenteMapper;
    private Persona persona;
    private Agrupacion agrupacion;

    @BeforeEach
    void setUp() {
        componenteMapper = new ComponenteMapper();

        // Configuración de objetos de apoyo
        persona = new Persona();
        persona.setId(100L);
        persona.setNombreReal("Juan Carlos Aragón");
        persona.setApodo("El Capitán Veneno");

        agrupacion = new Agrupacion();
        agrupacion.setId(50L);
        agrupacion.setNombre("Los Yesterday");
    }

    @Test
    @DisplayName("Debe mapear de Request a Entidad correctamente")
    void testToEntity() {
        ComponenteRequest request = new ComponenteRequest();
        request.setRol(RolComponente.AUTOR_LETRA);
        request.setPersonaId(100L);
        request.setAgrupacionId(50L);

        Componente entity = componenteMapper.toEntity(request, persona, agrupacion);

        assertNotNull(entity);
        assertEquals(RolComponente.AUTOR_LETRA, entity.getRol());
        assertEquals(persona, entity.getPersona());
        assertEquals(agrupacion, entity.getAgrupacion());
    }

    @Test
    @DisplayName("Debe mapear de Entidad a Response con todas sus relaciones")
    void testToResponse_Completo() {
        Componente componente = new Componente();
        componente.setId(1L);
        componente.setRol(RolComponente.DIRECTOR);
        componente.setPersona(persona);
        componente.setAgrupacion(agrupacion);

        ComponenteResponse response = componenteMapper.toResponse(componente);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(RolComponente.DIRECTOR, response.getRol());

        // Verificación de Persona
        assertEquals(100L, response.getPersonaId());
        assertEquals("Juan Carlos Aragón", response.getNombreReal());
        assertEquals("El Capitán Veneno", response.getNombreArtistico());

        // Verificación de Agrupación
        assertEquals(50L, response.getAgrupacionId());
        assertEquals("Los Yesterday", response.getAgrupacionNombre());
    }

    @Test
    @DisplayName("Debe manejar nulos en las relaciones al mapear a Response")
    void testToResponse_ConRelacionesNulas() {
        Componente componente = new Componente();
        componente.setId(2L);
        componente.setRol(RolComponente.GUITARRA);
        // Dejamos persona y agrupacion como null

        ComponenteResponse response = componenteMapper.toResponse(componente);

        assertNotNull(response);
        assertEquals(2L, response.getId());
        assertEquals(RolComponente.GUITARRA, response.getRol());
        
        assertNull(response.getPersonaId());
        assertNull(response.getNombreReal());
        assertNull(response.getNombreArtistico());
        
        assertNull(response.getAgrupacionId());
        assertNull(response.getAgrupacionNombre());
    }
}