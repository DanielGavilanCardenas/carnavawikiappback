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
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    private static final String ADMIN = "ADMIN";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // 1. Endpoints públicos de Auth y Swagger
                        .requestMatchers("/api/auth/**", "/v3/api-docs/**", "/swagger-ui/**").permitAll()

                        // 2. TEXTOS: Acceso público para lectura (Soluciona tu error 403)
                        .requestMatchers(HttpMethod.GET, "/api/textos/**").permitAll()
                        .requestMatchers("/api/textos/**").hasRole(ADMIN)

                        // 3. IMÁGENES: Acceso público para verlas (GET)
                        .requestMatchers(HttpMethod.GET, "/api/imagenes/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/imagenes/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/imagenes/**").permitAll()

                        // 4. AGRUPACIONES y CATEGORÍAS: Lectura pública
                        .requestMatchers(HttpMethod.GET, "/api/agrupaciones/**", "/api/categorias/**").permitAll()
                        .requestMatchers("/api/agrupaciones/**", "/api/categorias/**").hasRole(ADMIN)

                        // 5. Roles específicos
                        .requestMatchers("/api/admin/**").hasRole(ADMIN)
                        .requestMatchers("/api/especialisto/**").hasAnyRole(ADMIN, "ESPECIALISTO")

                        // 6. Cualquier otra petición requiere login
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // Mantenemos los orígenes permitidos, incluyendo tu frontend Angular (4200)
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://localhost:8081", "http://localhost:8083"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}