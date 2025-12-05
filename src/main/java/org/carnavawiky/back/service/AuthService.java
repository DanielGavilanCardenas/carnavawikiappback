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
import java.util.UUID;

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

    // Asumimos que tendremos un servicio de Email más adelante
    // @Autowired
    // private EmailService emailService;


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
        // Cifrar la contraseña
        nuevoUsuario.setPassword(passwordEncoder.encode(request.getPassword()));

        // Asignar el rol por defecto: ROLE_USER
        Role userRole = roleRepository.findByName(Role.RoleName.ROLE_USER)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", Role.RoleName.ROLE_USER.name()));
        nuevoUsuario.setRoles(Collections.singleton(userRole));

        // Preparar para activación por email
        nuevoUsuario.setEnabled(false);
        nuevoUsuario.setActivationToken(UUID.randomUUID().toString());

        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);

        // TODO: ENVÍO DE EMAIL
        // emailService.sendActivationEmail(usuarioGuardado);

        return usuarioGuardado;
    }

    /**
     * 2. Lógica de Login de Usuario.
     * Genera el Access Token y el Refresh Token si las credenciales son válidas.
     */
    @Transactional
    public TokenResponse login(LoginRequest request) {

        // 1. Autentica al usuario usando el AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // 2. Establece la autenticación en el contexto de seguridad (aunque sea stateless, es buena práctica)
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Obtiene el objeto Usuario para generar los tokens
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "username", request.getUsername()));

        // 4. Genera tokens
        String accessToken = jwtService.generateAccessToken(authentication);
        // Genera y guarda el Refresh Token en la DB
        String refreshToken = refreshTokenService.createRefreshToken(usuario).getToken();

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(jwtService.getExpirationSeconds())
                .build();
    }

    /**
     * 3. Lógica de Activación de Cuenta por Token de Email.
     */
    @Transactional
    public void activateAccount(String token) {
        Usuario usuario = usuarioRepository.findByActivationToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token de activación inválido o expirado."));

        usuario.setEnabled(true);
        usuario.setActivationToken(null); // Eliminar el token una vez usado
        usuarioRepository.save(usuario);
    }

    /**
     * 4. Lógica para Refrescar el Access Token usando el Refresh Token.
     */
    @Transactional
    public TokenResponse refreshToken(String refreshToken) {

        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration) // 1. Verifica si ha expirado
                .map(token -> {
                    // 2. Si es válido, cargamos el usuario asociado
                    Usuario usuario = token.getUsuario();
                    // Cargamos su Authentication para generar un nuevo Access Token
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            usuario.getUsername(), null, usuario.getAuthorities());

                    // 3. Genera un nuevo Access Token
                    String newAccessToken = jwtService.generateAccessToken(authentication);

                    // 4. Devuelve el Access Token nuevo, manteniendo el Refresh Token
                    return TokenResponse.builder()
                            .accessToken(newAccessToken)
                            .refreshToken(refreshToken)
                            .expiresIn(jwtService.getExpirationSeconds())
                            .build();
                })
                .orElseThrow(() -> new TokenRefreshException(refreshToken, "Refresh token no encontrado en la base de datos."));
    }
}