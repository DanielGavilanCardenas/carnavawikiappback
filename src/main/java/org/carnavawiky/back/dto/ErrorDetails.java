package org.carnavawiky.back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails {

    // Marca de tiempo del momento en que ocurrió el error
    private LocalDateTime timestamp;

    // Mensaje descriptivo del error (ej. "Recurso no encontrado")
    private String message;

    // Detalles adicionales, como la ruta de la petición que falló
    private String details;

    // Código de error HTTP (ej. 404, 400, 409)
    private int status;
}