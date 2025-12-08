package org.carnavawiky.back.repository;

import org.carnavawiky.back.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    // Método necesario para la validación de unicidad en POST/PUT
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    // Método para buscar en el flujo de Activación
    Optional<Usuario> findByActivationToken(String activationToken);

    // Método para buscar en el flujo de Reseteo (por email)
    Optional<Usuario> findByEmail(String email);

    // =======================================================
    // 🔑 MÉTODO REQUERIDO PARA EL RESETEO DE CONTRASEÑA
    // =======================================================
    Optional<Usuario> findByResetToken(String resetToken);
}