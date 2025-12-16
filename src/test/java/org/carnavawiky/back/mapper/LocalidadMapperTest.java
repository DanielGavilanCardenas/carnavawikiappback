package org.carnavawiky.back.mapper;

import org.carnavawiky.back.dto.LocalidadRequest;
import org.carnavawiky.back.dto.LocalidadResponse;
import org.carnavawiky.back.model.Localidad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas Unitarias para LocalidadMapper.
 * No se requieren mocks ya que el Mapper es un POJO de conversión simple (función pura).
 */
public class LocalidadMapperTest {

    // Instancia real de la clase a probar
    private LocalidadMapper localidadMapper;

    // Datos de Prueba (fixtures)
    private final Long LOCALIDAD_ID = 10L;
    private final String NOMBRE = "Chipiona";

    @BeforeEach
    void setUp() {
        // Inicializamos la clase sin usar inyección de Spring (es un POJO @Component)
        localidadMapper = new LocalidadMapper();
    }

    // =======================================================
    // 1. PRUEBAS PARA toEntity(LocalidadRequest request)
    // =======================================================

    @Test
    void testToEntity_DebeConvertirRequestAEntity() {
        // ARRANGE
        LocalidadRequest request = new LocalidadRequest();
        request.setNombre(NOMBRE);

        // ACT
        Localidad entity = localidadMapper.toEntity(request);

        // ASSERT
        assertNotNull(entity, "La entidad resultante no debe ser nula.");
        assertEquals(NOMBRE, entity.getNombre(), "El nombre debe transferirse correctamente.");
        assertNull(entity.getId(), "El ID debe ser nulo antes de ser guardado en BBDD.");
    }

    @Test
    void testToEntity_RequestNulo_DebeDevolverNulo() {
        // ACT
        Localidad entity = localidadMapper.toEntity(null);

        // ASSERT
        // Comportamiento esperado después de la corrección: si la entrada es nula, el Mapper devuelve null.
        assertNull(entity, "Debe devolver null si el request de entrada es null.");
    }

    // =======================================================
    // 2. PRUEBAS PARA toResponse(Localidad entity)
    // =======================================================

    @Test
    void testToResponse_DebeConvertirEntityAResponse() {
        // ARRANGE
        Localidad entity = new Localidad();
        entity.setId(LOCALIDAD_ID);
        entity.setNombre(NOMBRE);

        // ACT
        LocalidadResponse response = localidadMapper.toResponse(entity);

        // ASSERT
        assertNotNull(response, "La respuesta resultante no debe ser nula.");
        assertEquals(LOCALIDAD_ID, response.getId(), "El ID debe transferirse correctamente.");
        assertEquals(NOMBRE, response.getNombre(), "El nombre debe transferirse correctamente.");
    }

    @Test
    void testToResponse_EntityNula_DebeDevolverNulo() {
        // ACT
        LocalidadResponse response = localidadMapper.toResponse(null);

        // ASSERT
        // Comportamiento esperado después de la corrección: si la entrada es nula, el Mapper devuelve null.
        assertNull(response, "Debe devolver null si la entidad de entrada es null.");
    }
}