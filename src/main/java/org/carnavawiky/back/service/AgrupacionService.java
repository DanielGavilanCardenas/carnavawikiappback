package org.carnavawiky.back.service;

import org.carnavawiky.back.dto.AgrupacionRequest;
import org.carnavawiky.back.dto.AgrupacionResponse;
import org.carnavawiky.back.exception.ResourceNotFoundException;
import org.carnavawiky.back.mapper.AgrupacionMapper;
import org.carnavawiky.back.model.Agrupacion;
import org.carnavawiky.back.model.Usuario;
import org.carnavawiky.back.repository.AgrupacionRepository;
import org.carnavawiky.back.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgrupacionService {

    @Autowired
    private AgrupacionRepository agrupacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AgrupacionMapper agrupacionMapper;

    // =======================================================
    // MÉTODO 1: CREAR (POST)
    // =======================================================

    @Transactional
    public AgrupacionResponse crearAgrupacion(AgrupacionRequest request) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuarioCreador = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "username", username));

        Agrupacion agrupacion = agrupacionMapper.toEntity(request, usuarioCreador);

        Agrupacion nuevaAgrupacion = agrupacionRepository.save(agrupacion);

        return agrupacionMapper.toResponse(nuevaAgrupacion);
    }

    // =======================================================
    // MÉTODO 2: OBTENER POR ID (GET /ID)
    // =======================================================

    @Transactional(readOnly = true)
    public AgrupacionResponse obtenerAgrupacionPorId(Long id) {
        Agrupacion agrupacion = agrupacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agrupacion", "id", id));

        return agrupacionMapper.toResponse(agrupacion);
    }

    // =======================================================
    // MÉTODO 3: OBTENER TODAS (GET)
    // =======================================================

    @Transactional(readOnly = true)
    public List<AgrupacionResponse> obtenerTodasAgrupaciones() {
        return agrupacionRepository.findAll().stream()
                .map(agrupacionMapper::toResponse)
                .collect(Collectors.toList());
    }

    // =======================================================
    // MÉTODO 4: ACTUALIZAR (PUT /ID)
    // =======================================================

    @Transactional
    public AgrupacionResponse actualizarAgrupacion(Long id, AgrupacionRequest request) {
        // 1. Verificar si la entidad existe (y lanzar 404 si no)
        Agrupacion agrupacionExistente = agrupacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agrupacion", "id", id));

        // 2. Actualizar campos
        agrupacionExistente.setNombre(request.getNombre());
        agrupacionExistente.setDescripcion(request.getDescripcion());

        // NOTA: El usuarioCreador y la fechaAlta no se modifican, son de auditoría.

        // 3. Guardar y retornar
        Agrupacion agrupacionActualizada = agrupacionRepository.save(agrupacionExistente);

        return agrupacionMapper.toResponse(agrupacionActualizada);
    }

    // =======================================================
    // MÉTODO 5: ELIMINAR (DELETE /ID)
    // =======================================================

    @Transactional
    public void eliminarAgrupacion(Long id) {
        // 1. Verificar si la entidad existe
        Agrupacion agrupacion = agrupacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agrupacion", "id", id));

        // 2. Eliminar
        agrupacionRepository.delete(agrupacion);
    }
}