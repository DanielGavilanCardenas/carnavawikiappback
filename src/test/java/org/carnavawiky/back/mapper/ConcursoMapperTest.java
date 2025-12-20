package org.carnavawiky.back.mapper;

import org.carnavawiky.back.dto.ConcursoRequest;
import org.carnavawiky.back.dto.ConcursoResponse;
import org.carnavawiky.back.model.Concurso;
import org.carnavawiky.back.model.Localidad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConcursoMapperTest {

    private ConcursoMapper concursoMapper;
    private Localidad localidad;

    @BeforeEach
    void setUp() {
        concursoMapper = new ConcursoMapper();

        // Configuración de objetos de apoyo
        localidad = new Localidad();
        localidad.setId(50L);
        localidad.setNombre("Cádiz");
    }

    @Test
    @DisplayName("Debe mapear de Request a Entidad correctamente")
    void testToEntity() {
        ConcursoRequest request = new ConcursoRequest();
        request.setNombre("COAC");
        request.setEstaActivo(true);
        request.setLocalidadId(50L);

        Concurso entity = concursoMapper.toEntity(request, localidad);

        assertNotNull(entity);
        assertEquals("COAC", entity.getNombre());
        assertTrue(entity.getEstaActivo());
        assertEquals(localidad, entity.getLocalidad());
    }

    @Test
    @DisplayName("Debe mapear de Entidad a Response con todas sus relaciones")
    void testToResponse_Completo() {
        Concurso concurso = new Concurso();
        concurso.setId(1L);
        concurso.setNombre("COAC");
        concurso.setEstaActivo(true);
        concurso.setLocalidad(localidad);

        ConcursoResponse response = concursoMapper.toResponse(concurso);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("COAC", response.getNombre());
        assertTrue(response.getEstaActivo());

        // Verificación de Localidad
        assertEquals(50L, response.getLocalidadId());
        assertEquals("Cádiz", response.getLocalidadNombre());
    }

    @Test
    @DisplayName("Debe manejar nulos en las relaciones al mapear a Response")
    void testToResponse_ConRelacionesNulas() {
        Concurso concurso = new Concurso();
        concurso.setId(2L);
        concurso.setNombre("Concurso Histórico");
        concurso.setEstaActivo(false);
        // Dejamos localidad como null

        ConcursoResponse response = concursoMapper.toResponse(concurso);

        assertNotNull(response);
        assertEquals(2L, response.getId());
        assertEquals("Concurso Histórico", response.getNombre());
        assertFalse(response.getEstaActivo());
        
        assertNull(response.getLocalidadId());
        assertNull(response.getLocalidadNombre());
    }
}