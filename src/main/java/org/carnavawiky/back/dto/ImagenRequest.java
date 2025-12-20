package org.carnavawiky.back.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ImagenRequest {

    @NotNull(message = "El ID de la Agrupación es obligatorio.")
    private Long agrupacionId;

    // Indica si la imagen subida debe ser marcada como portada
    @NotNull(message = "El indicador 'esPortada' es obligatorio.")
    private Boolean esPortada;

    // NOTA: El archivo (MultipartFile) se recibe directamente en el Controller.
}