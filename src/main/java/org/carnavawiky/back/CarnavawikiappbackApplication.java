package org.carnavawiky.back;

import org.carnavawiky.back.service.EmailService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class CarnavawikiappbackApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarnavawikiappbackApplication.class, args);
	}

	@Bean
	public CommandLineRunner testRunner(EmailService emailService) {
		return args -> {
//			// --- PRUEBA DE ENVÍO DE EMAIL ---
//			System.out.println("Intentando enviar email de prueba...");
//			try {
//				emailService.sendEmail("daniel.gavilan.cardenas@gmail.com", "Prueba Spring Boot Email", "¡Hola! Este es un email de prueba desde Spring Boot.");
//				System.out.println("Email de prueba enviado con éxito.");
//			} catch (Exception e) {
//				System.err.println("ERROR AL ENVIAR EMAIL: " + e.getMessage());
//			}
		};
	}

}
