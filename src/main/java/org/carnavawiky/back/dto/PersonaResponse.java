package org.carnavawiky.back.dto;

import lombok.Data;

@Data
public class PersonaResponse {

    private Long id;
    private String nombreReal;
    private String apodo;

    // Datos de Localidad (Origen)
    private Long localidadId;
    private String localidadNombre;

    // Datos de Usuario (Asociado)
    private Long usuarioId;
    private String usuarioUsername;
}