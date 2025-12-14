package org.carnavawiky.back.repository;

import org.carnavawiky.back.model.Componente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponenteRepository extends JpaRepository<Componente, Long> {

    // Búsqueda por agrupación (útil para obtener el listado de componentes de una agrupación)
    Page<Componente> findByAgrupacion_Id(Long agrupacionId, Pageable pageable);

    // Búsqueda flexible por nombre/apodo de la Persona
    @Query("SELECT c FROM Componente c WHERE " +
            "LOWER(c.persona.nombreReal) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.persona.apodo) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.agrupacion.nombre) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Componente> findBySearchTerm(@Param("search") String search, Pageable pageable);
}