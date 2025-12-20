package org.carnavawiky.back.repository;

import org.carnavawiky.back.model.Concurso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcursoRepository extends JpaRepository<Concurso, Long> {

    // Método para paginación y búsqueda por nombre (sin distinción de mayúsculas/minúsculas)
    Page<Concurso> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
}