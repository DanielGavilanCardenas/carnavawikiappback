package org.carnavawiky.back.service;

import org.carnavawiky.back.model.Usuario;
import org.carnavawiky.back.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    private EmailService emailService;

    @Value("${app.frontend-url:http://localhost:4200}")
    private String frontendUrl;

    @Transactional
    public void generateAndSendActivationEmail(Usuario usuario) {
        String token = UUID.randomUUID().toString();
        usuario.setActivationToken(token);
        usuarioRepository.save(usuario);

        String activationUrl = frontendUrl + "/activar/" + token;

        String subject = "Activa tu cuenta en Carnavawiky";
        String body = "<html>" +
                "<body style='font-family: Arial, sans-serif; color: #333;'>" +
                "  <div style='max-width: 600px; margin: 20px auto; border: 1px solid #ddd; padding: 20px; border-radius: 8px;'>" +
                "    <h2 style='color: #0d6efd;'>¡Hola " + usuario.getUsername() + "!</h2>" +
                "    <p>Gracias por registrarte. Haz clic en el botón de abajo para activar tu cuenta:</p>" +
                "    <div style='text-align: center; margin: 30px 0;'>" +
                "      <a href='" + activationUrl + "' " +
                "         style='background-color: #0d6efd; color: white; padding: 15px 25px; " +
                "         text-decoration: none; border-radius: 5px; font-weight: bold; display: inline-block;'>" +
                "        Activar mi cuenta" +
                "      </a>" +
                "    </div>" +
                "    <p style='font-size: 0.8em; color: #777;'>Si el botón no funciona, copia este enlace: " + activationUrl + "</p>" +
                "  </div>" +
                "</body>" +
                "</html>";

        emailService.sendEmail(usuario.getEmail(), subject, body);
    }

    @Transactional
    public void activateUser(String token) {
        // Buscamos el usuario por el token
        Optional<Usuario> usuarioOpt = usuarioRepository.findByActivationToken(token);

        if (!usuarioOpt.isPresent()) {
            // Si el token no existe, es posible que el usuario ya esté activo
            // (por una doble pulsación o recarga). No lanzamos error si ya está activo.
            // Esto evita el 404 molesto en el frontend.
            return;
        }

        Usuario usuario = usuarioOpt.get();

        // Si el usuario existe y no está habilitado, lo habilitamos
        if (!usuario.isEnabled()) {
            usuario.setEnabled(true);
            usuario.setActivationToken(null); // Borramos el token tras el primer uso exitoso
            usuarioRepository.save(usuario);
        }
    }
}