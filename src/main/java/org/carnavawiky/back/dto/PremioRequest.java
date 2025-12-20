package org.carnavawiky.back.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.carnavawiky.back.model.Modalidad;

@Data
public class PremioRequest {

    @NotNull(message = "El puesto obtenido es obligatorio.")
    @Min(value = 1, message = "El puesto debe ser un valor positivo.")
    private Integer puesto;

    @NotNull(message = "La modalidad es obligatoria.")
    private Modalidad modalidad;

    @NotNull(message = "El ID de la Agrupación es obligatorio.")
    private Long agrupacionId;

    @NotNull(message = "El ID de la Edición es obligatorio.")
    private Long edicionId;
}