package org.carnavawiky.back.config;

import org.carnavawiky.back.security.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // 💡 CRÍTICO: Habilita el uso de @PreAuthorize
public class SecurityConfig {

    public static final String ADMIN = "ADMIN";
    @Autowired
    private JwtTokenFilter jwtTokenFilter; // El filtro que valida el JWT

    // 1. Cifrado de Contraseña (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Define el AuthenticationManager (Necesario para el login)
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService); // Nuestro CustomUserDetailsService
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    // 3. Reglas de Acceso por Endpoints (SecurityFilterChain)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitar CSRF para APIs REST
                .csrf(AbstractHttpConfigurer::disable)

                // Define las reglas de autorización (Acceso a Endpoints)
                .authorizeHttpRequests(auth -> auth

                        // 0. public health
                        .requestMatchers("/api/public/health").permitAll() // Permitir acceso sin login
                        .anyRequest().authenticated()

                        // 1. Rutas de Swagger/OpenAPI (PÚBLICAS)
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // 2. Endpoints de autenticación (Registro, Login) (PÚBLICOS)
                        .requestMatchers("/api/auth/**").permitAll()

                        // 3. PROTECCIÓN DE LOCALIDADES (POST/PUT/DELETE)
                        // POST (Crear): Solo ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/localidades").hasRole(ADMIN)
                        // PUT (Actualizar): Solo ADMIN
                        .requestMatchers(HttpMethod.PUT, "/api/localidades/**").hasRole(ADMIN)
                        // DELETE (Eliminar): Solo ADMIN
                        .requestMatchers(HttpMethod.DELETE, "/api/localidades/**").hasRole(ADMIN)

                        // 4. Endpoints protegidos por Roles generales
                        .requestMatchers("/api/admin/**").hasRole(ADMIN)
                        .requestMatchers("/api/especialisto/**").hasAnyRole(ADMIN, "ESPECIALISTO")

                        // 5. Resto de endpoints (incluyendo GET), requiere autenticación (JWT Válido)
                        .anyRequest().authenticated()
                )

                // 6. Configuración de Sesión (Sin estado/Stateless, crucial para JWT)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 7. AÑADIR NUESTRO FILTRO PERSONALIZADO JWT
                // Se ejecuta ANTES del filtro de autenticación por usuario/contraseña de Spring
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    //

    // 8. Configuración CORS (Necesario para la comunicación con Angular)
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // Configuración para permitir orígenes, métodos y headers
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://localhost:8081", "http://localhost:8083"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}