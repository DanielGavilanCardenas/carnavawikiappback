package org.carnavawiky.back.dto;

import lombok.Data;

@Data
public class EdicionResponse {

    private Long id;
    private Integer anho;

    // Datos del Concurso (Relación N:1)
    private Long concursoId;
    private String concursoNombre;
}