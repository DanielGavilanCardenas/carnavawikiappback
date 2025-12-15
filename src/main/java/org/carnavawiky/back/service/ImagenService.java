package org.carnavawiky.back.service;

import org.carnavawiky.back.dto.ImagenRequest;
import org.carnavawiky.back.dto.ImagenResponse;
import org.carnavawiky.back.exception.ResourceNotFoundException;
import org.carnavawiky.back.mapper.ImagenMapper;
import org.carnavawiky.back.model.Agrupacion;
import org.carnavawiky.back.model.Imagen;
import org.carnavawiky.back.repository.AgrupacionRepository;
import org.carnavawiky.back.repository.ImagenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImagenService {

    @Autowired
    private ImagenRepository imagenRepository;

    @Autowired
    private AgrupacionRepository agrupacionRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ImagenMapper imagenMapper;

    // Obtener el puerto de la aplicación para construir la URL pública
    @Value("${server.port:8083}")
    private String serverPort;

    // =======================================================
    // Helpers
    // =======================================================

    private Agrupacion findAgrupacion(Long id) {
        return agrupacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agrupacion", "id", id));
    }

    private String buildPublicUrl(String fileName) {
        // Asumiendo que el controlador mapea la ruta de acceso estático como /api/imagenes/ficheros/
        return "http://localhost:" + serverPort + "/api/imagenes/ficheros/" + fileName;
    }

    // =======================================================
    // 1. SUBIR Y GUARDAR IMAGEN (POST)
    // =======================================================
    @Transactional
    public ImagenResponse guardarImagen(MultipartFile file, ImagenRequest request) throws IOException {

        // 1. Validar la Agrupación
        Agrupacion agrupacion = findAgrupacion(request.getAgrupacionId());

        // 2. Guardar el archivo en el sistema de ficheros
        String fileName = fileStorageService.storeFile(file);

        // 3. Lógica de Portada: Si se marca como portada, desmarcar la anterior
        if (request.getEsPortada()) {
            imagenRepository.desmarcarPortadaActual(agrupacion.getId());
        }

        // 4. Crear la entidad Imagen
        Imagen imagen = new Imagen();
        imagen.setAgrupacion(agrupacion);
        imagen.setNombreFichero(fileName);
        // CORRECCIÓN APLICADA: Usamos getStorageLocation() del servicio corregido
        imagen.setRutaAbsoluta(fileStorageService.getStorageLocation().resolve(fileName).toString());
        imagen.setUrlPublica(buildPublicUrl(fileName));
        imagen.setEsPortada(request.getEsPortada());

        // 5. Guardar metadatos en la DB
        Imagen nuevaImagen = imagenRepository.save(imagen);

        return imagenMapper.toResponse(nuevaImagen);
    }

    // =======================================================
    // 2. OBTENER IMÁGENES POR AGRUPACIÓN (GET)
    // =======================================================
    @Transactional(readOnly = true)
    public List<ImagenResponse> obtenerImagenesPorAgrupacion(Long agrupacionId) {
        // Solo verificamos la existencia de la agrupación, no es estrictamente necesario
        // pero asegura que el ID es válido.
        findAgrupacion(agrupacionId);

        List<Imagen> imagenes = imagenRepository.findByAgrupacion_Id(agrupacionId);

        return imagenes.stream()
                .map(imagenMapper::toResponse)
                .collect(Collectors.toList());
    }

    // =======================================================
    // 3. OBTENER IMAGEN POR NOMBRE DE FICHERO (PARA SERVIR)
    // =======================================================
    public Resource cargarFicheroComoRecurso(String fileName) throws MalformedURLException {
        // Llama al servicio de almacenamiento para obtener el recurso
        return fileStorageService.loadFileAsResource(fileName);
    }

    // =======================================================
    // 4. ELIMINAR IMAGEN (DELETE)
    // =======================================================
    @Transactional
    public void eliminarImagen(Long id) throws IOException {
        Imagen imagen = imagenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen", "id", id));

        // 1. Eliminar el archivo físico
        fileStorageService.deleteFile(imagen.getNombreFichero());

        // 2. Eliminar el metadato de la DB
        imagenRepository.delete(imagen);

        // 3. Lógica de portada: Si se elimina la portada, se debe seleccionar una nueva por defecto
        // (por simplicidad, este paso se puede omitir o manejar con lógica avanzada,
        // por ahora, simplemente se elimina la referencia).
    }
}