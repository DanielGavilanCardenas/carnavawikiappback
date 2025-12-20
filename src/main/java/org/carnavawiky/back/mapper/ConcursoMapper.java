package org.carnavawiky.back.mapper;

import org.carnavawiky.back.dto.ConcursoRequest;
import org.carnavawiky.back.dto.ConcursoResponse;
import org.carnavawiky.back.model.Concurso;
import org.carnavawiky.back.model.Localidad;
import org.springframework.stereotype.Component;

@Component
public class ConcursoMapper {

    /**
     * Convierte un ConcursoRequest a una entidad Concurso.
     */
    public Concurso toEntity(ConcursoRequest request, Localidad localidad) {
        Concurso concurso = new Concurso();
        concurso.setNombre(request.getNombre());
        concurso.setEstaActivo(request.getEstaActivo());
        concurso.setLocalidad(localidad);
        return concurso;
    }

    /**
     * Convierte una entidad Concurso a un ConcursoResponse.
     */
    public ConcursoResponse toResponse(Concurso entity) {
        ConcursoResponse response = new ConcursoResponse();
        response.setId(entity.getId());
        response.setNombre(entity.getNombre());
        response.setEstaActivo(entity.getEstaActivo());

        // Mapeo de Localidad
        if (entity.getLocalidad() != null) {
            response.setLocalidadId(entity.getLocalidad().getId());
            response.setLocalidadNombre(entity.getLocalidad().getNombre());
        }

        return response;
    }
}