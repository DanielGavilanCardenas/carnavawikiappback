package org.carnavawiky.back.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.carnavawiky.back.model.RolComponente;

@Data
public class ComponenteRequest {

    @NotNull(message = "El rol del componente es obligatorio.")
    private RolComponente rol;

    @NotNull(message = "El ID de la Persona es obligatorio.")
    private Long personaId;

    @NotNull(message = "El ID de la Agrupación es obligatorio.")
    private Long agrupacionId;
}