package org.carnavawiky.back.repository;

import org.carnavawiky.back.model.Localidad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalidadRepository extends JpaRepository<Localidad, Long> {

    // Mét para paginación y búsqueda por nombre (sin distinción de mayúsculas/minúsculas)
    Page<Localidad> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
}