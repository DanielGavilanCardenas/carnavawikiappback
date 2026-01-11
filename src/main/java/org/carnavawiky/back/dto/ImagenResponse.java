package org.carnavawiky.back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImagenResponse {
    private Long id;
    private String nombreFichero;
    private String urlPublica;
    private Boolean esPortada;
}