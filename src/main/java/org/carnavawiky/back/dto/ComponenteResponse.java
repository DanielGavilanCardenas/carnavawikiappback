package org.carnavawiky.back.dto;

import lombok.Data;
import org.carnavawiky.back.model.RolComponente;

@Data
public class ComponenteResponse {

    private Long id;
    private RolComponente rol;

    // Datos de Persona (Identidad)
    private Long personaId;
    private String nombreArtistico; // Apodo de la persona
    private String nombreReal;

    // Datos de Agrupación
    private Long agrupacionId;
    private String agrupacionNombre;
}