package org.carnavawiky.back.dto;

import java.util.List;

public record AgrupacionFullResponse(
        Long id,
        String nombre,
        Integer anho,
        boolean oficial,
        String modalidad,
        String localidad,
        List<ImagenResponse> imagenes,
        List<ComponenteDetalleDto> componentes,
        List<VideoResponse> videos
) {}