package org.carnavawiky.back.mapper;

import org.carnavawiky.back.dto.AgrupacionRequest;
import org.carnavawiky.back.dto.AgrupacionResponse;
import org.carnavawiky.back.model.Agrupacion;
import org.carnavawiky.back.model.Localidad; // << NUEVA IMPORTACIÓN
import org.carnavawiky.back.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class AgrupacionMapper {

    /**
     * Convierte un AgrupacionRequest a una entidad Agrupacion.
     * @param request El DTO de entrada.
     * @param usuarioCreador El Usuario que está creando la agrupación.
     * @param localidad La entidad Localidad ya cargada desde la BBDD. // << NUEVO PARAMETRO
     * @return La entidad Agrupacion lista para ser persistida.
     */
    public Agrupacion toEntity(AgrupacionRequest request, Usuario usuarioCreador, Localidad localidad) { // << FIRMA MODIFICADA
        Agrupacion agrupacion = new Agrupacion();
        agrupacion.setNombre(request.getNombre());
        agrupacion.setDescripcion(request.getDescripcion());
        agrupacion.setUsuarioCreador(usuarioCreador); // Vinculamos el creador
        agrupacion.setModalidad(request.getModalidad());

        // =======================================================
        // AÑADIDO: VINCULACIÓN DE LOCALIDAD
        // =======================================================
        agrupacion.setLocalidad(localidad);

        return agrupacion;
    }

    /**
     * Convierte una entidad Agrupacion a un AgrupacionResponse.
     * @param entity La entidad persistida.
     * @return El DTO de respuesta para el cliente.
     */
    public AgrupacionResponse toResponse(Agrupacion entity) {
        AgrupacionResponse response = new AgrupacionResponse();
        response.setId(entity.getId());
        response.setNombre(entity.getNombre());
        response.setDescripcion(entity.getDescripcion());
        response.setFechaAlta(entity.getFechaAlta());
        response.setModalidad(entity.getModalidad());

        // Mapeamos el nombre del usuario creador para la respuesta
        response.setNombreUsuarioCreador(entity.getUsuarioCreador().getUsername());

        // =======================================================
        // AÑADIDO: DATOS DE LOCALIDAD
        // =======================================================
        if (entity.getLocalidad() != null) {
            response.setLocalidadId(entity.getLocalidad().getId());
            response.setLocalidadNombre(entity.getLocalidad().getNombre());
        }

        return response;
    }
}