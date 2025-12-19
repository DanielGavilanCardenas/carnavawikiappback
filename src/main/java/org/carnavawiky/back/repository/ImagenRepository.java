package org.carnavawiky.back.repository;

import org.carnavawiky.back.model.Agrupacion;
import org.carnavawiky.back.model.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImagenRepository extends JpaRepository<Imagen, Long> {

    // 1. Encontrar la imagen de portada de una Agrupación
    Optional<Imagen> findByAgrupacion_IdAndEsPortadaTrue(Long agrupacionId);

    // 2. Listar todas las imágenes de una Agrupación
    List<Imagen> findByAgrupacion_Id(Long agrupacionId);

    // 3. Mét para desmarcar la portada actual antes de marcar una nueva
    @Modifying
    @Query("UPDATE Imagen i SET i.esPortada = FALSE WHERE i.agrupacion.id = :agrupacionId AND i.esPortada = TRUE")
    void desmarcarPortadaActual(@Param("agrupacionId") Long agrupacionId);
}