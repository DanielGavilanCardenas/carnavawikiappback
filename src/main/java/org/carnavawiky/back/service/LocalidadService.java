package org.carnavawiky.back.service;

import org.carnavawiky.back.dto.LocalidadRequest;
import org.carnavawiky.back.dto.LocalidadResponse;
import org.carnavawiky.back.dto.PageResponse;
import org.carnavawiky.back.exception.ResourceNotFoundException;
import org.carnavawiky.back.mapper.LocalidadMapper;
import org.carnavawiky.back.model.Localidad;
import org.carnavawiky.back.repository.LocalidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class LocalidadService {

    @Autowired
    private LocalidadRepository localidadRepository;

    @Autowired
    private LocalidadMapper localidadMapper;

    // =======================================================
    // 1. CREAR (POST)
    // =======================================================
    @Transactional
    public LocalidadResponse crearLocalidad(LocalidadRequest request) {
        Localidad localidad = localidadMapper.toEntity(request);
        Localidad nuevaLocalidad = localidadRepository.save(localidad);
        return localidadMapper.toResponse(nuevaLocalidad);
    }

    // =======================================================
    // 2. OBTENER POR ID (GET /ID)
    // =======================================================
    @Transactional(readOnly = true)
    public LocalidadResponse obtenerLocalidadPorId(Long id) {
        Localidad localidad = localidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Localidad", "id", id));
        return localidadMapper.toResponse(localidad);
    }

    // =======================================================
    // 3. OBTENER TODAS (GET) - CON PAGINACIÓN Y BÚSQUEDA
    // =======================================================
    @Transactional(readOnly = true)
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
    // =======================================================
    @Transactional
    public LocalidadResponse actualizarLocalidad(Long id, LocalidadRequest request) {
        Localidad localidadExistente = localidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Localidad", "id", id));

        localidadExistente.setNombre(request.getNombre());

        Localidad localidadActualizada = localidadRepository.save(localidadExistente);

        return localidadMapper.toResponse(localidadActualizada);
    }

    // =======================================================
    // 5. ELIMINAR (DELETE /ID)
    // NOTA: Si esta localidad está referenciada por una Agrupacion,
    // Hibernate lanzará una excepción de restricción de clave foránea.
    // Esto es deseado para mantener la integridad referencial.
    // =======================================================
    @Transactional
    public void eliminarLocalidad(Long id) {
        Localidad localidad = localidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Localidad", "id", id));

        localidadRepository.delete(localidad);
    }
}