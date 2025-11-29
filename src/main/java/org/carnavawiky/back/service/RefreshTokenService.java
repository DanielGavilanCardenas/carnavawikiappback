package org.carnavawiky.back.service;

import org.carnavawiky.back.exceptions.TokenRefreshException;
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

@Service
public class RefreshTokenService {

    @Value("${jwt.refresh.expiration-days:7}")
    private Long refreshTokenDurationDays;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // 1. Busca un Refresh Token por su valor
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    // 2. Crea y guarda un nuevo Refresh Token para un usuario
    @Transactional
    public RefreshToken createRefreshToken(Usuario usuario) {
        // Primero, elimina cualquier token anterior si existe
        refreshTokenRepository.deleteByUsuario(usuario);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsuario(usuario);
        // El token expira en 'N' días
        refreshToken.setExpiryDate(Instant.now().plusSeconds(refreshTokenDurationDays * 24 * 60 * 60));
        // Genera un token aleatorio único
        refreshToken.setToken(UUID.randomUUID().toString());

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