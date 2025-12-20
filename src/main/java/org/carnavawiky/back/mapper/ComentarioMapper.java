package org.carnavawiky.back.mapper;

import org.carnavawiky.back.dto.ComentarioRequest;
import org.carnavawiky.back.dto.ComentarioResponse;
import org.carnavawiky.back.model.Agrupacion;
import org.carnavawiky.back.model.Comentario;
import org.carnavawiky.back.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class ComentarioMapper {

    /**
     * Convierte un ComentarioRequest a una entidad Comentario.
     */
    public Comentario toEntity(ComentarioRequest request, Usuario usuario, Agrupacion agrupacion) {
        Comentario comentario = new Comentario();
        comentario.setContenido(request.getContenido());
        comentario.setPuntuacion(request.getPuntuacion());
        comentario.setUsuario(usuario);
        comentario.setAgrupacion(agrupacion);
        return comentario;
    }

    /**
     * Convierte una entidad Comentario a un ComentarioResponse.
     */
    public ComentarioResponse toResponse(Comentario entity) {
        ComentarioResponse response = new ComentarioResponse();
        response.setId(entity.getId());
        response.setContenido(entity.getContenido());
        response.setPuntuacion(entity.getPuntuacion());
        response.setFechaCreacion(entity.getFechaCreacion());

        // Mapeo de Usuario
        if (entity.getUsuario() != null) {
            response.setUsuarioId(entity.getUsuario().getId());
            response.setUsuarioUsername(entity.getUsuario().getUsername());
        }

        // Mapeo de Agrupación
        if (entity.getAgrupacion() != null) {
            response.setAgrupacionId(entity.getAgrupacion().getId());
            response.setAgrupacionNombre(entity.getAgrupacion().getNombre());
        }

        return response;
    }
}