package org.carnavawiky.back.mapper;

import org.carnavawiky.back.dto.PersonaRequest;
import org.carnavawiky.back.dto.PersonaResponse;
import org.carnavawiky.back.model.Localidad;
import org.carnavawiky.back.model.Persona;
import org.carnavawiky.back.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class PersonaMapper {

    /**
     * Convierte un PersonaRequest a una entidad Persona, incluyendo sus relaciones.
     */
    public Persona toEntity(PersonaRequest request, Localidad localidad, Usuario usuario) {
        Persona persona = new Persona();
        persona.setNombreReal(request.getNombreReal());
        persona.setApodo(request.getApodo());
        persona.setOrigen(localidad);
        persona.setUsuario(usuario); // Puede ser null
        return persona;
    }

    /**
     * Convierte una entidad Persona a un PersonaResponse.
     */
    public PersonaResponse toResponse(Persona entity) {
        PersonaResponse response = new PersonaResponse();
        response.setId(entity.getId());
        response.setNombreReal(entity.getNombreReal());
        response.setApodo(entity.getApodo());

        // Mapeo de Localidad (Origen)
        if (entity.getOrigen() != null) {
            response.setLocalidadId(entity.getOrigen().getId());
            response.setLocalidadNombre(entity.getOrigen().getNombre());
        }

        // Mapeo de Usuario (Asociado)
        if (entity.getUsuario() != null) {
            response.setUsuarioId(entity.getUsuario().getId());
            response.setUsuarioUsername(entity.getUsuario().getUsername());
        }

        return response;
    }
}