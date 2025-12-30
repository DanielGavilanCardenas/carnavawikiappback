package org.carnavawiky.back.controller;

import jakarta.validation.Valid;
import org.carnavawiky.back.dto.VideoRequest;
import org.carnavawiky.back.dto.VideoResponse;
import org.carnavawiky.back.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    /**
     * Endpoint público para obtener solo los vídeos que ya han sido
     * verificados por un especialista.
     */
    @GetMapping("/public")
    public List<VideoResponse> getPublicVideos() {
        return videoService.listarVerificados();
    }

    /**
     * Crea un nuevo vídeo. Por defecto, el campo 'verificado' será false.
     * La anotación @Valid asegura que el título y la URL cumplan las reglas del DTO.
     */
    @PostMapping("/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public VideoResponse createVideo(@Valid @RequestBody VideoRequest request) {
        return videoService.guardar(request);
    }

    /**
     * Endpoint para que un especialista marque un vídeo como verificado.
     */
    @PutMapping("/admin/verificar/{id}")
    public VideoResponse verifyVideo(@PathVariable Long id) {
        return videoService.verificarVideo(id);
    }

    /**
     * Elimina un vídeo del sistema por su ID.
     */
    @DeleteMapping("/admin/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVideo(@PathVariable Long id) {
        videoService.eliminar(id);
    }
}