package org.carnavawiky.back.dto;

import lombok.Data;
import org.carnavawiky.back.model.Modalidad;

@Data
public class PremioResponse {

    private Long id;
    private Integer puesto;
    private Modalidad modalidad;

    // Datos de Agrupación (Relación N:1)
    private Long agrupacionId;
    private String agrupacionNombre;

    // Datos de Edición (Relación N:1)
    private Long edicionId;
    private Integer edicionAnho;
    private String concursoNombre; // Incluimos el nombre del concurso para contexto
}