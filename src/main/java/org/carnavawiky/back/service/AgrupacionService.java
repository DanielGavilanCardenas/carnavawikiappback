package org.carnavawiky.back.service;

import org.carnavawiky.back.dto.AgrupacionRequest;
import org.carnavawiky.back.dto.AgrupacionResponse;
import org.carnavawiky.back.dto.PageResponse;
import org.carnavawiky.back.exception.ResourceNotFoundException;
import org.carnavawiky.back.mapper.AgrupacionMapper;
import org.carnavawiky.back.model.Agrupacion;
import org.carnavawiky.back.model.Localidad; // << NUEVA IMPORTACIÓN
import org.carnavawiky.back.model.Usuario;
import org.carnavawiky.back.repository.AgrupacionRepository;
import org.carnavawiky.back.repository.LocalidadRepository; // << NUEVA IMPORTACIÓN
import org.carnavawiky.back.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service
public class AgrupacionService {

    public static final String AGRUPACION = "Agrupacion";
    @Autowired
    private AgrupacionRepository agrupacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AgrupacionMapper agrupacionMapper;

    @Autowired
    private LocalidadRepository localidadRepository;


    // =======================================================
    // MÉT 1: CREAR (POST) - MODIFICADO para Localidad
    // =======================================================

    @Transactional
    public AgrupacionResponse crearAgrupacion(AgrupacionRequest request) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuarioCreador = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "username", username));

        // 1. Buscar la Localidad por el ID (si no existe, lanza 404)
        Localidad localidad = localidadRepository.findById(request.getLocalidadId())
                .orElseThrow(() -> new ResourceNotFoundException("Localidad", "id", request.getLocalidadId()));

        // 2. Mapear y guardar, pasando la Localidad
        Agrupacion agrupacion = agrupacionMapper.toEntity(request, usuarioCreador, localidad); // << FIRMA MODIFICADA

        Agrupacion nuevaAgrupacion = agrupacionRepository.save(agrupacion);

        return agrupacionMapper.toResponse(nuevaAgrupacion);
    }

    // =======================================================
    // MÉT 2: OBTENER POR ID (GET /ID)
    // =======================================================

    @Transactional(readOnly = true)
    public AgrupacionResponse obtenerAgrupacionPorId(Long id) {
        Agrupacion agrupacion = agrupacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AGRUPACION, "id", id));

        return agrupacionMapper.toResponse(agrupacion);
    }

    // =======================================================
    // MÉTODO 3: OBTENER TODAS (GET) - CON PAGINACIÓN Y BÚSQUEDA
    // =======================================================
    @Transactional(readOnly = true)
    public PageResponse<AgrupacionResponse> obtenerTodasAgrupaciones(Pageable pageable, String search) {

        Page<Agrupacion> agrupacionPage;

        if (StringUtils.hasText(search)) {
            // Utilizamos el método de búsqueda por nombre o descripción (existente en el repositorio)
            agrupacionPage = agrupacionRepository.findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase(search, search, pageable);
        } else {
            // Paginación simple
            agrupacionPage = agrupacionRepository.findAll(pageable);
        }

        // Mapear el contenido de la página a AgrupacionResponse
        Page<AgrupacionResponse> responsePage = agrupacionPage.map(agrupacionMapper::toResponse);

        // Construir y retornar el objeto PageResponse
        return PageResponse.fromPage(responsePage);
    }

    // =======================================================
    // MÉT 4: ACTUALIZAR (PUT /ID) - MODIFICADO para Localidad
    // =======================================================

    @Transactional
    public AgrupacionResponse actualizarAgrupacion(Long id, AgrupacionRequest request) {
        // 1. Verificar si la entidad existe
        Agrupacion agrupacionExistente = agrupacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AGRUPACION, "id", id));

        // 2. Buscar la Localidad (si no existe, lanza 404)
        Localidad nuevaLocalidad = localidadRepository.findById(request.getLocalidadId())
                .orElseThrow(() -> new ResourceNotFoundException("Localidad", "id", request.getLocalidadId()));

        // 3. Actualizar campos
        agrupacionExistente.setNombre(request.getNombre());
        agrupacionExistente.setDescripcion(request.getDescripcion());
        agrupacionExistente.setModalidad(request.getModalidad());
        agrupacionExistente.setLocalidad(nuevaLocalidad); // << ACTUALIZAR LOCALIDAD
        agrupacionExistente.setAnho(request.getAnho());

        // 4. Guardar y retornar
        Agrupacion agrupacionActualizada = agrupacionRepository.save(agrupacionExistente);

        return agrupacionMapper.toResponse(agrupacionActualizada);
    }

    // =======================================================
    // MÉT 5: ELIMINAR (DELETE /ID)
    // =======================================================

    @Transactional
    public void eliminarAgrupacion(Long id) {
        // 1. Verificar si la entidad existe
        Agrupacion agrupacion = agrupacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AGRUPACION, "id", id));

        // 2. Eliminar
        agrupacionRepository.delete(agrupacion);
    }
}