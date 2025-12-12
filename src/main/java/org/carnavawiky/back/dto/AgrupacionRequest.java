package org.carnavawiky.back.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.carnavawiky.back.model.Modalidad;

@Data
public class AgrupacionRequest {

    @NotBlank(message = "El nombre de la agrupación es obligatorio.")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres.")
    private String nombre;

    @Size(max = 1000, message = "La descripción no puede exceder los 1000 caracteres.")
    private String descripcion;

    @NotNull(message = "La modalidad no puede ser nula.") // Validamos que se envíe el campo
    private Modalidad modalidad; // El JSON debe enviar "CHIRIGOTA", "COMPARSA", etc.

    // NOTA: No incluimos el ID ni el usuario creador, ya que se gestionan en el backend.
}