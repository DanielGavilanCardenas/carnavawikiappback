package org.carnavawiky.back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.carnavawiky.back.dto.ImagenRequest;
import org.carnavawiky.back.dto.ImagenResponse;
import org.carnavawiky.back.service.ImagenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequestMapping("/api/imagenes")
public class ImagenController {

    @Autowired
    private ImagenService imagenService;

    // Utilidad para mapear la parte JSON del request que viene en formato multipart
    @Autowired
    private ObjectMapper objectMapper;

    // =======================================================
    // 1. SUBIR IMAGEN (POST) - Solo ADMIN
    // Recibe Multipart/form-data: archivo y metadatos JSON
    // =======================================================
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ImagenResponse> uploadImagen(
            @RequestParam("file") MultipartFile file,
            @RequestParam("data") String requestJson) throws IOException {

        // Mapear el JSON de metadatos al DTO
        ImagenRequest request = objectMapper.readValue(requestJson, ImagenRequest.class);

        // La validación del DTO se hace manualmente o con un filtro,
        // pero se asume que los datos mínimos (agrupacionId) están presentes.

        ImagenResponse nuevaImagen = imagenService.guardarImagen(file, request);
        return new ResponseEntity<>(nuevaImagen, HttpStatus.CREATED);
    }

    // =======================================================
    // 2. LISTAR IMÁGENES POR AGRUPACIÓN (GET) - Público
    // =======================================================
    @GetMapping("/agrupacion/{agrupacionId}")
    public ResponseEntity<List<ImagenResponse>> obtenerImagenesPorAgrupacion(@PathVariable Long agrupacionId) {
        List<ImagenResponse> imagenes = imagenService.obtenerImagenesPorAgrupacion(agrupacionId);
        return ResponseEntity.ok(imagenes);
    }

    // =======================================================
    // 3. SERVIR FICHERO ESTÁTICO (GET /ficheros/{fileName}) - Público
    // NOTA: Esta ruta debe coincidir con la configuración de la URL pública en el Service.
    // =======================================================
    @GetMapping("/ficheros/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws MalformedURLException {

        Resource resource = imagenService.cargarFicheroComoRecurso(fileName);

        // Intentar determinar el tipo de contenido (MIME type)
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            // Se ignora y se usa un tipo por defecto
        }

        // Si el tipo de contenido no se puede determinar, usar el tipo por defecto
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    // =======================================================
    // 4. ELIMINAR IMAGEN (DELETE) - Solo ADMIN
    // =======================================================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarImagen(@PathVariable Long id) throws IOException {
        imagenService.eliminarImagen(id);
        return ResponseEntity.noContent().build();
    }
}