package org.carnavawiky.back.repository;

import org.carnavawiky.back.model.Comentario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    // 1. Obtener SOLO comentarios APROBADOS paginados por Agrupación (Uso Público)
    Page<Comentario> findByAgrupacion_IdAndAprobadoTrueOrderByFechaCreacionDesc(Long agrupacionId, Pageable pageable);

    // 2. Obtener SOLO comentarios APROBADOS paginados por Usuario (Uso Público)
    Page<Comentario> findByUsuario_IdAndAprobadoTrue(Long usuarioId, Pageable pageable);

    // 3. Obtener TODOS los comentarios (Aprobados o No) para la moderación (Uso ADMIN)
    // Usamos findAll() estándar de JpaRepository.

    // 4. Búsqueda simple por contenido (SOLO APROBADOS para público)
    Page<Comentario> findByContenidoContainingIgnoreCaseAndAprobadoTrue(String contenido, Pageable pageable);
}