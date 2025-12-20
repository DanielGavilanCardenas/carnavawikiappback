package org.carnavawiky.back.dto;

import lombok.Data;

@Data
public class ImagenResponse {

    private Long id;
    private String nombreFichero;
    private String urlPublica; // La URL que el frontend usará para mostrar la imagen
    private Boolean esPortada;

    // ID de la Agrupación a la que pertenece
    private Long agrupacionId;
    private String agrupacionNombre;
}