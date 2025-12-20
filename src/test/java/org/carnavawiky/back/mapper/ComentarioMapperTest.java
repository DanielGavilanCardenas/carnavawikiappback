package org.carnavawiky.back.mapper;

import org.carnavawiky.back.dto.ComentarioRequest;
import org.carnavawiky.back.dto.ComentarioResponse;
import org.carnavawiky.back.model.Agrupacion;
import org.carnavawiky.back.model.Comentario;
import org.carnavawiky.back.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ComentarioMapperTest {

    private ComentarioMapper comentarioMapper;
    private Usuario usuario;
    private Agrupacion agrupacion;

    @BeforeEach
    void setUp() {
        comentarioMapper = new ComentarioMapper();

        // Configuración de objetos de apoyo
        usuario = new Usuario();
        usuario.setId(10L);
        usuario.setUsername("carnavalero_pro");

        agrupacion = new Agrupacion();
        agrupacion.setId(50L);
        agrupacion.setNombre("Los Piratas");
    }

    @Test
    @DisplayName("Debe mapear de Request a Entidad correctamente")
    void testToEntity() {
        ComentarioRequest request = new ComentarioRequest();
        request.setContenido("¡Qué pedazo de comparsa!");
        request.setPuntuacion(10);
        request.setAgrupacionId(50L);

        Comentario entity = comentarioMapper.toEntity(request, usuario, agrupacion);

        assertNotNull(entity);
        assertEquals("¡Qué pedazo de comparsa!", entity.getContenido());
        assertEquals(10, entity.getPuntuacion());
        assertEquals(usuario, entity.getUsuario());
        assertEquals(agrupacion, entity.getAgrupacion());
    }

    @Test
    @DisplayName("Debe mapear de Entidad a Response con todas sus relaciones")
    void testToResponse_Completo() {
        LocalDateTime fecha = LocalDateTime.of(2024, 2, 15, 22, 30);
        
        Comentario comentario = new Comentario();
        comentario.setId(1L);
        comentario.setContenido("Muy buena actuación");
        comentario.setPuntuacion(9);
        comentario.setFechaCreacion(fecha);
        comentario.setUsuario(usuario);
        comentario.setAgrupacion(agrupacion);

        ComentarioResponse response = comentarioMapper.toResponse(comentario);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Muy buena actuación", response.getContenido());
        assertEquals(9, response.getPuntuacion());
        assertEquals(fecha, response.getFechaCreacion());

        // Verificación de Usuario
        assertEquals(10L, response.getUsuarioId());
        assertEquals("carnavalero_pro", response.getUsuarioUsername());

        // Verificación de Agrupación
        assertEquals(50L, response.getAgrupacionId());
        assertEquals("Los Piratas", response.getAgrupacionNombre());
    }

    @Test
    @DisplayName("Debe manejar nulos en las relaciones al mapear a Response")
    void testToResponse_ConRelacionesNulas() {
        Comentario comentario = new Comentario();
        comentario.setId(2L);
        comentario.setContenido("Comentario anónimo");
        comentario.setPuntuacion(5);
        // Dejamos usuario y agrupacion como null

        ComentarioResponse response = comentarioMapper.toResponse(comentario);

        assertNotNull(response);
        assertEquals(2L, response.getId());
        assertEquals("Comentario anónimo", response.getContenido());
        assertEquals(5, response.getPuntuacion());
        
        assertNull(response.getUsuarioId());
        assertNull(response.getUsuarioUsername());
        
        assertNull(response.getAgrupacionId());
        assertNull(response.getAgrupacionNombre());
    }
}