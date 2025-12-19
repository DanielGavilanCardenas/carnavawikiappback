package org.carnavawiky.back.repository;

import org.carnavawiky.back.model.Edicion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EdicionRepository extends JpaRepository<Edicion, Long> {

    // Mét para paginación y búsqueda por año
    Page<Edicion> findByAnho(Integer anho, Pageable pageable);

    // Mét para paginación y búsqueda por una parte del nombre del Concurso (para búsquedas flexibles)
    Page<Edicion> findByConcurso_NombreContainingIgnoreCase(String nombreConcurso, Pageable pageable);
}