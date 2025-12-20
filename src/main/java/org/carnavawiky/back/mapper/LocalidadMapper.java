package org.carnavawiky.back.mapper;

import org.carnavawiky.back.dto.LocalidadRequest;
import org.carnavawiky.back.dto.LocalidadResponse;
import org.carnavawiky.back.model.Localidad;
import org.springframework.stereotype.Component;

@Component
public class LocalidadMapper {

    /**
     * Convierte un LocalidadRequest a una entidad Localidad.
     * Retorna null si el request es null.
     */
    public Localidad toEntity(LocalidadRequest request) {
        if (request == null) {
            return null;
        }
        Localidad localidad = new Localidad();
        localidad.setNombre(request.getNombre());
        return localidad;
    }

    /**
     * Convierte una entidad Localidad a un LocalidadResponse.
     * Retorna null si la entidad es null.
     */
    public LocalidadResponse toResponse(Localidad entity) {
        if (entity == null) {
            return null;
        }
        LocalidadResponse response = new LocalidadResponse();
        response.setId(entity.getId());
        response.setNombre(entity.getNombre());
        return response;
    }
}