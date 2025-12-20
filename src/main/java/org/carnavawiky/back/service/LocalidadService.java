package org.carnavawiky.back.service;

import org.carnavawiky.back.dto.LocalidadRequest;
import org.carnavawiky.back.dto.LocalidadResponse;
import org.carnavawiky.back.dto.PageResponse;
import org.carnavawiky.back.exception.ResourceNotFoundException;
import org.carnavawiky.back.mapper.LocalidadMapper;
import org.carnavawiky.back.model.Localidad;
import org.carnavawiky.back.repository.LocalidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class LocalidadService {

    public static final String LOCALIDAD = "Localidad";
    @Autowired
    private LocalidadRepository localidadRepository;

    @Autowired
    private LocalidadMapper localidadMapper;

    // =======================================================
    // 1. CREAR (POST)
    // Cuando creamos, invalidamos la lista y la caché por ID, ya que hay un nuevo recurso.
    // =======================================================
    @Transactional
    @CacheEvict(value = {"localidades_list", "localidad_by_id"}, allEntries = true)
    public LocalidadResponse crearLocalidad(LocalidadRequest request) {
        Localidad localidad = localidadMapper.toEntity(request);
        Localidad nuevaLocalidad = localidadRepository.save(localidad);
        return localidadMapper.toResponse(nuevaLocalidad);
    }

    // =======================================================
    // 2. OBTENER POR ID (GET /ID)
    // El resultado se almacena en el caché "localidad_by_id", usando el ID como clave.
    // =======================================================
    @Transactional(readOnly = true)
    @Cacheable(value = "localidad_by_id", key = "#id")
    public LocalidadResponse obtenerLocalidadPorId(Long id) {
        Localidad localidad = localidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(LOCALIDAD, "id", id));
        return localidadMapper.toResponse(localidad);
    }

    // =======================================================
    // 3. OBTENER TODAS (GET) - CON PAGINACIÓN Y BÚSQUEDA
    // El resultado se almacena en el caché "localidades_list".
    // La clave debe incluir todos los parámetros (página, tamaño, búsqueda) para diferenciar cachés.
    // =======================================================
    @Transactional(readOnly = true)
    @Cacheable(value = "localidades_list", key = "#pageable.pageNumber + '_' + #pageable.pageSize + '_' + #search")
    public PageResponse<LocalidadResponse> obtenerTodasLocalidades(Pageable pageable, String search) {

        Page<Localidad> localidadPage;

        if (StringUtils.hasText(search)) {
            // Si hay término de búsqueda, buscamos por nombre
            localidadPage = localidadRepository.findByNombreContainingIgnoreCase(search, pageable);
        } else {
            // Si no hay búsqueda, usamos la paginación normal
            localidadPage = localidadRepository.findAll(pageable);
        }

        Page<LocalidadResponse> responsePage = localidadPage.map(localidadMapper::toResponse);

        return PageResponse.fromPage(responsePage);
    }

    // =======================================================
    // 4. ACTUALIZAR (PUT /ID)
    // Al actualizar, invalidamos la lista y el objeto individual.
    // =======================================================
    @Transactional
    @CacheEvict(value = {"localidades_list", "localidad_by_id"}, allEntries = true)
    public LocalidadResponse actualizarLocalidad(Long id, LocalidadRequest request) {
        Localidad localidadExistente = localidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(LOCALIDAD, "id", id));

        localidadExistente.setNombre(request.getNombre());

        Localidad localidadActualizada = localidadRepository.save(localidadExistente);

        return localidadMapper.toResponse(localidadActualizada);
    }

    // =======================================================
    // 5. ELIMINAR (DELETE /ID)
    // Al eliminar, invalidamos la lista y el objeto individual.
    // =======================================================
    @Transactional
    @CacheEvict(value = {"localidades_list", "localidad_by_id"}, allEntries = true)
    public void eliminarLocalidad(Long id) {
        Localidad localidad = localidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(LOCALIDAD, "id", id));

        localidadRepository.delete(localidad);
    }
}