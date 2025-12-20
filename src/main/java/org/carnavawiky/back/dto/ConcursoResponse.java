package org.carnavawiky.back.dto;

import lombok.Data;

@Data
public class ConcursoResponse {

    private Long id;
    private String nombre;
    private Boolean estaActivo;

    // Datos de Localidad (Necesarios para la respuesta)
    private Long localidadId;
    private String localidadNombre;
}