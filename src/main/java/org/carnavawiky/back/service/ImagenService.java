package org.carnavawiky.back.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.carnavawiky.back.dto.ImagenRequest;
import org.carnavawiky.back.dto.ImagenResponse;
import org.carnavawiky.back.exception.ResourceNotFoundException;
import org.carnavawiky.back.mapper.ImagenMapper;
import org.carnavawiky.back.model.Agrupacion;
import org.carnavawiky.back.model.Imagen;
import org.carnavawiky.back.repository.AgrupacionRepository;
import org.carnavawiky.back.repository.ImagenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ImagenService {

    @Autowired
    private ImagenRepository imagenRepository;

    @Autowired
    private AgrupacionRepository agrupacionRepository;

    @Autowired
    private ImagenMapper imagenMapper;

    // Inyectamos el Bean de Cloudinary que configuramos anteriormente
    @Autowired
    private Cloudinary cloudinary;

    // =======================================================
    // Helpers
    // =======================================================

    private Agrupacion findAgrupacion(Long id) {
        return agrupacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agrupacion", "id", id));
    }

    // =======================================================
    // 1. SUBIR Y GUARDAR IMAGEN (POST) - AHORA EN CLOUDINARY
    // =======================================================
    @Transactional
    public ImagenResponse guardarImagen(MultipartFile file, ImagenRequest request) throws IOException {

        // 1. Validar la Agrupación
        Agrupacion agrupacion = findAgrupacion(request.getAgrupacionId());

        // 2. Subir el archivo a Cloudinary
        // Especificamos una carpeta para mantener ordenado el bucket
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("folder", "carnavawiky/agrupaciones"));

        String publicUrl = uploadResult.get("secure_url").toString();
        String publicId = uploadResult.get("public_id").toString();

        // 3. Lógica de Portada: Si se marca como portada, desmarcar la anterior
        if (Boolean.TRUE.equals(request.getEsPortada())) {
            imagenRepository.desmarcarPortadaActual(agrupacion.getId());
        }

        // 4. Crear la entidad Imagen con los datos de Cloudinary
        Imagen imagen = new Imagen();
        imagen.setAgrupacion(agrupacion);
        imagen.setNombreFichero(publicId); // Guardamos el publicId de Cloudinary para futuras eliminaciones
        imagen.setUrlPublica(publicUrl);   // URL estable y definitiva (HTTPS)
        imagen.setEsPortada(request.getEsPortada());

        // La ruta absoluta ya no es necesaria para ficheros locales,
        // pero podemos guardar el publicId como referencia.
        imagen.setRutaAbsoluta("cloudinary://" + publicId);

        // 5. Guardar metadatos en la DB
        Imagen nuevaImagen = imagenRepository.save(imagen);

        return imagenMapper.toResponse(nuevaImagen);
    }

    // =======================================================
    // 2. OBTENER IMÁGENES POR AGRUPACIÓN (GET)
    // =======================================================
    @Transactional(readOnly = true)
    public List<ImagenResponse> obtenerImagenesPorAgrupacion(Long agrupacionId) {
        findAgrupacion(agrupacionId);
        List<Imagen> imagenes = imagenRepository.findByAgrupacion_Id(agrupacionId);

        return imagenes.stream()
                .map(imagenMapper::toResponse)
                .collect(Collectors.toList());
    }

    // =======================================================
    // 3. ELIMINAR IMAGEN (DELETE) - AHORA EN CLOUDINARY
    // =======================================================
    @Transactional
    public void eliminarImagen(Long id) throws IOException {
        Imagen imagen = imagenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen", "id", id));

        // 1. Eliminar el archivo de Cloudinary usando su public_id
        cloudinary.uploader().destroy(imagen.getNombreFichero(), ObjectUtils.emptyMap());

        // 2. Eliminar el metadato de la DB
        imagenRepository.delete(imagen);
    }

    // NOTA: El método cargarFicheroComoRecurso ya no es necesario
    // porque las imágenes se sirven directamente desde la URL de Cloudinary.
}