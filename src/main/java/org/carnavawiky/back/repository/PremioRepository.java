package org.carnavawiky.back.repository;

import org.carnavawiky.back.model.Premio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PremioRepository extends JpaRepository<Premio, Long> {

    // Búsqueda por año de Edición (útil para listar premios de un año)
    Page<Premio> findByEdicion_Anho(Integer anho, Pageable pageable);

    // Búsqueda por nombre de Agrupación (útil para listar premios de una agrupación)
    Page<Premio> findByAgrupacion_NombreContainingIgnoreCase(String nombreAgrupacion, Pageable pageable);
}