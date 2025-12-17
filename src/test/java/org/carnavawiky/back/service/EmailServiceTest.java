package org.carnavawiky.back.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    @DisplayName("Debe configurar y enviar el mensaje de correo correctamente")
    void testSendEmail_Success() {
        // ARRANGE (Datos de entrada)
        String to = "usuario@ejemplo.com";
        String subject = "Asunto de Prueba";
        String body = "Contenido del mensaje de prueba";

        // ArgumentCaptor nos permite "atrapar" el objeto que se envía a mailSender.send()
        // para inspeccionar sus propiedades internas.
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        // ACT
        emailService.sendEmail(to, subject, body);

        // ASSERT
        // 1. Verificamos que el método send del mailSender fue llamado exactamente 1 vez
        verify(mailSender, times(1)).send(messageCaptor.capture());

        // 2. Obtenemos el mensaje capturado y validamos sus campos
        SimpleMailMessage sentMessage = messageCaptor.getValue();

        assertEquals("carnavawiky@noreply.com", sentMessage.getFrom(), "El remitente debe ser el configurado");
        assertEquals(to, sentMessage.getTo()[0], "El destinatario debe coincidir");
        assertEquals(subject, sentMessage.getSubject(), "El asunto debe coincidir");
        assertEquals(body, sentMessage.getText(), "El cuerpo del mensaje debe coincidir");
    }
}