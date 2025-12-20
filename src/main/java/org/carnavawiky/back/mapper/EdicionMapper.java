package org.carnavawiky.back.mapper;

import org.carnavawiky.back.dto.EdicionRequest;
import org.carnavawiky.back.dto.EdicionResponse;
import org.carnavawiky.back.model.Concurso;
import org.carnavawiky.back.model.Edicion;
import org.springframework.stereotype.Component;

@Component
public class EdicionMapper {

    /**
     * Convierte un EdicionRequest a una entidad Edicion.
     */
    public Edicion toEntity(EdicionRequest request, Concurso concurso) {
        Edicion edicion = new Edicion();
        edicion.setAnho(request.getAnho());
        edicion.setConcurso(concurso);
        return edicion;
    }

    /**
     * Convierte una entidad Edicion a un EdicionResponse.
     */
    public EdicionResponse toResponse(Edicion entity) {
        EdicionResponse response = new EdicionResponse();
        response.setId(entity.getId());
        response.setAnho(entity.getAnho());

        // Mapeo de Concurso
        if (entity.getConcurso() != null) {
            response.setConcursoId(entity.getConcurso().getId());
            response.setConcursoNombre(entity.getConcurso().getNombre());
        }

        return response;
    }
}