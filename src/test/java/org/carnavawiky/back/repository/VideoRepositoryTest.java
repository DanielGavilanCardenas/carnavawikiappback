package org.carnavawiky.back.repository;

import org.carnavawiky.back.model.Video;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class VideoRepositoryTest {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private TestEntityManager entityManager;

    // =======================================================
    // 1. PRUEBA DE CREACIÓN Y BÚSQUEDA POR ID
    // =======================================================
    @Test
    void testSaveAndFindById() {
        // Arrange
        Video newVideo = new Video();
        newVideo.setTitulo("Actuación Final");
        newVideo.setUrlYoutube("https://youtube.com/watch?v=12345");
        newVideo.setVerificado(false);

        // Act
        Video savedVideo = videoRepository.save(newVideo);
        Optional<Video> foundVideo = videoRepository.findById(savedVideo.getId());

        // Assert
        assertThat(foundVideo).isPresent();
        assertThat(foundVideo.get().getTitulo()).isEqualTo("Actuación Final");
        assertThat(foundVideo.get().getUrlYoutube()).isEqualTo("https://youtube.com/watch?v=12345");
        assertThat(foundVideo.get().isVerificado()).isFalse();
    }

    // =======================================================
    // 2. PRUEBA DE BÚSQUEDA DE ALL
    // =======================================================
    @Test
    void testFindAll() {
        // Arrange
        Video v1 = new Video();
        v1.setTitulo("Video 1");
        entityManager.persist(v1);

        Video v2 = new Video();
        v2.setTitulo("Video 2");
        entityManager.persist(v2);

        entityManager.flush();

        // Act
        List<Video> videos = videoRepository.findAll();

        // Assert
        assertThat(videos).hasSize(2);
        assertThat(videos).extracting(Video::getTitulo).containsExactlyInAnyOrder("Video 1", "Video 2");
    }

    // =======================================================
    // 3. PRUEBA DE ACTUALIZACIÓN
    // =======================================================
    @Test
    void testUpdate() {
        // Arrange
        Video existingVideo = new Video();
        existingVideo.setTitulo("Video Original");
        Video savedVideo = entityManager.persistFlushFind(existingVideo);

        // Act
        savedVideo.setTitulo("Video Actualizado");
        Video updatedVideo = videoRepository.save(savedVideo);

        // Assert
        assertThat(updatedVideo.getTitulo()).isEqualTo("Video Actualizado");
    }

    // =======================================================
    // 4. PRUEBA DE ELIMINACIÓN
    // =======================================================
    @Test
    void testDelete() {
        // Arrange
        Video videoToDelete = new Video();
        videoToDelete.setTitulo("Video a borrar");
        Video savedVideo = entityManager.persistFlushFind(videoToDelete);

        // Act
        videoRepository.deleteById(savedVideo.getId());

        // Assert
        Optional<Video> foundVideo = videoRepository.findById(savedVideo.getId());
        assertThat(foundVideo).isNotPresent();
    }

    // =======================================================
    // 5. PRUEBA DE BÚSQUEDA POR VERIFICADO (findByVerificadoTrue)
    // =======================================================
    @Test
    void testFindByVerificadoTrue() {
        // Arrange
        Video vVerificado1 = new Video();
        vVerificado1.setTitulo("Verificado 1");
        vVerificado1.setVerificado(true);
        entityManager.persist(vVerificado1);

        Video vVerificado2 = new Video();
        vVerificado2.setTitulo("Verificado 2");
        vVerificado2.setVerificado(true);
        entityManager.persist(vVerificado2);

        Video vNoVerificado = new Video();
        vNoVerificado.setTitulo("No Verificado");
        vNoVerificado.setVerificado(false);
        entityManager.persist(vNoVerificado);

        entityManager.flush();

        // Act
        List<Video> verificados = videoRepository.findByVerificadoTrue();

        // Assert
        assertThat(verificados).hasSize(2);
        assertThat(verificados).extracting(Video::getTitulo)
                .containsExactlyInAnyOrder("Verificado 1", "Verificado 2");
        assertThat(verificados).extracting(Video::isVerificado)
                .containsOnly(true);
    }
}
