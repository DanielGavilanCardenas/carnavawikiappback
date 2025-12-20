package org.carnavawiky.back.service;

import org.carnavawiky.back.dto.PageResponse;
import org.carnavawiky.back.dto.PremioRequest;
import org.carnavawiky.back.dto.PremioResponse;
import org.carnavawiky.back.exception.ResourceNotFoundException;
import org.carnavawiky.back.mapper.PremioMapper;
import org.carnavawiky.back.model.Agrupacion;
import org.carnavawiky.back.model.Edicion;
import org.carnavawiky.back.model.Premio;
import org.carnavawiky.back.repository.AgrupacionRepository;
import org.carnavawiky.back.repository.EdicionRepository;
import org.carnavawiky.back.repository.PremioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class PremioService {

    public static final String PREMIO = "Premio";
    @Autowired
    private PremioRepository premioRepository;

    @Autowired
    private AgrupacionRepository agrupacionRepository;

    @Autowired
    private EdicionRepository edicionRepository;

    @Autowired
    private PremioMapper premioMapper;

    // =======================================================
    // Helpers
    // =======================================================

    private Agrupacion obtenerAgrupacion(Long agrupacionId) {
        return agrupacionRepository.findById(agrupacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Agrupacion", "id", agrupacionId));
    }

    private Edicion obtenerEdicion(Long edicionId) {
        return edicionRepository.findById(edicionId)
                .orElseThrow(() -> new ResourceNotFoundException("Edicion", "id", edicionId));
    }

    // =======================================================
    // 1. CREAR (POST)
    // =======================================================
    @Transactional
    public PremioResponse crearPremio(PremioRequest request) {

        // 1. Buscar entidades relacionadas
        Agrupacion agrupacion = obtenerAgrupacion(request.getAgrupacionId());
        Edicion edicion = obtenerEdicion(request.getEdicionId());

        // 2. Mapear y guardar (las restricciones de unicidad las gestiona la base de datos)
        Premio premio = premioMapper.toEntity(request, agrupacion, edicion);
        Premio nuevoPremio = premioRepository.save(premio);

        return premioMapper.toResponse(nuevoPremio);
    }

    // =======================================================
    // 2. OBTENER POR ID (GET /ID)
    // =======================================================
    @Transactional(readOnly = true)
    public PremioResponse obtenerPremioPorId(Long id) {
        Premio premio = premioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PREMIO, "id", id));
        return premioMapper.toResponse(premio);
    }

    // =======================================================
    // 3. OBTENER TODOS (GET) - CON PAGINACIÓN Y BÚSQUEDA
    // Búsqueda por año (si es número) o por nombre de agrupación
    // =======================================================
    @Transactional(readOnly = true)
    public PageResponse<PremioResponse> obtenerTodosPremios(Pageable pageable, String search) {

        Page<Premio> premioPage;

        if (StringUtils.hasText(search)) {
            // 1. Intentamos buscar por año (si es un número)
            try {
                Integer anho = Integer.parseInt(search);
                premioPage = premioRepository.findByEdicion_Anho(anho, pageable);
            } catch (NumberFormatException e) {
                // 2. Si no es un número, buscamos por nombre de Agrupación
                premioPage = premioRepository.findByAgrupacion_NombreContainingIgnoreCase(search, pageable);
            }
        } else {
            // Paginación normal
            premioPage = premioRepository.findAll(pageable);
        }

        Page<PremioResponse> responsePage = premioPage.map(premioMapper::toResponse);

        return PageResponse.fromPage(responsePage);
    }

    // =======================================================
    // 4. ACTUALIZAR (PUT /ID)
    // =======================================================
    @Transactional
    public PremioResponse actualizarPremio(Long id, PremioRequest request) {
        Premio premioExistente = premioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PREMIO, "id", id));

        // 1. Buscar entidades relacionadas (se necesita de nuevo si el ID cambió)
        Agrupacion nuevaAgrupacion = obtenerAgrupacion(request.getAgrupacionId());
        Edicion nuevaEdicion = obtenerEdicion(request.getEdicionId());

        // 2. Actualizar campos
        premioExistente.setPuesto(request.getPuesto());
        premioExistente.setModalidad(request.getModalidad());
        premioExistente.setAgrupacion(nuevaAgrupacion);
        premioExistente.setEdicion(nuevaEdicion);

        // 3. Guardar y retornar
        Premio premioActualizado = premioRepository.save(premioExistente);

        return premioMapper.toResponse(premioActualizado);
    }

    // =======================================================
    // 5. ELIMINAR (DELETE /ID)
    // =======================================================
    @Transactional
    public void eliminarPremio(Long id) {
        Premio premio = premioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PREMIO, "id", id));

        premioRepository.delete(premio);
    }
}