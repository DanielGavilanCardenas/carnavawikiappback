package org.carnavawiky.back.controller;

import org.carnavawiky.back.dto.ImagenRequest;
import org.carnavawiky.back.dto.ImagenResponse;
import org.carnavawiky.back.service.ImagenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/imagenes")
public class ImagenController {

    @Autowired
    private ImagenService imagenService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ImagenResponse subirImagen(
            @RequestParam("file") MultipartFile file,
            @ModelAttribute ImagenRequest request) throws IOException {
        return imagenService.guardarImagen(file, request);
    }

    @GetMapping("/agrupacion/{agrupacionId}")
    public List<ImagenResponse> obtenerPorAgrupacion(@PathVariable Long agrupacionId) {
        return imagenService.obtenerImagenesPorAgrupacion(agrupacionId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) throws IOException {
        imagenService.eliminarImagen(id);
    }

}