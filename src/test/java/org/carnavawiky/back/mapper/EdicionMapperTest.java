package org.carnavawiky.back.mapper;

import org.carnavawiky.back.dto.EdicionRequest;
import org.carnavawiky.back.dto.EdicionResponse;
import org.carnavawiky.back.model.Concurso;
import org.carnavawiky.back.model.Edicion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EdicionMapperTest {

    private EdicionMapper edicionMapper;
    private Concurso concurso;

    @BeforeEach
    void setUp() {
        edicionMapper = new EdicionMapper();

        // Configuración de objetos de apoyo
        concurso = new Concurso();
        concurso.setId(10L);
        concurso.setNombre("COAC");
    }

    @Test
    @DisplayName("Debe mapear de Request a Entidad correctamente")
    void testToEntity() {
        EdicionRequest request = new EdicionRequest();
        request.setAnho(2024);
        request.setConcursoId(10L); // Aunque el mapper usa el objeto Concurso pasado aparte

        Edicion entity = edicionMapper.toEntity(request, concurso);

        assertNotNull(entity);
        assertEquals(2024, entity.getAnho());
        assertEquals(concurso, entity.getConcurso());
    }

    @Test
    @DisplayName("Debe mapear de Entidad a Response con todas sus relaciones")
    void testToResponse_Completo() {
        Edicion edicion = new Edicion();
        edicion.setId(1L);
        edicion.setAnho(2024);
        edicion.setConcurso(concurso);

        EdicionResponse response = edicionMapper.toResponse(edicion);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(2024, response.getAnho());

        // Verificación de Concurso
        assertEquals(10L, response.getConcursoId());
        assertEquals("COAC", response.getConcursoNombre());
    }

    @Test
    @DisplayName("Debe manejar nulos en las relaciones al mapear a Response")
    void testToResponse_ConRelacionesNulas() {
        Edicion edicion = new Edicion();
        edicion.setId(5L);
        edicion.setAnho(1990);
        // Dejamos concurso como null

        EdicionResponse response = edicionMapper.toResponse(edicion);

        assertNotNull(response);
        assertEquals(5L, response.getId());
        assertEquals(1990, response.getAnho());
        
        assertNull(response.getConcursoId());
        assertNull(response.getConcursoNombre());
    }
}