package org.carnavawiky.back.service;

import org.carnavawiky.back.dto.ConcursoRequest;
import org.carnavawiky.back.dto.ConcursoResponse;
import org.carnavawiky.back.dto.PageResponse;
import org.carnavawiky.back.exception.ResourceNotFoundException;
import org.carnavawiky.back.mapper.ConcursoMapper;
import org.carnavawiky.back.model.Concurso;
import org.carnavawiky.back.model.Localidad;
import org.carnavawiky.back.repository.ConcursoRepository;
import org.carnavawiky.back.repository.LocalidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class ConcursoService {

    public static final String CONCURSO = "Concurso";
    @Autowired
    private ConcursoRepository concursoRepository;

    @Autowired
    private LocalidadRepository localidadRepository;

    @Autowired
    private ConcursoMapper concursoMapper;

    // =======================================================
    // 1. CREAR (POST)
    // =======================================================
    @Transactional
    public ConcursoResponse crearConcurso(ConcursoRequest request) {

        // 1. Buscar Localidad (N:1)
        Localidad localidad = localidadRepository.findById(request.getLocalidadId())
                .orElseThrow(() -> new ResourceNotFoundException("Localidad", "id", request.getLocalidadId()));

        // 2. Mapear y guardar
        Concurso concurso = concursoMapper.toEntity(request, localidad);
        Concurso nuevoConcurso = concursoRepository.save(concurso);

        return concursoMapper.toResponse(nuevoConcurso);
    }

    // =======================================================
    // 2. OBTENER POR ID (GET /ID)
    // =======================================================
    @Transactional(readOnly = true)
    public ConcursoResponse obtenerConcursoPorId(Long id) {
        Concurso concurso = concursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CONCURSO, "id", id));
        return concursoMapper.toResponse(concurso);
    }

    // =======================================================
    // 3. OBTENER TODOS (GET) - CON PAGINACIÓN Y BÚSQUEDA
    // =======================================================
    @Transactional(readOnly = true)
    public PageResponse<ConcursoResponse> obtenerTodosConcursos(Pageable pageable, String search) {

        Page<Concurso> concursoPage;

        if (StringUtils.hasText(search)) {
            // Buscamos por nombre
            concursoPage = concursoRepository.findByNombreContainingIgnoreCase(search, pageable);
        } else {
            // Paginación normal
            concursoPage = concursoRepository.findAll(pageable);
        }

        Page<ConcursoResponse> responsePage = concursoPage.map(concursoMapper::toResponse);

        return PageResponse.fromPage(responsePage);
    }

    // =======================================================
    // 4. ACTUALIZAR (PUT /ID)
    // =======================================================
    @Transactional
    public ConcursoResponse actualizarConcurso(Long id, ConcursoRequest request) {
        Concurso concursoExistente = concursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CONCURSO, "id", id));

        // 1. Buscar Localidad (N:1)
        Localidad nuevaLocalidad = localidadRepository.findById(request.getLocalidadId())
                .orElseThrow(() -> new ResourceNotFoundException("Localidad", "id", request.getLocalidadId()));

        // 2. Actualizar campos
        concursoExistente.setNombre(request.getNombre());
        concursoExistente.setEstaActivo(request.getEstaActivo());
        concursoExistente.setLocalidad(nuevaLocalidad);

        // 3. Guardar y retornar
        Concurso concursoActualizado = concursoRepository.save(concursoExistente);

        return concursoMapper.toResponse(concursoActualizado);
    }

    // =======================================================
    // 5. ELIMINAR (DELETE /ID)
    // =======================================================
    @Transactional
    public void eliminarConcurso(Long id) {
        Concurso concurso = concursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CONCURSO, "id", id));

        // NOTA: La eliminación fallará si existen Ediciones vinculadas (Integridad Referencial)
        concursoRepository.delete(concurso);
    }
}