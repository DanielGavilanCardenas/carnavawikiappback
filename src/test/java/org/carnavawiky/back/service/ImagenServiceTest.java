package org.carnavawiky.back.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
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
import java.util.Map;
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

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

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
        imagen.setNombreFichero("public_id_cloudinary");

        imagenResponse = new ImagenResponse();
        imagenResponse.setId(1L);
        imagenResponse.setUrlPublica("https://res.cloudinary.com/demo/image/upload/v1234567890/public_id_cloudinary.jpg");

        imagenRequest = new ImagenRequest();
        imagenRequest.setAgrupacionId(10L);
        imagenRequest.setEsPortada(true);

        mockFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "data".getBytes());
    }

    @Test
    @DisplayName("Debe guardar una imagen correctamente en Cloudinary")
    void testGuardarImagen_Exito() throws IOException {
        // ARRANGE
        when(agrupacionRepository.findById(10L)).thenReturn(Optional.of(agrupacion));
        
        // Mock de Cloudinary
        when(cloudinary.uploader()).thenReturn(uploader);
        Map<String, Object> uploadResult = Map.of(
            "public_id", "public_id_cloudinary",
            "secure_url", "https://res.cloudinary.com/demo/image/upload/v1234567890/public_id_cloudinary.jpg"
        );
        when(uploader.upload(any(byte[].class), any(Map.class))).thenReturn(uploadResult);

        when(imagenRepository.save(any(Imagen.class))).thenReturn(imagen);
        when(imagenMapper.toResponse(any(Imagen.class))).thenReturn(imagenResponse);

        // ACT
        ImagenResponse result = imagenService.guardarImagen(mockFile, imagenRequest);

        // ASSERT
        assertNotNull(result);
        assertEquals(imagenResponse.getUrlPublica(), result.getUrlPublica());
        verify(imagenRepository).save(any(Imagen.class));
        verify(uploader).upload(any(byte[].class), any(Map.class));
        verify(imagenRepository).desmarcarPortadaActual(10L); // Verifica que se desmarca la portada anterior
    }

    @Test
    @DisplayName("Debe eliminar una imagen correctamente de Cloudinary")
    void testEliminarImagen_Exito() throws IOException {
        // ARRANGE
        when(imagenRepository.findById(1L)).thenReturn(Optional.of(imagen));
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.destroy(anyString(), any(Map.class))).thenReturn(Map.of("result", "ok"));

        // ACT
        imagenService.eliminarImagen(1L);

        // ASSERT
        verify(imagenRepository).delete(imagen);
        verify(uploader).destroy(eq("public_id_cloudinary"), any(Map.class));
    }
}