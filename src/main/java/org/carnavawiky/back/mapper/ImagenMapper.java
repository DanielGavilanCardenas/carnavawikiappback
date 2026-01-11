package org.carnavawiky.back.mapper;

import org.carnavawiky.back.dto.ImagenResponse;
import org.carnavawiky.back.model.Imagen;
import org.springframework.stereotype.Component;

@Component
public class ImagenMapper {

    // NOTA: No necesitamos un toEntity(Request) ya que la entidad se construye
    // con datos del Request DTO Y datos del archivo subido (MultipartFile).

    /**
     * Convierte una entidad Imagen a un ImagenResponse.
     */
    public ImagenResponse toResponse(Imagen entity) {
        ImagenResponse response = new ImagenResponse();
        response.setId(entity.getId());
        response.setNombreFichero(entity.getNombreFichero());
        response.setUrlPublica(entity.getUrlPublica());
        response.setEsPortada(entity.getEsPortada() != null && entity.getEsPortada());

        return response;
    }
}