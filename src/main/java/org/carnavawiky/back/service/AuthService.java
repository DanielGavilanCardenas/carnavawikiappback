package org.carnavawiky.back.service;

import org.carnavawiky.back.dto.auth.LoginRequest;
import org.carnavawiky.back.dto.auth.PasswordResetRequest; // Importamos el nuevo DTO
import org.carnavawiky.back.dto.auth.RegisterRequest;
import org.carnavawiky.back.dto.auth.TokenResponse;
import org.carnavawiky.back.exception.BadRequestException;
import org.carnavawiky.back.exception.ResourceNotFoundException;
import org.carnavawiky.back.exception.TokenRefreshException;
import org.carnavawiky.back.model.Role;
import org.carnavawiky.back.model.Usuario;
import org.carnavawiky.back.repository.RoleRepository;
import org.carnavawiky.back.repository.UsuarioRepository;
import org.carnavawiky.back.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
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

    @Autowired
    private ActivationService activationService;

    @Autowired
    private EmailService emailService; // Necesario para enviar el email de reseteo

    @Value("${app.base-url:http://localhost:8083}")
    private String baseUrl;

    // =======================================================
    // MÉTODOS EXISTENTES (Registro, Login, Activación, Refresh)
    // =======================================================

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

        Role userRole = roleRepository.findByName(Role.RoleName.ROLE_USER)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", Role.RoleName.ROLE_USER.name()));

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        nuevoUsuario.setRoles(roles);

        nuevoUsuario.setEnabled(false);

        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);

        activationService.generateAndSendActivationEmail(usuarioGuardado);

        return usuarioGuardado;
    }

    @Transactional
    public TokenResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "username", request.getUsername()));

        String accessToken = jwtService.generateAccessToken(authentication);
        String refreshToken = refreshTokenService.createRefreshToken(usuario).getToken();

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(jwtService.getExpirationSeconds())
                .build();
    }

    @Transactional
    public void activateAccount(String token) {
        activationService.activateUser(token);
    }

    @Transactional
    public TokenResponse refreshToken(String refreshToken) {
        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(token -> {
                    Usuario usuario = token.getUsuario();
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            usuario.getUsername(), null, usuario.getAuthorities());

                    String newAccessToken = jwtService.generateAccessToken(authentication);

                    return TokenResponse.builder()
                            .accessToken(newAccessToken)
                            .refreshToken(refreshToken)
                            .expiresIn(jwtService.getExpirationSeconds())
                            .build();
                })
                .orElseThrow(() -> new TokenRefreshException(refreshToken, "Refresh token no encontrado en la base de datos."));
    }

    // =======================================================
    // MÉT PARA RECUPERACIÓN DE CONTRASEÑA
    // =======================================================

    /**
     * Genera un token de reseteo para el email dado y envía el email de recuperación.
     * @param email Email del usuario que solicita el reseteo.
     */
    @Transactional
    public void requestPasswordReset(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", email)); // Es mejor no dar detalles exactos, pero para el desarrollo está bien.

        // 1. Generar token y fecha de expiración (ej. 1 hora)
        String resetToken = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(1);

        // 2. Guardar token y fecha en el usuario
        usuario.setResetToken(resetToken);
        usuario.setResetTokenExpiryDate(expiryDate);
        usuarioRepository.save(usuario);

        // 3. Enviar email de recuperación (Debe redirigir a una página del frontend que envíe el token al endpoint de reseteo)
        String resetUrl = baseUrl + "/reset-password?token=" + resetToken; // URL del Frontend

        String subject = "Recuperación de Contraseña Carnavawiky";
        String body = "¡Hola!\n\n"
                + "Has solicitado restablecer tu contraseña. Haz clic en el siguiente enlace. "
                + "Este enlace caducará en 1 hora.\n\n"
                + resetUrl + "\n\n"
                + "Si no solicitaste esto, puedes ignorar este correo.";

        emailService.sendEmail(usuario.getEmail(), subject, body);
    }

    /**
     * Valida el token y establece la nueva contraseña.
     * @param request DTO que contiene el token y la nueva contraseña.
     */
    @Transactional
    public void resetPassword(PasswordResetRequest request) {

        Usuario usuario = usuarioRepository.findByResetToken(request.getToken())
                .orElseThrow(() -> new BadRequestException("Token de reseteo inválido."));

        // 1. Validar expiración del token
        if (usuario.getResetTokenExpiryDate() == null || usuario.getResetTokenExpiryDate().isBefore(LocalDateTime.now())) {
            // Limpiamos el token expirado antes de lanzar la excepción
            usuario.setResetToken(null);
            usuario.setResetTokenExpiryDate(null);
            usuarioRepository.save(usuario);
            throw new BadRequestException("El token de reseteo ha expirado. Por favor, solicita uno nuevo.");
        }

        // 2. Cifrar y actualizar la contraseña
        String newHashedPassword = passwordEncoder.encode(request.getNewPassword());
        usuario.setPassword(newHashedPassword);

        // 3. Limpiar los tokens de reseteo para que no puedan ser usados de nuevo
        usuario.setResetToken(null);
        usuario.setResetTokenExpiryDate(null);

        usuarioRepository.save(usuario);
    }
}