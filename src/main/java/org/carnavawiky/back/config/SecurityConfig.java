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
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    private static final String ADMIN = "ADMIN";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // 1. Endpoints Públicos de Salud y Documentación
                        .requestMatchers("/api/public/health").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // 2. Autenticación y Registro
                        .requestMatchers("/api/auth/**").permitAll()

                        // 3. TEXTOS: Acceso público para lectura (imprescindible para carga inicial)
                        .requestMatchers(HttpMethod.GET, "/api/textos/**").permitAll()
                        // Solo ADMIN puede gestionar los textos (CRUD)
                        .requestMatchers(HttpMethod.POST, "/api/textos/**").hasRole(ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/api/textos/**").hasRole(ADMIN)
                        .requestMatchers(HttpMethod.PATCH, "/api/textos/**").hasRole(ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/api/textos/**").hasRole(ADMIN)

                        // 4. Localidades
                        .requestMatchers(HttpMethod.POST, "/api/localidades").hasRole(ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/api/localidades/**").hasRole(ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/api/localidades/**").hasRole(ADMIN)

                        // 5. Vídeos y otros contenidos
                        .requestMatchers("/api/videos/public").permitAll()
                        .requestMatchers("/api/videos/admin/**").hasAnyRole(ADMIN, "ESPECIALISTO")
                        .requestMatchers(HttpMethod.DELETE, "/api/imagenes/**").hasRole(ADMIN)

                        // 6. Admin general
                        .requestMatchers("/api/admin/**").hasRole(ADMIN)
                        .requestMatchers("/api/especialisto/**").hasAnyRole(ADMIN, "ESPECIALISTO")

                        // 7. Cualquier otra petición requiere login
                        .anyRequest().authenticated()
                )
                // Configuración Stateless para JWT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Filtro JWT antes del filtro de autenticación estándar
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // Permitimos los orígenes habituales de desarrollo
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://localhost:8081", "http://localhost:8083"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}