package org.carnavawiky.back.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ComentarioRequest {

    @NotBlank(message = "El contenido del comentario es obligatorio.")
    @Size(max = 1000, message = "El comentario no puede exceder los 1000 caracteres.")
    private String contenido;

    // Puntuación opcional, pero si existe, debe estar entre 1 y 5
    @Min(value = 1, message = "La puntuación mínima es 1.")
    @Max(value = 5, message = "La puntuación máxima es 5.")
    private Integer puntuacion; // Puede ser nulo

    // Necesario para vincular el comentario
    @NotNull(message = "El ID de la Agrupación es obligatorio.")
    private Long agrupacionId;

    // NOTA: El usuarioId se obtiene normalmente del contexto de seguridad,
    // pero lo incluiremos en el Request para simplificar la prueba del servicio/mapper.
    @NotNull(message = "El ID del Usuario es obligatorio.")
    private Long usuarioId;
}