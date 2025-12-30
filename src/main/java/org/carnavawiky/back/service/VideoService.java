package org.carnavawiky.back.service;

import org.carnavawiky.back.dto.VideoRequest;
import org.carnavawiky.back.dto.VideoResponse;
import org.carnavawiky.back.model.Agrupacion;
import org.carnavawiky.back.model.Video;
import org.carnavawiky.back.repository.AgrupacionRepository;
import org.carnavawiky.back.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private AgrupacionRepository agrupacionRepository;

    public List<VideoResponse> listarVerificados() {
        return videoRepository.findByVerificadoTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public VideoResponse guardar(VideoRequest request) {
        // Buscamos la agrupación; si no existe, lanzamos error
        Agrupacion agrupacion = agrupacionRepository.findById(request.getAgrupacionId())
                .orElseThrow(() -> new RuntimeException("Agrupación no encontrada"));

        Video video = new Video();
        video.setTitulo(request.getTitulo());
        video.setUrlYoutube(request.getUrlYoutube());
        video.setAgrupacion(agrupacion);

        return mapToResponse(videoRepository.save(video));
    }

    public VideoResponse verificarVideo(Long id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video no encontrado"));
        video.setVerificado(true);
        return mapToResponse(videoRepository.save(video));
    }

    public void eliminar(Long id) {
        videoRepository.deleteById(id);
    }

    private VideoResponse mapToResponse(Video video) {
        return VideoResponse.builder()
                .id(video.getId())
                .titulo(video.getTitulo())
                .urlYoutube(video.getUrlYoutube())
                .verificado(video.isVerificado())
                .agrupacionId(video.getAgrupacion().getId())
                .agrupacionNombre(video.getAgrupacion().getNombre())
                .build();
    }
}