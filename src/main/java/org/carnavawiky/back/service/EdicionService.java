package org.carnavawiky.back.service;

import org.carnavawiky.back.dto.EdicionRequest;
import org.carnavawiky.back.dto.EdicionResponse;
import org.carnavawiky.back.dto.PageResponse;
import org.carnavawiky.back.exception.ResourceNotFoundException;
import org.carnavawiky.back.mapper.EdicionMapper;
import org.carnavawiky.back.model.Concurso;
import org.carnavawiky.back.model.Edicion;
import org.carnavawiky.back.repository.ConcursoRepository;
import org.carnavawiky.back.repository.EdicionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class EdicionService {

    public static final String EDICION = "Edicion";
    @Autowired
    private EdicionRepository edicionRepository;

    @Autowired
    private ConcursoRepository concursoRepository; // Necesario para buscar la relación

    @Autowired
    private EdicionMapper edicionMapper;

    // =======================================================
    // Helpers
    // =======================================================

    private Concurso obtenerConcurso(Long concursoId) {
        return concursoRepository.findById(concursoId)
                .orElseThrow(() -> new ResourceNotFoundException("Concurso", "id", concursoId));
    }

    // =======================================================
    // 1. CREAR (POST)
    // =======================================================
    @Transactional
    public EdicionResponse crearEdicion(EdicionRequest request) {

        // 1. Buscar Concurso (N:1)
        Concurso concurso = obtenerConcurso(request.getConcursoId());

        // 2. Mapear y guardar (la restricción de unicidad la maneja la base de datos)
        Edicion edicion = edicionMapper.toEntity(request, concurso);
        Edicion nuevaEdicion = edicionRepository.save(edicion);

        return edicionMapper.toResponse(nuevaEdicion);
    }

    // =======================================================
    // 2. OBTENER POR ID (GET /ID)
    // =======================================================
    @Transactional(readOnly = true)
    public EdicionResponse obtenerEdicionPorId(Long id) {
        Edicion edicion = edicionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(EDICION, "id", id));
        return edicionMapper.toResponse(edicion);
    }

    // =======================================================
    // 3. OBTENER TODAS (GET) - CON PAGINACIÓN Y BÚSQUEDA
    // NOTA: Se buscará por año o por nombre de concurso
    // =======================================================
    @Transactional(readOnly = true)
    public PageResponse<EdicionResponse> obtenerTodasEdiciones(Pageable pageable, String search) {

        Page<Edicion> edicionPage;

        if (StringUtils.hasText(search)) {
            // Intentamos buscar por año (si es un número)
            try {
                Integer anho = Integer.parseInt(search);
                edicionPage = edicionRepository.findByAnho(anho, pageable);
            } catch (NumberFormatException e) {
                // Si no es un número, buscamos por nombre del Concurso
                edicionPage = edicionRepository.findByConcurso_NombreContainingIgnoreCase(search, pageable);
            }
        } else {
            // Paginación normal
            edicionPage = edicionRepository.findAll(pageable);
        }

        Page<EdicionResponse> responsePage = edicionPage.map(edicionMapper::toResponse);

        return PageResponse.fromPage(responsePage);
    }

    // =======================================================
    // 4. ACTUALIZAR (PUT /ID)
    // =======================================================
    @Transactional
    public EdicionResponse actualizarEdicion(Long id, EdicionRequest request) {
        Edicion edicionExistente = edicionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(EDICION, "id", id));

        // 1. Buscar Concurso (N:1) (Se necesita de nuevo si el ID cambió)
        Concurso nuevoConcurso = obtenerConcurso(request.getConcursoId());

        // 2. Actualizar campos
        edicionExistente.setAnho(request.getAnho());
        edicionExistente.setConcurso(nuevoConcurso);

        // 3. Guardar y retornar
        Edicion edicionActualizada = edicionRepository.save(edicionExistente);

        return edicionMapper.toResponse(edicionActualizada);
    }

    // =======================================================
    // 5. ELIMINAR (DELETE /ID)
    // =======================================================
    @Transactional
    public void eliminarEdicion(Long id) {
        Edicion edicion = edicionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(EDICION, "id", id));

        // NOTA: La eliminación fallará si existen Premios vinculados (Integridad Referencial)
        edicionRepository.delete(edicion);
    }
}