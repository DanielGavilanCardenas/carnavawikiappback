package org.carnavawiky.back.repository;

import org.carnavawiky.back.model.Persona;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {

    Optional<Persona> findByNombreReal(String nombreReal);

    // Búsqueda para paginación por nombre real o apodo
    Page<Persona> findByNombreRealContainingIgnoreCaseOrApodoContainingIgnoreCase(
            String nombreReal, String apodo, Pageable pageable);

    // Para validar la unicidad de la relación 1:1 con Usuario
    Optional<Persona> findByUsuario_Id(Long usuarioId);
}