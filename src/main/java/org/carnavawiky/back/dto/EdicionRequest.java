package org.carnavawiky.back.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EdicionRequest {

    // Validamos el año (ejemplo: entre 1900 y el año actual)
    @NotNull(message = "El año de la edición es obligatorio.")
    @Min(value = 1900, message = "El año debe ser posterior a 1900.")
    @Max(value = 2100, message = "El año no puede exceder el 2100.")
    private Integer anho;

    @NotNull(message = "El ID del Concurso es obligatorio.")
    private Long concursoId;
}