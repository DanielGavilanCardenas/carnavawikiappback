package org.carnavawiky.back.service;

import org.carnavawiky.back.dto.ImagenResponse;
import org.carnavawiky.back.mapper.ImagenMapper;
import org.carnavawiky.back.model.Agrupacion;
import org.carnavawiky.back.model.Imagen;
import org.carnavawiky.back.repository.AgrupacionRepository;
import org.carnavawiky.back.repository.ImagenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImagenServiceTest {

    @Mock
    private ImagenRepository imagenRepository;

    @Mock
    private AgrupacionRepository agrupacionRepository;

    @Mock
    private ImagenMapper imagenMapper;

    @InjectMocks
    private ImagenService imagenService;

    private Agrupacion agrupacion;
    private Imagen imagen;
    private ImagenResponse imagenResponse;
    private MockMultipartFile mockFile;
    private Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        // Configurar directorio temporal para simular subida de archivos
        tempDir = Files.createTempDirectory("uploads");
        ReflectionTestUtils.setField(imagenService, "uploadLocation", tempDir.toString());

        agrupacion = new Agrupacion();
        agrupacion.setId(10L);

        imagen = new Imagen();
        imagen.setId(1L);
        imagen.setNombreFichero("test-uuid.jpg");
        imagen.setRutaAbsoluta(tempDir.resolve("test-uuid.jpg").toString());

        imagenResponse = new ImagenResponse();
        imagenResponse.setId(1L);
        imagenResponse.setUrlPublica("/api/imagenes/test-uuid.jpg");

        mockFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "data".getBytes());
    }

    @Test
    @DisplayName("Debe guardar una imagen correctamente en el sistema de archivos")
    void testGuardarImagen_Exito() throws IOException {
        // ARRANGE
        when(agrupacionRepository.findById(10L)).thenReturn(Optional.of(agrupacion));
        when(imagenRepository.save(any(Imagen.class))).thenReturn(imagen);
        when(imagenMapper.toResponse(any(Imagen.class))).thenReturn(imagenResponse);

        // ACT
        ImagenResponse result = imagenService.subirImagen(10L, mockFile, true);

        // ASSERT
        assertNotNull(result);
        assertEquals(imagenResponse.getUrlPublica(), result.getUrlPublica());
        
        verify(imagenRepository).save(any(Imagen.class));
        verify(imagenRepository).desmarcarPortadaActual(10L); // Verifica que se desmarca la portada anterior
        
        // Verificar que se creó algún archivo en el directorio temporal
        assertTrue(Files.list(tempDir).count() > 0);
    }

    @Test
    @DisplayName("Debe eliminar una imagen correctamente del sistema de archivos")
    void testEliminarImagen_Exito() throws IOException {
        // ARRANGE
        // Primero creamos un archivo real para que pueda ser borrado
        Path archivoReal = tempDir.resolve("test-uuid.jpg");
        Files.createFile(archivoReal);
        imagen.setRutaAbsoluta(archivoReal.toString());

        when(imagenRepository.findById(1L)).thenReturn(Optional.of(imagen));

        // ACT
        imagenService.eliminarImagen(1L);

        // ASSERT
        verify(imagenRepository).delete(imagen);
        assertFalse(Files.exists(archivoReal)); // Verificar que el archivo físico fue borrado
    }
}