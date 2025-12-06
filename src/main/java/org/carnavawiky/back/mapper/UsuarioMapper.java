package org.carnavawiky.back.mapper;

import org.carnavawiky.back.dto.UsuarioResponse;
import org.carnavawiky.back.model.Usuario;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class UsuarioMapper {

    /**
     * Convierte una entidad Usuario a un UsuarioResponse (excluyendo la contraseña y tokens sensibles).
     * * @param usuario La entidad Usuario persistida.
     * @return El DTO de respuesta para el cliente.
     */
    public UsuarioResponse toResponse(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        UsuarioResponse response = new UsuarioResponse();

        response.setId(usuario.getId());
        response.setUsername(usuario.getUsername());
        response.setEmail(usuario.getEmail());
        response.setEnabled(usuario.isEnabled());
        response.setFechaAlta(usuario.getFechaAlta());

        // Mapeamos el Set de objetos Role a un Set de nombres de rol (Strings)
        response.setRoles(usuario.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet()));

        return response;
    }

    // Nota: El mapeo de Request a Entidad no se hace aquí directamente
    // porque es complejo (implica cifrar la contraseña y buscar roles),
    // por lo que esa lógica (cifrado y búsqueda) se mantiene en el Servicio.
}