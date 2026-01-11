package org.carnavawiky.back.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.carnavawiky.back.model.Modalidad;

@Data
public class AgrupacionRequest {

    @NotBlank(message = "El nombre de la agrupación es obligatorio.")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres.")
    private String nombre;

    // Validamos el año (ejemplo: entre 1900 y el año actual)
    @NotNull(message = "El año de la edición es obligatorio.")
    @Min(value = 1900, message = "El año debe ser posterior a 1900.")
    @Max(value = 2100, message = "El año no puede exceder el 2100.")
    private Integer anho;

    @Size(max = 1000, message = "La descripción no puede exceder los 1000 caracteres.")
    private String descripcion;

    @NotNull(message = "La modalidad no puede ser nula.") // Validamos que se envíe el campo
    private Modalidad modalidad; // El JSON debe enviar "CHIRIGOTA", "COMPARSA", etc.

    private boolean oficial;

    @NotNull(message = "El ID de la Localidad es obligatorio.")
    private Long localidadId;

    // NOTA: No incluimos el ID ni el usuario creador, ya que se gestionan en el backend.
}