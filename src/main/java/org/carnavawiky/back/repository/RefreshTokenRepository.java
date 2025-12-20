package org.carnavawiky.back.repository;

import org.carnavawiky.back.model.RefreshToken;
import org.carnavawiky.back.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    // Necesario para buscar el Refresh Token al renovar el JWT
    Optional<RefreshToken> findByToken(String token);

    // Necesario para eliminar el token antiguo al generar uno nuevo, o al cerrar sesión
    void deleteByUsuario(Usuario usuario);

    // Buscamos el token por el objeto Usuario
    Optional<RefreshToken> findByUsuario(Usuario usuario);

    // Si ya tienes un mét para buscar por ID de usuario, también sirve:
    Optional<RefreshToken> findByUsuarioId(Long usuarioId);
}