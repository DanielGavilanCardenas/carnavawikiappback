package org.carnavawiky.back.service;

import org.carnavawiky.back.dto.auth.LoginRequest;
import org.carnavawiky.back.dto.auth.RegisterRequest;
import org.carnavawiky.back.dto.auth.TokenResponse;
import org.carnavawiky.back.exception.ResourceNotFoundException;
import org.carnavawiky.back.exception.TokenRefreshException;
import org.carnavawiky.back.model.Role;
import org.carnavawiky.back.model.Usuario;
import org.carnavawiky.back.repository.RoleRepository;
import org.carnavawiky.back.repository.UsuarioRepository;
import org.carnavawiky.back.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private ActivationService activationService;


    /**
     * 1. Lógica de Registro de Nuevo Usuario.
     * Crea un usuario, asigna el rol por defecto (ROLE_USER) y requiere activación por email.
     */
    @Transactional
    public Usuario register(RegisterRequest request) {

        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
        }
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(request.getUsername());
        nuevoUsuario.setEmail(request.getEmail());
        nuevoUsuario.setPassword(passwordEncoder.encode(request.getPassword()));

        // Asignar el rol por defecto: ROLE_USER
        Role userRole = roleRepository.findByName(Role.RoleName.ROLE_USER)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", Role.RoleName.ROLE_USER.name()));

        // CORRECCIÓN: Usar HashSet mutable para evitar la UnsupportedOperationException de Hibernate.
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        nuevoUsuario.setRoles(roles);

        // Desactivar la cuenta por defecto (requiere activación)
        nuevoUsuario.setEnabled(false);

        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);

        // Llama al servicio centralizado para generar el token y enviar el email
        activationService.generateAndSendActivationEmail(usuarioGuardado);

        return usuarioGuardado;
    }

    /**
     * 2. Lógica de Login de Usuario.
     * Genera el Access Token y el Refresh Token si las credenciales son válidas.
     */
    @Transactional
    public TokenResponse login(LoginRequest request) {

        // 1. Autentica al usuario (Spring Security verifica enabled=true)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // 2. Establece la autenticación
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Obtiene el objeto Usuario
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "username", request.getUsername()));

        // 4. Genera tokens
        String accessToken = jwtService.generateAccessToken(authentication);
        String refreshToken = refreshTokenService.createRefreshToken(usuario).getToken();

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(jwtService.getExpirationSeconds())
                .build();
    }

    /**
     * 3. Lógica de Activación de Cuenta por Token de Email.
     * Delega la lógica de activación al ActivationService.
     */
    @Transactional
    public void activateAccount(String token) {
        activationService.activateUser(token);
    }

    /**
     * 4. Lógica para Refrescar el Access Token usando el Refresh Token.
     */
    @Transactional
    public TokenResponse refreshToken(String refreshToken) {

        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(token -> {
                    // Cargamos el usuario asociado
                    Usuario usuario = token.getUsuario();
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            usuario.getUsername(), null, usuario.getAuthorities());

                    // Genera un nuevo Access Token
                    String newAccessToken = jwtService.generateAccessToken(authentication);

                    return TokenResponse.builder()
                            .accessToken(newAccessToken)
                            .refreshToken(refreshToken)
                            .expiresIn(jwtService.getExpirationSeconds())
                            .build();
                })
                .orElseThrow(() -> new TokenRefreshException(refreshToken, "Refresh token no encontrado en la base de datos."));
    }
}