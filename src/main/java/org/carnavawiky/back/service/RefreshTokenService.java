package org.carnavawiky.back.service;

import org.carnavawiky.back.exception.TokenRefreshException;
import org.carnavawiky.back.model.RefreshToken;
import org.carnavawiky.back.model.Usuario;
import org.carnavawiky.back.repository.RefreshTokenRepository;
import org.carnavawiky.back.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.EntityManager; // Importa el EntityManager


@Service
public class RefreshTokenService {

    @Value("${jwt.refresh.expiration-days:7}")
    private Long refreshTokenDurationDays;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EntityManager entityManager; // Inyecta el EntityManager


    // 1. Busca un Refresh Token por su valor
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }


    /**
     * Crea un nuevo Refresh Token para el usuario dado.
     * Si ya existe un token activo para el usuario, lo elimina ANTES de crear uno nuevo,
     * forzando la sincronización de la DB para evitar errores de unicidad (Duplicate Entry).
     * * @param usuario El objeto Usuario para el cual crear el token.
     * @return El nuevo RefreshToken creado.
     */
    @Transactional
    public RefreshToken createRefreshToken(Usuario usuario) {

        // 1. Buscamos el token existente para este usuario.
        refreshTokenRepository.findByUsuario(usuario)
                .ifPresent(token -> {
                    // 2. Si existe un token, lo eliminamos.
                    refreshTokenRepository.delete(token);

                    // 3. FORZAMOS LA SINCRONIZACIÓN (FLUSH) DE LA ELIMINACIÓN.
                    // Esto obliga a Hibernate a ejecutar el DELETE en la DB antes del INSERT (paso 5).
                    entityManager.flush();
                });

        // 4. Crear el nuevo Refresh Token
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsuario(usuario);

        // El token expira en 'N' días
        long expirationSeconds = refreshTokenDurationDays * 24 * 60 * 60;
        refreshToken.setExpiryDate(Instant.now().plusSeconds(expirationSeconds));

        // Genera un token aleatorio único
        refreshToken.setToken(UUID.randomUUID().toString());

        // 5. Guardar el nuevo token (INSERT). Ahora que el DELETE fue forzado, este INSERT debe funcionar.
        return refreshTokenRepository.save(refreshToken);
    }

    // 3. Verifica si el token ha expirado
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            // Si ha expirado, lo borramos de la DB
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(),
                    "Refresh token expirado. Por favor, inicie sesión de nuevo.");
        }
        return token;
    }
}