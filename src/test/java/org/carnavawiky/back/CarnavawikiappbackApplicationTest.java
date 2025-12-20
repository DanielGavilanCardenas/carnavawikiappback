package org.carnavawiky.back;

import org.carnavawiky.back.model.Role;
import org.carnavawiky.back.model.Role.RoleName;
import org.carnavawiky.back.model.Usuario;
import org.carnavawiky.back.repository.RoleRepository;
import org.carnavawiky.back.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarnavawikiappbackApplicationTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CarnavawikiappbackApplication application;

    @Test
    @DisplayName("DataSeeder: Crea roles y usuario admin si no existen")
    void testDataSeeder_CreatesEverything() throws Exception {
        // Inyectar valor de propiedad @Value
        ReflectionTestUtils.setField(application, "defaultAdminPass", "admin123");

        // Configurar Mocks
        // 1. Roles no existen
        when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(Optional.empty());
        
        // Para ROLE_ADMIN:
        // Primera llamada (check existencia): Empty -> Entra al if y hace save
        // Segunda llamada (recuperar para usuario): Presente (simulando que se guardó o ya estaba)
        Role adminRole = new Role();
        adminRole.setId(2L);
        adminRole.setName(RoleName.ROLE_ADMIN);

        when(roleRepository.findByName(RoleName.ROLE_ADMIN))
                .thenReturn(Optional.empty()) 
                .thenReturn(Optional.of(adminRole));

        // Usuario no existe
        when(usuarioRepository.findByEmail("admin@carnavawiky.com")).thenReturn(Optional.empty());
        
        when(passwordEncoder.encode("admin123")).thenReturn("encodedPass");

        // Ejecutar
        CommandLineRunner runner = application.dataSeeder(roleRepository, usuarioRepository, passwordEncoder);
        runner.run();

        // Verificar
        verify(roleRepository, times(2)).save(any(Role.class)); // Se guardan ambos roles
        verify(usuarioRepository, times(1)).save(any(Usuario.class)); // Se guarda el usuario
    }

    @Test
    @DisplayName("DataSeeder: No hace nada si ya existen")
    void testDataSeeder_NothingCreated() throws Exception {
        ReflectionTestUtils.setField(application, "defaultAdminPass", "admin123");

        when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(Optional.of(new Role()));
        when(roleRepository.findByName(RoleName.ROLE_ADMIN)).thenReturn(Optional.of(new Role()));
        when(usuarioRepository.findByEmail("admin@carnavawiky.com")).thenReturn(Optional.of(new Usuario()));

        CommandLineRunner runner = application.dataSeeder(roleRepository, usuarioRepository, passwordEncoder);
        runner.run();

        verify(roleRepository, never()).save(any(Role.class));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("DataSeeder: Lanza excepción si falla al recuperar ROLE_ADMIN para crear usuario")
    void testDataSeeder_ThrowsIfAdminRoleMissingForUser() {
        ReflectionTestUtils.setField(application, "defaultAdminPass", "admin123");

        // Roles existen (para saltar el save inicial)
        when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(Optional.of(new Role()));
        
        // ROLE_ADMIN existe en el check inicial, pero falla en el segundo (simulando error de consistencia)
        when(roleRepository.findByName(RoleName.ROLE_ADMIN))
                .thenReturn(Optional.of(new Role())) // Check
                .thenReturn(Optional.empty());       // Fetch

        // Usuario no existe, intentará crearlo
        when(usuarioRepository.findByEmail("admin@carnavawiky.com")).thenReturn(Optional.empty());

        CommandLineRunner runner = application.dataSeeder(roleRepository, usuarioRepository, passwordEncoder);
        
        assertThrows(RuntimeException.class, () -> runner.run());
    }
    
    @Test
    @DisplayName("Constructor coverage")
    void testConstructor() {
        // Simplemente instanciamos la clase para cobertura del constructor por defecto.
        CarnavawikiappbackApplication app = new CarnavawikiappbackApplication();
        // No llamamos a main(args) para evitar levantar el servidor completo en un test unitario.
    }
}