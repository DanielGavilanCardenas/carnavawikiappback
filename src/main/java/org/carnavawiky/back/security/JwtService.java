package org.carnavawiky.back.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtService {

    // Clave secreta para firmar y verificar el token
    @Value("${jwt.secret.key}")
    private String jwtSecretKey;

    // Duración del token de acceso (ej: 15 minutos)
    @Value("${jwt.access.expiration-minutes:15}")
    private Long jwtExpirationMinutes;

    // Genera la clave secreta a partir de la cadena de texto
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Genera el Token JWT de Acceso a partir de la información de autenticación.
     */
    public String generateAccessToken(Authentication authentication) {

        // 1. Obtiene los roles (Authorities) del usuario para incluirlos en el token
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // Calcula la fecha de expiración
        long expirationMillis = nowMillis + (jwtExpirationMinutes * 60 * 1000); // Convierte minutos a milisegundos
        Date expirationDate = new Date(expirationMillis);

        // 2. Construye y firma el token con la clave secreta (JJWT)
        return Jwts.builder()
                .setSubject(authentication.getName()) // Username
                .claim("roles", roles)              // Roles
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Valida el token JWT verificando la firma y la fecha de expiración.
     */
    public boolean validateToken(String token) {
        try {
            // Sintaxis alternativa para evitar el error 'parserBuilder'
            Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // Si hay alguna excepción (token expirado, firma inválida), retorna falso
            // logger.warn("JWT inválido o expirado: " + e.getMessage()); // Descomentar en producción
            return false;
        }
    }

    /**
     * Extrae el nombre de usuario (subject) del token JWT.
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    /**
     * Devuelve el tiempo de expiración del Access Token en segundos.
     */
    public Long getExpirationSeconds() {
        return jwtExpirationMinutes * 60;
    }
}