package org.carnavawiky.back.repository;

import org.carnavawiky.back.model.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagenRepository extends JpaRepository<Imagen, Long> {

    List<Imagen> findByAgrupacion_Id(Long agrupacionId);

    @Modifying
    @Query("UPDATE Imagen i SET i.esPortada = FALSE WHERE i.agrupacion.id = :agrupacionId AND i.esPortada = TRUE")
    void desmarcarPortadaActual(@Param("agrupacionId") Long agrupacionId);
}