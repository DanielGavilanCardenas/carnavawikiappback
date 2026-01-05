package org.carnavawiky.back.repository;

import org.carnavawiky.back.model.Texto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TextoRepository extends JpaRepository<Texto, Long> {
    Optional<Texto> findByKey(String key);
    void deleteByKey(String key);
}