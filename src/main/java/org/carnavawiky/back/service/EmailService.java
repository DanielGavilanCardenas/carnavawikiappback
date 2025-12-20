package org.carnavawiky.back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("carnavawiky@noreply.com");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        // Envío (esto puede ser asíncrono en un entorno de producción, pero lo hacemos simple aquí)
        mailSender.send(message);
    }
}