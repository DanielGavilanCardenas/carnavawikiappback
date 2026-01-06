package org.carnavawiky.back.service;

import org.carnavawiky.back.dto.ImagenResponse;
import org.carnavawiky.back.exception.ResourceNotFoundException;
import org.carnavawiky.back.mapper.ImagenMapper;
import org.carnavawiky.back.model.Agrupacion;
import org.carnavawiky.back.model.Imagen;
import org.carnavawiky.back.repository.AgrupacionRepository;
import org.carnavawiky.back.repository.ImagenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ImagenService {

    @Autowired
    private ImagenRepository imagenRepository;

    @Autowired
    private AgrupacionRepository agrupacionRepository;

    @Autowired
    private ImagenMapper imagenMapper;

    @Value("${file.upload.location}")
    private String uploadLocation;

    @Transactional
    public ImagenResponse subirImagen(Long agrupacionId, MultipartFile archivo, Boolean esPortada) throws IOException {
        Agrupacion agrupacion = agrupacionRepository.findById(agrupacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Agrupacion", "id", agrupacionId));

        // 1. Crear directorio si no existe
        Path root = Paths.get(uploadLocation);
        if (!Files.exists(root)) {
            Files.createDirectories(root);
        }

        // 2. Gestionar la portada (si es nueva portada, desmarcamos la anterior)
        if (Boolean.TRUE.equals(esPortada)) {
            imagenRepository.desmarcarPortadaActual(agrupacionId);
        }

        // 3. Generar nombre único y guardar archivo
        String extension = "";
        String originalName = archivo.getOriginalFilename();
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        }
        String nombreFichero = UUID.randomUUID().toString() + extension;
        Path destino = root.resolve(nombreFichero);
        Files.copy(archivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

        // 4. Guardar en Base de Datos
        Imagen imagen = new Imagen();
        imagen.setNombreFichero(nombreFichero);
        imagen.setRutaAbsoluta(destino.toString());
        imagen.setUrlPublica("/api/imagenes/" + nombreFichero);
        imagen.setEsPortada(esPortada);
        imagen.setAgrupacion(agrupacion);

        Imagen guardada = imagenRepository.save(imagen);
        return imagenMapper.toResponse(guardada);
    }

    @Transactional(readOnly = true)
    public List<ImagenResponse> obtenerImagenesPorAgrupacion(Long agrupacionId) {
        List<Imagen> imagenes = imagenRepository.findByAgrupacion_Id(agrupacionId);
        return imagenes.stream().map(imagenMapper::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public void eliminarImagen(Long id) throws IOException {
        Imagen imagen = imagenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen", "id", id));

        // Borrado físico
        Path rutaFichero = Paths.get(imagen.getRutaAbsoluta());
        Files.deleteIfExists(rutaFichero);

        // Borrado lógico (BD)
        imagenRepository.delete(imagen);
    }
}