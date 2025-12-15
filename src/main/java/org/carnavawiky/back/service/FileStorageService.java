package org.carnavawiky.back.service;

import org.carnavawiky.back.config.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) throws IOException {
        // Inicializa la ruta base de almacenamiento
        this.fileStorageLocation = Paths.get(fileStorageProperties.getLocation())
                .toAbsolutePath().normalize();

        // Crea el directorio si no existe
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new IOException("No se pudo crear el directorio de almacenamiento de archivos.");
        }
    }

    /**
     * Agregado: Getter para obtener la ubicación base de almacenamiento.
     * @return Path de la ubicación de almacenamiento.
     */
    public Path getStorageLocation() {
        return this.fileStorageLocation;
    }

    /**
     * Guarda el archivo en el disco y devuelve el nombre de fichero generado.
     * @param file Archivo subido (MultipartFile)
     * @return El nombre único del fichero guardado.
     * @throws IOException Si ocurre un error de E/S.
     */
    public String storeFile(MultipartFile file) throws IOException {
        // Normalizar el nombre del archivo y añadir UUID para evitar colisiones
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String extension = "";
        int lastDot = originalFilename.lastIndexOf('.');
        if (lastDot > 0) {
            extension = originalFilename.substring(lastDot);
        }

        // Generar un nombre de fichero único y seguro
        String fileName = UUID.randomUUID().toString() + extension;
        Path targetLocation = this.fileStorageLocation.resolve(fileName);

        // Copiar el archivo al directorio de destino
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    /**
     * Carga el archivo como un recurso para ser servido.
     * @param fileName Nombre del fichero (UUID)
     * @return Recurso cargado
     * @throws MalformedURLException Si la ruta no es una URL válida
     */
    public Resource loadFileAsResource(String fileName) throws MalformedURLException {
        Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists()) {
            return resource;
        } else {
            // Manejo de error si el archivo no existe
            throw new MalformedURLException("Archivo no encontrado: " + fileName);
        }
    }

    /**
     * Elimina un archivo físico del disco.
     * @param fileName Nombre del fichero a eliminar.
     * @return true si se eliminó con éxito.
     * @throws IOException Si ocurre un error de E/S.
     */
    public boolean deleteFile(String fileName) throws IOException {
        Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
        return Files.deleteIfExists(filePath);
    }
}