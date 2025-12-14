package org.carnavawiky.back.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PersonaRequest {

    @NotBlank(message = "El nombre real es obligatorio.")
    @Size(max = 100, message = "El nombre real no puede exceder los 100 caracteres.")
    private String nombreReal;

    @Size(max = 100, message = "El apodo no puede exceder los 100 caracteres.")
    private String apodo;

    @NotNull(message = "El ID de la Localidad de origen es obligatorio.")
    private Long localidadId;

    // Opcional: ID del usuario registrado asociado
    private Long usuarioId;
}