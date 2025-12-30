package org.carnavawiky.back.service;

import org.carnavawiky.back.dto.VideoRequest;
import org.carnavawiky.back.dto.VideoResponse;
import org.carnavawiky.back.model.Agrupacion;
import org.carnavawiky.back.model.Video;
import org.carnavawiky.back.repository.AgrupacionRepository;
import org.carnavawiky.back.repository.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoServiceTest {

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private AgrupacionRepository agrupacionRepository;

    @InjectMocks
    private VideoService videoService;

    private Video video;
    private VideoRequest request;
    private Agrupacion agrupacion;

    @BeforeEach
    void setUp() {
        agrupacion = new Agrupacion();
        agrupacion.setId(10L);
        agrupacion.setNombre("Los Piratas");

        video = new Video();
        video.setId(1L);
        video.setTitulo("Actuación Final");
        video.setUrlYoutube("https://youtube.com/watch?v=12345");
        video.setVerificado(false);
        video.setAgrupacion(agrupacion);

        request = new VideoRequest();
        request.setTitulo("Actuación Final");
        request.setUrlYoutube("https://youtube.com/watch?v=12345");
        request.setAgrupacionId(10L);
    }

    @Test
    @DisplayName("Debe listar solo los vídeos verificados")
    void testListarVerificados() {
        video.setVerificado(true);
        when(videoRepository.findByVerificadoTrue()).thenReturn(Collections.singletonList(video));

        List<VideoResponse> result = videoService.listarVerificados();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Actuación Final", result.get(0).getTitulo());
        assertTrue(result.get(0).isVerificado());
        assertEquals(10L, result.get(0).getAgrupacionId());
        assertEquals("Los Piratas", result.get(0).getAgrupacionNombre());
        verify(videoRepository).findByVerificadoTrue();
    }

    @Test
    @DisplayName("Debe guardar un nuevo vídeo (por defecto no verificado)")
    void testGuardar() {
        when(agrupacionRepository.findById(10L)).thenReturn(Optional.of(agrupacion));
        when(videoRepository.save(any(Video.class))).thenAnswer(invocation -> {
            Video v = invocation.getArgument(0);
            v.setId(1L); // Simular ID generado
            return v;
        });

        VideoResponse result = videoService.guardar(request);

        assertNotNull(result);
        assertEquals("Actuación Final", result.getTitulo());
        assertFalse(result.isVerificado()); // Por defecto false
        assertEquals(10L, result.getAgrupacionId());
        assertEquals("Los Piratas", result.getAgrupacionNombre());
        verify(agrupacionRepository).findById(10L);
        verify(videoRepository).save(any(Video.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al guardar si la agrupación no existe")
    void testGuardar_AgrupacionNoEncontrada() {
        when(agrupacionRepository.findById(10L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            videoService.guardar(request);
        });

        assertEquals("Agrupación no encontrada", exception.getMessage());
        verify(agrupacionRepository).findById(10L);
        verify(videoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe eliminar un vídeo por ID")
    void testEliminar() {
        doNothing().when(videoRepository).deleteById(1L);

        assertDoesNotThrow(() -> videoService.eliminar(1L));
        verify(videoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Debe verificar un vídeo existente")
    void testVerificarVideo_Exito() {
        when(videoRepository.findById(1L)).thenReturn(Optional.of(video));
        when(videoRepository.save(any(Video.class))).thenAnswer(invocation -> invocation.getArgument(0));

        VideoResponse result = videoService.verificarVideo(1L);

        assertNotNull(result);
        assertTrue(result.isVerificado());
        assertEquals(10L, result.getAgrupacionId());
        verify(videoRepository).findById(1L);
        verify(videoRepository).save(video);
    }

    @Test
    @DisplayName("Debe lanzar excepción al intentar verificar un vídeo inexistente")
    void testVerificarVideo_NoEncontrado() {
        when(videoRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            videoService.verificarVideo(99L);
        });

        assertEquals("Video no encontrado", exception.getMessage());
        verify(videoRepository).findById(99L);
        verify(videoRepository, never()).save(any());
    }
}
