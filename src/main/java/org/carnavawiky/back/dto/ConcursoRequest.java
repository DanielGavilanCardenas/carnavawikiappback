package org.carnavawiky.back.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ConcursoRequest {

    @NotBlank(message = "El nombre del concurso es obligatorio.")
    @Size(max = 200, message = "El nombre no puede exceder los 200 caracteres.")
    private String nombre;

    @NotNull(message = "El estado de actividad es obligatorio.")
    private Boolean estaActivo;

    @NotNull(message = "El ID de la Localidad es obligatorio.")
    private Long localidadId;
}