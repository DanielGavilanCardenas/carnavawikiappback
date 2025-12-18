package org.carnavawiky.back.service;

import org.carnavawiky.back.dto.ImagenRequest;
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

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private FileStorageService fileStorageService;

    @Mock
    private ImagenMapper imagenMapper;

    @InjectMocks
    private ImagenService imagenService;

    private Agrupacion agrupacion;
    private Imagen imagen;
    private ImagenResponse imagenResponse;
    private ImagenRequest imagenRequest;
    private MockMultipartFile mockFile;

    @BeforeEach
    void setUp() {
        agrupacion = new Agrupacion();
        agrupacion.setId(10L);

        imagen = new Imagen();
        imagen.setId(1L);
        imagen.setNombreFichero("stored-name.jpg");

        imagenResponse = new ImagenResponse();
        imagenResponse.setId(1L);
        imagenResponse.setUrlPublica("http://localhost:8080/api/imagenes/view/stored-name.jpg");

        imagenRequest = new ImagenRequest();
        imagenRequest.setAgrupacionId(10L);
        imagenRequest.setEsPortada(true);

        mockFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "data".getBytes());
    }

    @Test
    @DisplayName("Debe guardar una imagen correctamente")
    void testGuardarImagen_Exito() throws IOException {
        // ARRANGE
        when(agrupacionRepository.findById(10L)).thenReturn(Optional.of(agrupacion));
        when(fileStorageService.storeFile(any())).thenReturn("stored-name.jpg");

        // CORRECCIÓN PARA EL NULL POINTER EXCEPTION:
        // Mockeamos getStorageLocation para que devuelva una ruta válida (Path) y no sea null
        Path fakePath = Paths.get("uploads");
        when(fileStorageService.getStorageLocation()).thenReturn(fakePath);

        when(imagenRepository.save(any(Imagen.class))).thenReturn(imagen);
        when(imagenMapper.toResponse(any(Imagen.class))).thenReturn(imagenResponse);

        // ACT
        // Asegúrate de que el orden sea (fichero, request) o (request, fichero) según tu Service.
        // En tu anterior log aparecía guardarImagen(ImagenRequest, MultipartFile)
        ImagenResponse result = imagenService.guardarImagen(mockFile, imagenRequest);

        // ASSERT
        assertNotNull(result);
        assertEquals(imagenResponse.getUrlPublica(), result.getUrlPublica());
        verify(imagenRepository).save(any(Imagen.class));
        verify(fileStorageService).storeFile(mockFile);
    }
}