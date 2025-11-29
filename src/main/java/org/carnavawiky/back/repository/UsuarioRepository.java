package org.carnavawiky.back.repository;

import org.carnavawiky.back.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Necesario para la autenticación y el registro (validar si existe)
    Optional<Usuario> findByUsername(String username);

    // Necesario para el registro (validar si el email ya existe)
    boolean existsByEmail(String email);

    // Necesario para el registro (validar si el username ya existe)
    boolean existsByUsername(String username);

    // Necesario para la activación por email
    Optional<Usuario> findByActivationToken(String activationToken);
}