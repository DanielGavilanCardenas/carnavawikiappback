package org.carnavawiky.back.repository;

import org.carnavawiky.back.model.Agrupacion;
import org.springframework.data.domain.Page; // <-- NECESARIA
import org.springframework.data.domain.Pageable; // <-- NECESARIA
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgrupacionRepository extends JpaRepository<Agrupacion, Long> {

    // Met PARA BÚSQUEDA (Página y filtra por nombre o descripción)
    Page<Agrupacion> findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase(
            String nombre, String descripcion, Pageable pageable);

    // Mét para buscar agrupaciones por una parte de su nombre, ignorando mayúsculas/minúsculas, con paginación.
    Page<Agrupacion> findAllByNombreContainingIgnoreCase(String nombre, Pageable pageable);

    @Query(value = "SELECT DISTINCT a FROM Agrupacion a " +
            "LEFT JOIN FETCH a.imagenes " +
            "LEFT JOIN FETCH a.componentes c " +
            "LEFT JOIN FETCH c.persona " +
            "LEFT JOIN FETCH a.videos " +
            "JOIN FETCH a.localidad",
            countQuery = "SELECT count(a) FROM Agrupacion a")
    Page<Agrupacion> findAllWithDetails(Pageable pageable);
}