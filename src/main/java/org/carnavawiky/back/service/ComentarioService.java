package org.carnavawiky.back.service;

import org.carnavawiky.back.dto.ComentarioRequest;
import org.carnavawiky.back.dto.ComentarioResponse;
import org.carnavawiky.back.dto.PageResponse;
import org.carnavawiky.back.exception.ResourceNotFoundException;
import org.carnavawiky.back.mapper.ComentarioMapper;
import org.carnavawiky.back.model.Agrupacion;
import org.carnavawiky.back.model.Comentario;
import org.carnavawiky.back.model.Usuario;
import org.carnavawiky.back.repository.AgrupacionRepository;
import org.carnavawiky.back.repository.ComentarioRepository;
import org.carnavawiky.back.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class ComentarioService {

    public static final String COMENTARIO = "Comentario" ;
    public static final String COMENTARIO1 = "Comentario1";
    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private AgrupacionRepository agrupacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ComentarioMapper comentarioMapper;

    // =======================================================
    // Helpers para relaciones
    // =======================================================

    private Usuario findUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
    }

    private Agrupacion findAgrupacion(Long id) {
        return agrupacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agrupacion", "id", id));
    }

    // =======================================================
    // 1. CREAR (POST)
    // =======================================================
    @Transactional
    public ComentarioResponse crearComentario(ComentarioRequest request) {

        // 1. Buscar entidades relacionadas
        Usuario usuario = findUsuario(request.getUsuarioId());
        Agrupacion agrupacion = findAgrupacion(request.getAgrupacionId());

        // 2. Mapear y guardar (Comentario.aprobado por defecto es false)
        Comentario comentario = comentarioMapper.toEntity(request, usuario, agrupacion);
        Comentario nuevoComentario = comentarioRepository.save(comentario);

        return comentarioMapper.toResponse(nuevoComentario);
    }

    // =======================================================
    // 2. OBTENER POR ID (GET /ID)
    // =======================================================
    @Transactional(readOnly = true)
    public ComentarioResponse obtenerComentarioPorId(Long id) {
        Comentario comentario = comentarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(COMENTARIO, "id", id));

        // Se puede añadir una lógica aquí para denegar el acceso a comentarios no aprobados
        // si el usuario no es ADMIN, pero por ahora se permite a nivel de servicio.
        return comentarioMapper.toResponse(comentario);
    }

    // =======================================================
    // 3. OBTENER TODOS (GET) - CON PAGINACIÓN Y BÚSQUEDA
    // Filtra por 'aprobado=true' si se usa búsqueda (uso público).
    // Si no hay búsqueda, devuelve todos (uso de moderación/admin).
    // =======================================================
    @Transactional(readOnly = true)
    public PageResponse<ComentarioResponse> obtenerTodosComentarios(Pageable pageable, String search) {

        Page<Comentario> comentarioPage;

        if (StringUtils.hasText(search)) {
            try {
                // 1. Intentamos buscar por ID de Agrupación (SOLO APROBADOS)
                Long agrupacionId = Long.parseLong(search);
                comentarioPage = comentarioRepository.findByAgrupacion_IdAndAprobadoTrueOrderByFechaCreacionDesc(agrupacionId, pageable);
            } catch (NumberFormatException e) {
                // 2. Si no es un número, buscamos por contenido (SOLO APROBADOS)
                comentarioPage = comentarioRepository.findByContenidoContainingIgnoreCaseAndAprobadoTrue(search, pageable);
            }
        } else {
            // Paginación normal. Para el uso de ADMIN/Moderación.
            // Si esto fuese un endpoint público, usaríamos findByAprobadoTrue(pageable).
            comentarioPage = comentarioRepository.findAll(pageable);
        }

        Page<ComentarioResponse> responsePage = comentarioPage.map(comentarioMapper::toResponse);

        return PageResponse.fromPage(responsePage);
    }

    // =======================================================
    // 4. ACTUALIZAR (PUT /ID)
    // =======================================================
    @Transactional
    public ComentarioResponse actualizarComentario(Long id, ComentarioRequest request) {
        Comentario comentarioExistente = comentarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(COMENTARIO, "id", id));

        // 1. Buscar entidades relacionadas (se necesita de nuevo si los IDs cambiaron)
        Usuario nuevoUsuario = findUsuario(request.getUsuarioId());
        Agrupacion nuevaAgrupacion = findAgrupacion(request.getAgrupacionId());

        // 2. Actualizar campos
        comentarioExistente.setContenido(request.getContenido());
        comentarioExistente.setPuntuacion(request.getPuntuacion());
        comentarioExistente.setUsuario(nuevoUsuario);
        comentarioExistente.setAgrupacion(nuevaAgrupacion);
        // NOTA: fechaCreacion y 'aprobado' no se modifican aquí (se requiere un endpoint específico para aprobar)

        // 3. Guardar y retornar
        Comentario comentarioActualizado = comentarioRepository.save(comentarioExistente);

        return comentarioMapper.toResponse(comentarioActualizado);
    }

    // =======================================================
    // 5. ELIMINAR (DELETE /ID)
    // =======================================================
    @Transactional
    public void eliminarComentario(Long id) {
        Comentario comentario = comentarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(COMENTARIO1, "id", id));

        comentarioRepository.delete(comentario);
    }

    // =======================================================
    // 6. APROBAR COMENTARIO (NUEVA FUNCIONALIDAD ADMIN)
    // =======================================================
    @Transactional
    public ComentarioResponse aprobarComentario(Long id) {
        Comentario comentario = comentarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(COMENTARIO1, "id", id));

        comentario.setAprobado(true);
        Comentario comentarioActualizado = comentarioRepository.save(comentario);

        return comentarioMapper.toResponse(comentarioActualizado);
    }
}