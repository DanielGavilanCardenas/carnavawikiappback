package org.carnavawiky.back.service;

import org.carnavawiky.back.config.FileStorageProperties;
import org.carnavawiky.back.exception.FileNotFoundException;
import org.carnavawiky.back.exception.FileStorageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileStorageServiceTest {

    private FileStorageService fileStorageService;
    private FileStorageProperties properties;

    @TempDir
    Path tempDir; // JUnit 5 creará una carpeta temporal automática para los tests

    @BeforeEach
    void setUp() {
        properties = new FileStorageProperties();
        // Configuramos la propiedad para que use el directorio temporal
        properties.setLocation(tempDir.toString());
        fileStorageService = new FileStorageService(properties);
    }

    // =======================================================
    // 1. TEST DE ALMACENAMIENTO (storeFile)
    // =======================================================
    @Test
    @DisplayName("Debe almacenar un archivo correctamente y devolver un nombre único")
    void testStoreFile_Exito() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.jpg", "image/jpeg", "contenido".getBytes()
        );

        // ACT
        String storedName = fileStorageService.storeFile(file);

        // ASSERT
        assertNotNull(storedName);
        // Verificamos que el archivo existe físicamente usando el nombre devuelto
        Path filePath = tempDir.resolve(storedName);
        assertTrue(Files.exists(filePath), "El archivo debería existir en: " + filePath.toAbsolutePath());

        // Opcional: Verificar que el contenido es el mismo
        try {
            byte[] content = Files.readAllBytes(filePath);
            assertArrayEquals("contenido".getBytes(), content);
        } catch (IOException e) {
            fail("No se pudo leer el archivo guardado");
        }
    }

    @Test
    @DisplayName("Debe lanzar excepción si el nombre del archivo contiene caracteres inválidos")
    void testStoreFile_NombreInvalido() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "../script.sh", "text/plain", "data".getBytes()
        );

        assertThrows(FileStorageException.class, () -> fileStorageService.storeFile(file));
    }

    // =======================================================
    // 2. TEST DE CARGA (loadFileAsResource)
    // =======================================================
    @Test
    @DisplayName("Debe cargar un archivo existente como recurso")
    void testLoadFileAsResource_Exito() throws IOException {
        // Primero guardamos uno manualmente en el tempDir
        String fileName = "test-file.txt";
        Files.writeString(tempDir.resolve(fileName), "Hello World");

        Resource resource = fileStorageService.loadFileAsResource(fileName);

        assertTrue(resource.exists());
        assertEquals(fileName, resource.getFilename());
    }

    @Test
    @DisplayName("Debe lanzar FileNotFoundException si el archivo no existe")
    void testLoadFileAsResource_NoExistente() {
        assertThrows(FileNotFoundException.class, () ->
                fileStorageService.loadFileAsResource("fantasma.png"));
    }

    // =======================================================
    // 3. TEST DE ELIMINACIÓN (deleteFile)
    // =======================================================
    @Test
    @DisplayName("Debe eliminar un archivo físico correctamente")
    void testDeleteFile_Exito() throws IOException {
        String fileName = "todelete.jpg";
        Files.createFile(tempDir.resolve(fileName));

        boolean deleted = fileStorageService.deleteFile(fileName);

        assertTrue(deleted);
        assertFalse(Files.exists(tempDir.resolve(fileName)));
    }

    @Test
    @DisplayName("Debe devolver false si se intenta borrar un archivo que no existe")
    void testDeleteFile_NoExistente() {
        boolean deleted = fileStorageService.deleteFile("no-existe.jpg");
        assertFalse(deleted);
    }

    // =======================================================
    // 4. TEST DE UBICACIÓN (getStorageLocation)
    // =======================================================
    @Test
    @DisplayName("Debe devolver la ruta de almacenamiento correcta")
    void testGetStorageLocation() {
        Path location = fileStorageService.getStorageLocation();
        assertEquals(tempDir.toAbsolutePath().normalize(), location);
    }
}