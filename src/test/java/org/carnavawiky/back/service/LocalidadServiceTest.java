package org.carnavawiky.back.service;

import org.carnavawiky.back.dto.LocalidadRequest;
import org.carnavawiky.back.dto.LocalidadResponse;
import org.carnavawiky.back.exception.ResourceNotFoundException; // Asumiendo este paquete
import org.carnavawiky.back.mapper.LocalidadMapper;
import org.carnavawiky.back.model.Localidad; // Asumiendo este paquete
import org.carnavawiky.back.repository.LocalidadRepository; // Asumiendo este paquete

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas Unitarias para la capa Service de Localidad.
 * Usamos Mockito para simular (mockear) las dependencias (Repository y Mapper).
 */
@ExtendWith(MockitoExtension.class)
class LocalidadServiceTest {

    // Instancia real de la clase a probar, donde se inyectarán los Mocks
    @InjectMocks
    private LocalidadService localidadService;

    // Dependencia simulada (Repository): Controlamos la interacción con la BBDD
    @Mock
    private LocalidadRepository localidadRepository;

    // Dependencia simulada (Mapper): Controlamos las conversiones de DTO a Entity
    @Mock
    private LocalidadMapper localidadMapper;

    // =======================================================
    // Datos de Prueba (fixtures)
    // =======================================================
    private Localidad localidad;
    private LocalidadRequest localidadRequest;
    private LocalidadResponse localidadResponse;
    private final Long LOCALIDAD_ID = 1L;
    private final String NOMBRE = "Cádiz";
    private final String NOMBRE_ACTUALIZADO = "Sevilla";

    @BeforeEach
    void setUp() {
        // Inicialización de la Entidad Localidad para el test
        localidad = new Localidad();
        // Asumiendo que la entidad tiene getters y setters (Lombok @Data)
        localidad.setId(LOCALIDAD_ID);
        localidad.setNombre(NOMBRE);

        // Inicialización del Request DTO
        localidadRequest = new LocalidadRequest();
        localidadRequest.setNombre(NOMBRE);

        // Inicialización del Response DTO
        localidadResponse = new LocalidadResponse();
        localidadResponse.setId(LOCALIDAD_ID);
        localidadResponse.setNombre(NOMBRE);
    }

    // =======================================================
    // 1. PRUEBAS PARA crearLocalidad(LocalidadRequest request)
    // Cobertura: Flujo completo de creación exitosa.
    // =======================================================
    @Test
    void testCrearLocalidad_DebeGuardarYDevolverResponse() {
        // ARRANGE
        // 1. Cuando se llama al mapper para convertir el Request, devuelve la Entidad.
        when(localidadMapper.toEntity(localidadRequest)).thenReturn(localidad);

        // 2. Cuando se llama al repositorio para guardar, devuelve la Entidad (simulando el ID asignado).
        when(localidadRepository.save(localidad)).thenReturn(localidad);

        // 3. Cuando se llama al mapper para convertir la Entidad, devuelve el Response.
        when(localidadMapper.toResponse(localidad)).thenReturn(localidadResponse);

        // ACT
        LocalidadResponse resultado = localidadService.crearLocalidad(localidadRequest);

        // ASSERT
        assertNotNull(resultado, "El resultado no debe ser nulo.");
        assertEquals(LOCALIDAD_ID, resultado.getId(), "El ID de la respuesta debe ser el de la entidad guardada.");

        // VERIFICACIÓN DE INTERACCIONES (La orquestación es correcta)
        verify(localidadMapper, times(1)).toEntity(localidadRequest); // Conversión de entrada
        verify(localidadRepository, times(1)).save(localidad);         // Guardado en BBDD
        verify(localidadMapper, times(1)).toResponse(localidad);       // Conversión de salida
    }

    // =======================================================
    // 2. PRUEBAS PARA obtenerLocalidadPorId(Long id)
    // Cobertura: Caso de éxito y manejo de excepción (no encontrado).
    // =======================================================
    @Test
    void testObtenerPorId_Existente_DebeDevolverResponse() {
        // ARRANGE
        // 1. Simular que el repositorio encuentra la Localidad
        when(localidadRepository.findById(LOCALIDAD_ID)).thenReturn(Optional.of(localidad));
        // 2. Simular la conversión
        when(localidadMapper.toResponse(localidad)).thenReturn(localidadResponse);

        // ACT
        LocalidadResponse resultado = localidadService.obtenerLocalidadPorId(LOCALIDAD_ID);

        // ASSERT
        assertNotNull(resultado, "El resultado no debe ser nulo.");
        assertEquals(NOMBRE, resultado.getNombre(), "El nombre debe coincidir.");

        verify(localidadRepository, times(1)).findById(LOCALIDAD_ID);
    }

    @Test
    void testObtenerPorId_NoExistente_DebeLanzarExcepcion() {
        // ARRANGE
        // 1. Simular que el repositorio NO encuentra la Localidad
        when(localidadRepository.findById(LOCALIDAD_ID)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(ResourceNotFoundException.class, () -> {
            localidadService.obtenerLocalidadPorId(LOCALIDAD_ID);
        }, "Debe lanzar ResourceNotFoundException si no se encuentra el recurso.");

        // VERIFICACIÓN: El mapeador NUNCA debe ser llamado en caso de error
        verify(localidadMapper, never()).toResponse(any());
    }

    // =======================================================
    // 3. PRUEBAS PARA actualizarLocalidad(Long id, LocalidadRequest request)
    // Cobertura: Éxito y manejo de excepción. Verificación de la actualización del campo.
    // =======================================================
    @Test
    void testActualizarLocalidad_Existente_DebeActualizarNombre() {
        // ARRANGE
        LocalidadRequest requestActualizacion = new LocalidadRequest();
        requestActualizacion.setNombre(NOMBRE_ACTUALIZADO);

        // Entidad simulada después de ser guardada con el nuevo nombre
        Localidad localidadActualizada = new Localidad();
        localidadActualizada.setId(LOCALIDAD_ID);
        localidadActualizada.setNombre(NOMBRE_ACTUALIZADO);

        // Response simulada
        LocalidadResponse responseActualizada = new LocalidadResponse();
        responseActualizada.setId(LOCALIDAD_ID);
        responseActualizada.setNombre(NOMBRE_ACTUALIZADO);

        // 1. Simular la búsqueda: devuelve la entidad original
        when(localidadRepository.findById(LOCALIDAD_ID)).thenReturn(Optional.of(localidad));

        // 2. Simular el guardado: devuelve la entidad actualizada
        when(localidadRepository.save(any(Localidad.class))).thenReturn(localidadActualizada);

        // 3. Simular la conversión a respuesta
        when(localidadMapper.toResponse(localidadActualizada)).thenReturn(responseActualizada);

        // ACT
        LocalidadResponse resultado = localidadService.actualizarLocalidad(LOCALIDAD_ID, requestActualizacion);

        // ASSERT
        assertNotNull(resultado, "El resultado no debe ser nulo.");
        assertEquals(NOMBRE_ACTUALIZADO, resultado.getNombre(), "El nombre debe estar actualizado.");

        // VERIFICACIÓN: Usamos argThat para asegurar que el objeto Localidad
        // que se pasó a save() tenía el nombre modificado (la lógica del Service)
        verify(localidadRepository, times(1)).save(argThat(loc ->
                loc.getNombre().equals(NOMBRE_ACTUALIZADO) && loc.getId().equals(LOCALIDAD_ID)
        ));
    }

    @Test
    void testActualizarLocalidad_NoExistente_DebeLanzarExcepcion() {
        // ARRANGE
        LocalidadRequest requestActualizacion = new LocalidadRequest();
        requestActualizacion.setNombre(NOMBRE_ACTUALIZADO);

        when(localidadRepository.findById(LOCALIDAD_ID)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(ResourceNotFoundException.class, () -> {
            localidadService.actualizarLocalidad(LOCALIDAD_ID, requestActualizacion);
        }, "Debe lanzar ResourceNotFoundException si no se encuentra el recurso.");

        // VERIFICACIÓN: No se debe llamar al save()
        verify(localidadRepository, never()).save(any(Localidad.class));
    }

    // =======================================================
    // 4. PRUEBAS PARA eliminarLocalidad(Long id)
    // Cobertura: Éxito y manejo de excepción.
    // =======================================================

    @Test
    void testEliminarLocalidad_Existente_DebeLlamarDelete() {
        // ARRANGE
        when(localidadRepository.findById(LOCALIDAD_ID)).thenReturn(Optional.of(localidad));

        // ACT
        localidadService.eliminarLocalidad(LOCALIDAD_ID);

        // ASSERT
        // Verificar que se llamó a delete() con la entidad encontrada.
        verify(localidadRepository, times(1)).delete(localidad);
    }

    @Test
    void testEliminarLocalidad_NoExistente_DebeLanzarExcepcion() {
        // ARRANGE
        when(localidadRepository.findById(LOCALIDAD_ID)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(ResourceNotFoundException.class, () -> {
            localidadService.eliminarLocalidad(LOCALIDAD_ID);
        }, "Debe lanzar ResourceNotFoundException si no se encuentra el recurso.");

        // VERIFICACIÓN: No se debe llamar a delete()
        verify(localidadRepository, never()).delete(any(Localidad.class));
    }

    // =======================================================
    // 5. PRUEBAS PARA obtenerTodasLocalidades(Pageable pageable, String search)
    // Cobertura: Caminos de búsqueda (con y sin término).
    // =======================================================

    @Test
    void testObtenerTodas_SinBusqueda_DebeLlamarFindAll() {
        // ARRANGE
        Pageable pageable = PageRequest.of(0, 10);
        String search = null; // Caso sin término de búsqueda

        List<Localidad> localidadesList = Arrays.asList(localidad);
        Page<Localidad> localidadPage = new PageImpl<>(localidadesList, pageable, 1);

        // Simular que el método findAll devuelve una página
        when(localidadRepository.findAll(pageable)).thenReturn(localidadPage);

        // Simular el mapeo de la página
        when(localidadMapper.toResponse(any(Localidad.class))).thenReturn(localidadResponse);

        // ACT
        localidadService.obtenerTodasLocalidades(pageable, search);

        // ASSERT
        // Verificar que se llamó al método general de paginación
        verify(localidadRepository, times(1)).findAll(pageable);
        // Verificar que NO se llamó al método de búsqueda específica
        verify(localidadRepository, never()).findByNombreContainingIgnoreCase(anyString(), any(Pageable.class));
    }

    @Test
    void testObtenerTodas_ConBusqueda_DebeLlamarFindByNombre() {
        // ARRANGE
        Pageable pageable = PageRequest.of(0, 10);
        String search = "cádiz"; // Caso con término de búsqueda

        List<Localidad> localidadesList = Arrays.asList(localidad);
        Page<Localidad> localidadPage = new PageImpl<>(localidadesList, pageable, 1);

        // Simular que el método findByNombreContainingIgnoreCase devuelve una página
        when(localidadRepository.findByNombreContainingIgnoreCase(search, pageable)).thenReturn(localidadPage);

        // Simular el mapeo de la página
        when(localidadMapper.toResponse(any(Localidad.class))).thenReturn(localidadResponse);

        // ACT
        localidadService.obtenerTodasLocalidades(pageable, search);

        // ASSERT
        // Verificar que se llamó al método de búsqueda específica
        verify(localidadRepository, times(1)).findByNombreContainingIgnoreCase(search, pageable);
        // Verificar que NO se llamó al método general
        verify(localidadRepository, never()).findAll(any(Pageable.class));
    }
}