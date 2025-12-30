package org.carnavawiky.back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// DTO de salida: Lo que devolvemos al cliente
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoResponse {
    private Long id;
    private String titulo;
    private String urlYoutube;
    private boolean verificado;
    private Long agrupacionId;
    private String agrupacionNombre; // Opcional, para facilitar el frontal
}