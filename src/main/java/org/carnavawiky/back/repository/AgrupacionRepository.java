package org.carnavawiky.back.repository;

import org.carnavawiky.back.model.Agrupacion;
import org.springframework.data.domain.Page; // <-- NECESARIA
import org.springframework.data.domain.Pageable; // <-- NECESARIA
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgrupacionRepository extends JpaRepository<Agrupacion, Long> {

    // Met PARA BÚSQUEDA (Página y filtra por nombre o descripción)
    Page<Agrupacion> findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase(
            String nombre, String descripcion, Pageable pageable);

    // Mét para buscar agrupaciones por una parte de su nombre, ignorando mayúsculas/minúsculas, con paginación.
    Page<Agrupacion> findAllByNombreContainingIgnoreCase(String nombre, Pageable pageable);
}