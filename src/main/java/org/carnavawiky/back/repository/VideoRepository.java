package org.carnavawiky.back.repository;

import org.carnavawiky.back.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    // Para el público: solo ver los que ya están verificados
    List<Video> findByVerificadoTrue();
}