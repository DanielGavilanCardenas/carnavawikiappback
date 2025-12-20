package org.carnavawiky.back;

import org.carnavawiky.back.model.Role;
import org.carnavawiky.back.model.Role.RoleName;
import org.carnavawiky.back.model.Usuario;
import org.carnavawiky.back.repository.RoleRepository;
import org.carnavawiky.back.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty; // Nueva importación
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;



@SpringBootApplication
public class CarnavawikiappbackApplication {

    Logger logger = Logger.getLogger(getClass().getName());

    @Value("${app.security.defaultAdminPass}")
    private String defaultAdminPass;

    public static void main(String[] args) {
        SpringApplication.run(CarnavawikiappbackApplication.class, args);
    }

    @Bean
    // Solo crea este bean si la propiedad es 'true'
    @ConditionalOnProperty(
            name = "app.security.seed-enabled",
            havingValue = "true",
            matchIfMissing = false // Si la propiedad no existe, no se ejecuta
    )
    public CommandLineRunner dataSeeder(
            RoleRepository roleRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            logger.info("---------------------------------------------------------");
            logger.info("INICIANDO Data Seeding de Seguridad (app.security.seed-enabled=true)");
            logger.info("---------------------------------------------------------");

            // =======================================================
            // DATA SEEDING DE ROLES
            // =======================================================
            // Usamos findByName para asegurar la idempotencia en DBs permanentes
            if (roleRepository.findByName(RoleName.ROLE_USER).isEmpty()) {
                Role roleUser = new Role();
                roleUser.setName(RoleName.ROLE_USER);
                roleRepository.save(roleUser);
            }

            if (roleRepository.findByName(RoleName.ROLE_ADMIN).isEmpty()) {
                Role roleAdmin = new Role();
                roleAdmin.setName(RoleName.ROLE_ADMIN);
                roleRepository.save(roleAdmin);
            }

            // =======================================================
            // DATA SEEDING DE USUARIO ADMIN
            // =======================================================
            if (usuarioRepository.findByEmail("admin@carnavawiky.com").isEmpty()) {
                logger.info("Creando usuario administrador inicial...");

                Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("Error: Role 'ROLE_ADMIN' no encontrado en DB."));

                Usuario adminUser = new Usuario();
                adminUser.setUsername("admin");
                adminUser.setEmail("admin@carnavawiky.com");
                adminUser.setPassword(passwordEncoder.encode(defaultAdminPass));

                Set<Role> roles = new HashSet<>();
                roles.add(adminRole);
                adminUser.setRoles(roles);

                adminUser.setEnabled(true);

                usuarioRepository.save(adminUser);
                logger.info("Usuario Administrador creado. Username: admin | Pass: " + defaultAdminPass +"!");
            }

            logger.info("Data Seeding de seguridad completado.");
        };
    }

}