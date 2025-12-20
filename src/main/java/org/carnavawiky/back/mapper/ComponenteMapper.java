package org.carnavawiky.back.mapper;

import org.carnavawiky.back.dto.ComponenteRequest;
import org.carnavawiky.back.dto.ComponenteResponse;
import org.carnavawiky.back.model.Agrupacion;
import org.carnavawiky.back.model.Componente;
import org.carnavawiky.back.model.Persona;
import org.springframework.stereotype.Component;

@Component
public class ComponenteMapper {

    /**
     * Convierte un ComponenteRequest a una entidad Componente.
     */
    public Componente toEntity(ComponenteRequest request, Persona persona, Agrupacion agrupacion) {
        Componente componente = new Componente();
        componente.setRol(request.getRol());
        componente.setPersona(persona);
        componente.setAgrupacion(agrupacion);
        return componente;
    }

    /**
     * Convierte una entidad Componente a un ComponenteResponse.
     */
    public ComponenteResponse toResponse(Componente entity) {
        ComponenteResponse response = new ComponenteResponse();
        response.setId(entity.getId());
        response.setRol(entity.getRol());

        // Mapeo de Persona
        if (entity.getPersona() != null) {
            response.setPersonaId(entity.getPersona().getId());
            response.setNombreReal(entity.getPersona().getNombreReal());
            response.setNombreArtistico(entity.getPersona().getApodo());
        }

        // Mapeo de Agrupación
        if (entity.getAgrupacion() != null) {
            response.setAgrupacionId(entity.getAgrupacion().getId());
            response.setAgrupacionNombre(entity.getAgrupacion().getNombre());
        }

        return response;
    }
}