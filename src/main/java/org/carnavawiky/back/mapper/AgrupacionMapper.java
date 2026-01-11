package org.carnavawiky.back.mapper;

import org.carnavawiky.back.dto.AgrupacionRequest;
import org.carnavawiky.back.dto.AgrupacionResponse;
import org.carnavawiky.back.model.Agrupacion;
import org.carnavawiky.back.model.Localidad;
import org.carnavawiky.back.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class AgrupacionMapper {

    public Agrupacion toEntity(AgrupacionRequest request, Usuario usuarioCreador, Localidad localidad) {
        Agrupacion agrupacion = new Agrupacion();
        agrupacion.setNombre(request.getNombre());
        agrupacion.setDescripcion(request.getDescripcion());
        agrupacion.setUsuarioCreador(usuarioCreador);
        agrupacion.setModalidad(request.getModalidad());
        agrupacion.setAnho(request.getAnho());
        agrupacion.setOficial(request.isOficial()); // << CRUCIAL: Para persistir el cambio
        agrupacion.setLocalidad(localidad);

        return agrupacion;
    }

    public AgrupacionResponse toResponse(Agrupacion entity) {
        AgrupacionResponse response = new AgrupacionResponse();
        response.setId(entity.getId());
        response.setNombre(entity.getNombre());
        response.setDescripcion(entity.getDescripcion());
        response.setFechaAlta(entity.getFechaAlta());
        response.setModalidad(entity.getModalidad());
        response.setAnho(entity.getAnho());
        response.setOficial(entity.isOficial());

        response.setNombreUsuarioCreador(entity.getUsuarioCreador().getUsername());

        if (entity.getLocalidad() != null) {
            response.setLocalidadId(entity.getLocalidad().getId());
            response.setLocalidadNombre(entity.getLocalidad().getNombre());
        }

        return response;
    }
}