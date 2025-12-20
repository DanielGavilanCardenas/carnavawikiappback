package org.carnavawiky.back.service;

import org.carnavawiky.back.config.FileStorageProperties;
import org.carnavawiky.back.exception.FileStorageException; // Importar nueva excepción
import org.carnavawiky.back.exception.FileNotFoundException; // Importar nueva excepción
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getLocation())
                .toAbsolutePath().normalize();

        // Crea el directorio si no existe
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            // Se lanza la nueva excepción personalizada
            throw new FileStorageException("No se pudo crear el directorio de almacenamiento de archivos. Verifique la configuración de 'file.upload.location' y los permisos del servidor.", ex);
        }
    }

    public Path getStorageLocation() {
        return this.fileStorageLocation;
    }

    /**
     * Guarda el archivo en el disco y devuelve el nombre de fichero generado.
     * @param file Archivo subido (MultipartFile)
     * @return El nombre único del fichero guardado.
     */
    public String storeFile(MultipartFile file) {
        // 1. Limpiar y normalizar el nombre del archivo
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            // 2. VALIDACIÓN DE SEGURIDAD (Esto es lo que hace que falle el segundo test si falta)
            if (fileName.contains("..")) {
                throw new FileStorageException("El nombre del archivo contiene una ruta relativa inválida: " + fileName);
            }

            // 3. Generar nombre único (Para evitar colisiones)
            String extension = "";
            int i = fileName.lastIndexOf('.');
            if (i > 0) { extension = fileName.substring(i); }
            String storedName = UUID.randomUUID().toString() + extension;

            // 4. Resolver ruta y copiar
            Path targetLocation = this.fileStorageLocation.resolve(storedName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return storedName;
        } catch (IOException ex) {
            throw new FileStorageException("No se pudo almacenar el archivo " + fileName, ex);
        }
    }

    /**
     * Carga el archivo como un recurso para ser servido.
     * @param fileName Nombre del fichero (UUID)
     * @return Recurso cargado
     */
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                // Se lanza la nueva excepción personalizada para archivo no encontrado
                throw new FileNotFoundException("El archivo no pudo ser encontrado en el servidor: " + fileName);
            }
        } catch (Exception ex) {
            // Captura MalformedURLException (ahora manejada como FileNotFoundException)
            if (ex instanceof FileNotFoundException) {
                throw (FileNotFoundException) ex;
            }
            throw new FileNotFoundException("El archivo no pudo ser cargado o la ruta no es válida: " + fileName, ex);
        }
    }

    /**
     * Elimina un archivo físico del disco.
     * @param fileName Nombre del fichero a eliminar.
     * @return true si se eliminó con éxito.
     */
    public boolean deleteFile(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            return Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            // Si hay un fallo de I/O al intentar eliminar (falla del sistema), se lanza FileStorageException
            throw new FileStorageException("Error al intentar eliminar el archivo físico: " + fileName, ex);
        }
    }
}