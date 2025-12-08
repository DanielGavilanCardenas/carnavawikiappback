package org.carnavawiky.back.service;

import org.carnavawiky.back.exception.BadRequestException;
import org.carnavawiky.back.exception.ResourceNotFoundException;
import org.carnavawiky.back.model.Usuario;
import org.carnavawiky.back.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
// Importamos Value para obtener la URL base (opcional, pero buena práctica)
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class ActivationService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmailService emailService; // <--- INYECCIÓN DEL SERVICIO REAL

    // Configuramos la URL base de tu API (debe coincidir con la configuración real)
    @Value("${app.base-url:http://localhost:8083}") // Valor por defecto si no está en application.properties
    private String baseUrl;

    /**
     * Genera un token de activación único, lo guarda y ENVÍA EL EMAIL REAL.
     * @param usuario El usuario recién creado.
     */
    @Transactional
    public void generateAndSendActivationEmail(Usuario usuario) {
        // 1. Generar un token único y seguro (UUID)
        String token = UUID.randomUUID().toString();

        // 2. Asignar el token al usuario
        usuario.setActivationToken(token);

        usuarioRepository.save(usuario);

        // 3. ENVÍO DEL EMAIL REAL
        String activationUrl = baseUrl + "/api/auth/activate/" + token;

        String subject = "Activación de Cuenta Carnavawiky";
        String body = "¡Hola " + usuario.getUsername() + "!\n\n"
                + "Gracias por registrarte en Carnavawiky. Por favor, haz clic en el siguiente enlace "
                + "para activar tu cuenta y poder iniciar sesión:\n\n"
                + activationUrl + "\n\n"
                + "Si tienes algún problema, contacta con soporte.";

        // Llamada al EmailService
        emailService.sendEmail(usuario.getEmail(), subject, body); // <--- USO DEL SERVICIO REAL
    }

    /**
     * Procesa la activación de la cuenta usando el token recibido por el usuario.
     * ... (El resto del método activateUser() permanece igual)
     */
    @Transactional
    public void activateUser(String token) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByActivationToken(token);

        if (!usuarioOpt.isPresent()) {
            throw new ResourceNotFoundException("Token", "valor", token);
        }

        Usuario usuario = usuarioOpt.get();

        if (usuario.isEnabled()) {
            throw new BadRequestException("La cuenta de usuario ya estaba activa.");
        }

        // 1. Activar la cuenta
        usuario.setEnabled(true);
        // 2. Invalidar el token para que no pueda ser reutilizado
        usuario.setActivationToken(null);

        usuarioRepository.save(usuario);
    }
}