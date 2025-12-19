package org.carnavawiky.back.mapper;

import org.carnavawiky.back.dto.AgrupacionRequest;
import org.carnavawiky.back.dto.AgrupacionResponse;
import org.carnavawiky.back.model.Agrupacion;
import org.carnavawiky.back.model.Localidad;
import org.carnavawiky.back.model.Modalidad;
import org.carnavawiky.back.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AgrupacionMapperTest {

    private AgrupacionMapper agrupacionMapper;
    private Usuario usuario;
    private Localidad localidad;

    @BeforeEach
    void setUp() {
        agrupacionMapper = new AgrupacionMapper();

        // Configuración de objetos de apoyo
        usuario = new Usuario();
        usuario.setId(10L);
        usuario.setUsername("carnavalero_pro");

        localidad = new Localidad();
        localidad.setId(50L);
        localidad.setNombre("Cádiz");
    }

    @Test
    @DisplayName("Debe mapear de Request a Entidad correctamente")
    void testToEntity() {
        AgrupacionRequest request = new AgrupacionRequest();
        request.setNombre("Los Yesterday");
        request.setDescripcion("Una chirigota mítica");
        request.setModalidad(Modalidad.CHIRIGOTA);
        request.setAnho(1999);
        request.setLocalidadId(50L);

        Agrupacion entity = agrupacionMapper.toEntity(request, usuario, localidad);

        assertNotNull(entity);
        assertEquals("Los Yesterday", entity.getNombre());
        assertEquals("Una chirigota mítica", entity.getDescripcion());
        assertEquals(Modalidad.CHIRIGOTA, entity.getModalidad());
        assertEquals(1999, entity.getAnho());
        assertEquals(usuario, entity.getUsuarioCreador());
        assertEquals(localidad, entity.getLocalidad());
    }

    @Test
    @DisplayName("Debe mapear de Entidad a Response con todas sus relaciones")
    void testToResponse_Completo() {
        LocalDateTime fecha = LocalDateTime.of(2024, 2, 15, 22, 30);
        
        Agrupacion agrupacion = new Agrupacion();
        agrupacion.setId(1L);
        agrupacion.setNombre("Los Piratas");
        agrupacion.setDescripcion("Comparsa ganadora");
        agrupacion.setModalidad(Modalidad.COMPARSA);
        agrupacion.setAnho(1998);
        agrupacion.setFechaAlta(fecha);
        agrupacion.setUsuarioCreador(usuario);
        agrupacion.setLocalidad(localidad);

        AgrupacionResponse response = agrupacionMapper.toResponse(agrupacion);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Los Piratas", response.getNombre());
        assertEquals("Comparsa ganadora", response.getDescripcion());
        assertEquals(Modalidad.COMPARSA, response.getModalidad());
        assertEquals(1998, response.getAnho());
        assertEquals(fecha, response.getFechaAlta());

        // Verificación de Usuario
        assertEquals("carnavalero_pro", response.getNombreUsuarioCreador());

        // Verificación de Localidad
        assertEquals(50L, response.getLocalidadId());
        assertEquals("Cádiz", response.getLocalidadNombre());
    }

    @Test
    @DisplayName("Debe manejar nulos en las relaciones al mapear a Response")
    void testToResponse_ConRelacionesNulas() {
        Agrupacion agrupacion = new Agrupacion();
        agrupacion.setId(2L);
        agrupacion.setNombre("Agrupación Incompleta");
        agrupacion.setModalidad(Modalidad.CORO);
        agrupacion.setAnho(2000);
        agrupacion.setUsuarioCreador(usuario); // Usuario es obligatorio en el modelo, pero localidad podría faltar en casos raros
        // Dejamos localidad como null

        AgrupacionResponse response = agrupacionMapper.toResponse(agrupacion);

        assertNotNull(response);
        assertEquals(2L, response.getId());
        assertEquals("Agrupación Incompleta", response.getNombre());
        assertEquals(Modalidad.CORO, response.getModalidad());
        assertEquals(2000, response.getAnho());
        
        assertEquals("carnavalero_pro", response.getNombreUsuarioCreador());
        
        assertNull(response.getLocalidadId());
        assertNull(response.getLocalidadNombre());
    }
}