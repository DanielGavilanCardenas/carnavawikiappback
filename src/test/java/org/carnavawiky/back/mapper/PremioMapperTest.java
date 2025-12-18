package org.carnavawiky.back.mapper;

import org.carnavawiky.back.dto.PremioRequest;
import org.carnavawiky.back.dto.PremioResponse;
import org.carnavawiky.back.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PremioMapperTest {

    private PremioMapper premioMapper;
    private Agrupacion agrupacion;
    private Edicion edicion;
    private Concurso concurso;

    @BeforeEach
    void setUp() {
        premioMapper = new PremioMapper();

        // Configuración de objetos de apoyo
        agrupacion = new Agrupacion();
        agrupacion.setId(10L);
        agrupacion.setNombre("Los Curanderos");

        concurso = new Concurso();
        concurso.setNombre("COAC");

        edicion = new Edicion();
        edicion.setId(20L);
        edicion.setAnho(2001);
        edicion.setConcurso(concurso);
    }

    @Test
    @DisplayName("Debe mapear de Request a Entidad correctamente")
    void testToEntity() {
        PremioRequest request = new PremioRequest();
        request.setPuesto(2);
        request.setModalidad(Modalidad.COMPARSA);

        Premio entity = premioMapper.toEntity(request, agrupacion, edicion);

        assertNotNull(entity);
        assertEquals(2, entity.getPuesto());
        assertEquals(Modalidad.COMPARSA, entity.getModalidad());
        assertEquals(agrupacion, entity.getAgrupacion());
        assertEquals(edicion, entity.getEdicion());
    }

    @Test
    @DisplayName("Debe mapear de Entidad a Response con todas sus relaciones")
    void testToResponse_Completo() {
        Premio premio = new Premio();
        premio.setId(1L);
        premio.setPuesto(1);
        premio.setModalidad(Modalidad.CHIRIGOTA);
        premio.setAgrupacion(agrupacion);
        premio.setEdicion(edicion);

        PremioResponse response = premioMapper.toResponse(premio);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(1, response.getPuesto());
        assertEquals(Modalidad.CHIRIGOTA, response.getModalidad());

        // Verificación de Agrupación
        assertEquals(10L, response.getAgrupacionId());
        assertEquals("Los Curanderos", response.getAgrupacionNombre());

        // Verificación de Edición y Concurso
        assertEquals(20L, response.getEdicionId());
        assertEquals(2001, response.getEdicionAnho());
        assertEquals("COAC", response.getConcursoNombre());
    }

    @Test
    @DisplayName("Debe manejar nulos en las relaciones al mapear a Response")
    void testToResponse_ConRelacionesNulas() {
        Premio premio = new Premio();
        premio.setId(5L);
        premio.setPuesto(3);
        // Dejamos agrupacion y edicion como null

        PremioResponse response = premioMapper.toResponse(premio);

        assertNotNull(response);
        assertEquals(5L, response.getId());
        assertNull(response.getAgrupacionNombre());
        assertNull(response.getEdicionAnho());
        assertNull(response.getConcursoNombre());
    }
}