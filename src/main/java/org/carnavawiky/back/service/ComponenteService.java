package org.carnavawiky.back.service;

import org.carnavawiky.back.dto.ComponenteRequest;
import org.carnavawiky.back.dto.ComponenteResponse;
import org.carnavawiky.back.dto.PageResponse;
import org.carnavawiky.back.exception.ResourceNotFoundException;
import org.carnavawiky.back.mapper.ComponenteMapper;
import org.carnavawiky.back.model.Agrupacion;
import org.carnavawiky.back.model.Componente;
import org.carnavawiky.back.model.Persona;
import org.carnavawiky.back.repository.AgrupacionRepository;
import org.carnavawiky.back.repository.ComponenteRepository;
import org.carnavawiky.back.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class ComponenteService {

    public static final String COMPONENTE = "Componente";
    @Autowired
    private ComponenteRepository componenteRepository;

    @Autowired
    private AgrupacionRepository agrupacionRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private ComponenteMapper componenteMapper;

    // =======================================================
    // Helpers para relaciones
    // =======================================================

    private Persona findPersona(Long id) {
        return personaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Persona", "id", id));
    }

    private Agrupacion findAgrupacion(Long id) {
        return agrupacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agrupacion", "id", id));
    }

    // =======================================================
    // 1. CREAR (POST)
    // =======================================================
    @Transactional
    public ComponenteResponse crearComponente(ComponenteRequest request) {

        // 1. Buscar entidades relacionadas
        Persona persona = findPersona(request.getPersonaId());
        Agrupacion agrupacion = findAgrupacion(request.getAgrupacionId());

        // 2. Mapear y guardar (la restricción de unicidad la gestiona la base de datos)
        Componente componente = componenteMapper.toEntity(request, persona, agrupacion);
        Componente nuevoComponente = componenteRepository.save(componente);

        return componenteMapper.toResponse(nuevoComponente);
    }

    // =======================================================
    // 2. OBTENER POR ID (GET /ID)
    // =======================================================
    @Transactional(readOnly = true)
    public ComponenteResponse obtenerComponentePorId(Long id) {
        Componente componente = componenteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(COMPONENTE, "id", id));
        return componenteMapper.toResponse(componente);
    }

    // =======================================================
    // 3. OBTENER TODOS (GET) - CON PAGINACIÓN Y BÚSQUEDA
    // =======================================================
    @Transactional(readOnly = true)
    public PageResponse<ComponenteResponse> obtenerTodosComponentes(Pageable pageable, String search) {

        Page<Componente> componentePage;

        if (StringUtils.hasText(search)) {
            // Buscamos por nombre real, apodo o nombre de agrupación
            componentePage = componenteRepository.findBySearchTerm(search, pageable);
        } else {
            // Paginación normal
            componentePage = componenteRepository.findAll(pageable);
        }

        Page<ComponenteResponse> responsePage = componentePage.map(componenteMapper::toResponse);

        return PageResponse.fromPage(responsePage);
    }

    // =======================================================
    // 4. ACTUALIZAR (PUT /ID)
    // =======================================================
    @Transactional
    public ComponenteResponse actualizarComponente(Long id, ComponenteRequest request) {
        Componente componenteExistente = componenteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(COMPONENTE, "id", id));

        // 1. Buscar entidades relacionadas (se necesita de nuevo si el ID cambió)
        Persona nuevaPersona = findPersona(request.getPersonaId());
        Agrupacion nuevaAgrupacion = findAgrupacion(request.getAgrupacionId());

        // 2. Actualizar campos
        componenteExistente.setRol(request.getRol());
        componenteExistente.setPersona(nuevaPersona);
        componenteExistente.setAgrupacion(nuevaAgrupacion);

        // 3. Guardar y retornar
        Componente componenteActualizado = componenteRepository.save(componenteExistente);

        return componenteMapper.toResponse(componenteActualizado);
    }

    // =======================================================
    // 5. ELIMINAR (DELETE /ID)
    // =======================================================
    @Transactional
    public void eliminarComponente(Long id) {
        Componente componente = componenteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(COMPONENTE, "id", id));

        componenteRepository.delete(componente);
    }
}