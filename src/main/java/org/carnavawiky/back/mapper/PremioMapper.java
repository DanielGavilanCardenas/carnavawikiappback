package org.carnavawiky.back.mapper;

import org.carnavawiky.back.dto.PremioRequest;
import org.carnavawiky.back.dto.PremioResponse;
import org.carnavawiky.back.model.Agrupacion;
import org.carnavawiky.back.model.Edicion;
import org.carnavawiky.back.model.Premio;
import org.springframework.stereotype.Component;

@Component
public class PremioMapper {

    /**
     * Convierte un PremioRequest a una entidad Premio.
     */
    public Premio toEntity(PremioRequest request, Agrupacion agrupacion, Edicion edicion) {
        Premio premio = new Premio();
        premio.setPuesto(request.getPuesto());
        premio.setModalidad(request.getModalidad());
        premio.setAgrupacion(agrupacion);
        premio.setEdicion(edicion);
        return premio;
    }

    /**
     * Convierte una entidad Premio a un PremioResponse.
     */
    public PremioResponse toResponse(Premio entity) {
        PremioResponse response = new PremioResponse();
        response.setId(entity.getId());
        response.setPuesto(entity.getPuesto());
        response.setModalidad(entity.getModalidad());

        // Mapeo de Agrupación
        if (entity.getAgrupacion() != null) {
            response.setAgrupacionId(entity.getAgrupacion().getId());
            response.setAgrupacionNombre(entity.getAgrupacion().getNombre());
        }

        // Mapeo de Edición y Concurso (asumiendo que Edicion tiene la relación Concurso)
        if (entity.getEdicion() != null) {
            response.setEdicionId(entity.getEdicion().getId());
            response.setEdicionAnho(entity.getEdicion().getAnho());
            if (entity.getEdicion().getConcurso() != null) {
                response.setConcursoNombre(entity.getEdicion().getConcurso().getNombre());
            }
        }

        return response;
    }
}