package org.carnavawiky.back.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ComentarioResponse {

    private Long id;
    private String contenido;
    private Integer puntuacion;
    private Boolean aprobado; // << AÑADIDO
    private LocalDateTime fechaCreacion;

    // Datos de Usuario (Autor)
    private Long usuarioId;
    private String usuarioUsername;

    // Datos de Agrupación
    private Long agrupacionId;
    private String agrupacionNombre;
}