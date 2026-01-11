package org.carnavawiky.back.service;

import org.carnavawiky.back.dto.*;
import org.carnavawiky.back.exception.ResourceNotFoundException;
import org.carnavawiky.back.mapper.AgrupacionMapper;
import org.carnavawiky.back.model.Agrupacion;
import org.carnavawiky.back.model.Imagen;
import org.carnavawiky.back.model.Localidad;
import org.carnavawiky.back.model.Usuario;
import org.carnavawiky.back.repository.AgrupacionRepository;
import org.carnavawiky.back.repository.LocalidadRepository;
import org.carnavawiky.back.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.util.List;
import java.util.stream.Collectors;


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
            agrupacionPage = agrupacionRepository.findAllWithDetails(pageable);
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


    /**
     * Obtiene una agrupación y transforma su lista de vídeos internos
     * en una lista de VideoResponse para el Frontend.
     */
    @Transactional(readOnly = true)
    public List<VideoResponse> obtenerVideosDeAgrupacion(Long agrupacionId) {
        Agrupacion agrupacion = agrupacionRepository.findById(agrupacionId)
                .orElseThrow(() -> new RuntimeException("Agrupación no encontrada"));

        return agrupacion.getVideos().stream()
                .map(video -> VideoResponse.builder()
                        .id(video.getId())
                        .titulo(video.getTitulo())
                        .urlYoutube(video.getUrlYoutube())
                        .verificado(video.isVerificado())
                        .agrupacionId(agrupacion.getId())
                        .agrupacionNombre(agrupacion.getNombre())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AgrupacionFullResponse> obtenerTodasCompleto() {
        return agrupacionRepository.findAll().stream()
                .map(this::mapToFullResponse)
                .toList();
    }

    private AgrupacionFullResponse mapToFullResponse(Agrupacion a) {
        return new AgrupacionFullResponse(
                a.getId(),
                a.getNombre(),
                a.getAnho(),
                a.isOficial(), // << AÑADIDO: Ahora coincide con el constructor del Record
                a.getModalidad().name(),
                a.getLocalidad() != null ? a.getLocalidad().getNombre() : "Desconocida",

                // Mapeo de Objetos Imagen Completos
                a.getImagenes().stream()
                        .map(img -> ImagenResponse.builder()
                                .id(img.getId())
                                .nombreFichero(img.getNombreFichero())
                                .urlPublica(img.getUrlPublica())
                                .esPortada(img.getEsPortada())
                                .build())
                        .toList(),

                // Mapeo de Componentes
                a.getComponentes().stream()
                        .map(c -> new ComponenteDetalleDto(
                                c.getPersona().getNombreReal(),
                                c.getPersona().getApodo(),
                                c.getRol().name()
                        )).toList(),

                // Mapeo de Vídeos
                a.getVideos().stream()
                        .map(v -> VideoResponse.builder()
                                .id(v.getId())
                                .titulo(v.getTitulo())
                                .urlYoutube(v.getUrlYoutube())
                                .verificado(v.isVerificado())
                                .agrupacionId(a.getId())
                                .agrupacionNombre(a.getNombre())
                                .build())
                        .toList()
        );
    }
}