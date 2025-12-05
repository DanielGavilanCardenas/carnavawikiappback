package org.carnavawiky.back.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AgrupacionResponse {

    private Long id;
    private String nombre;
    private String descripcion;
    private LocalDateTime fechaAlta;
    private String nombreUsuarioCreador; // Mostramos solo el nombre del creador
}