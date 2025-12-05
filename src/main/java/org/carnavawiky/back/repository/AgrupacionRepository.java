package org.carnavawiky.back.repository;

import org.carnavawiky.back.model.Agrupacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgrupacionRepository extends JpaRepository<Agrupacion, Long> {
    // Puedes añadir métodos personalizados aquí si son necesarios
}