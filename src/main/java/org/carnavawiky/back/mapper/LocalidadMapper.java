package org.carnavawiky.back.mapper;

import org.carnavawiky.back.dto.LocalidadRequest;
import org.carnavawiky.back.dto.LocalidadResponse;
import org.carnavawiky.back.model.Localidad;
import org.springframework.stereotype.Component;

@Component
public class LocalidadMapper {

    /**
     * Convierte un LocalidadRequest a una entidad Localidad.
     */
    public Localidad toEntity(LocalidadRequest request) {
        Localidad localidad = new Localidad();
        localidad.setNombre(request.getNombre());
        return localidad;
    }

    /**
     * Convierte una entidad Localidad a un LocalidadResponse.
     */
    public LocalidadResponse toResponse(Localidad entity) {
        LocalidadResponse response = new LocalidadResponse();
        response.setId(entity.getId());
        response.setNombre(entity.getNombre());
        return response;
    }
}