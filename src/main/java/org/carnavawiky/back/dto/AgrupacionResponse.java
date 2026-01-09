package org.carnavawiky.back.dto;

import lombok.Data;
import org.carnavawiky.back.model.Modalidad;
import java.time.LocalDateTime;

@Data
public class AgrupacionResponse {

    private Long id;
    private String nombre;
    private String descripcion;
    private LocalDateTime fechaAlta;
    private String nombreUsuarioCreador;
    private Integer anho;
    private Modalidad modalidad;
    private Long localidadId;
    private String localidadNombre;
}