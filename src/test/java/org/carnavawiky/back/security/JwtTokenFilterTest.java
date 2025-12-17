package org.carnavawiky.back.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private JwtTokenFilter jwtTokenFilter;

    @BeforeEach
    void setUp() {
        // Limpiamos el contexto de seguridad antes de cada test
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        // Limpiamos el contexto de seguridad después de cada test
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Debe continuar la cadena de filtros si no hay cabecera Authorization")
    void testDoFilterInternal_NoAuthHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("Debe continuar la cadena de filtros si la cabecera no empieza con Bearer")
    void testDoFilterInternal_NoBearerHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Basic dXNlcjpwYXNz");

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("Debe autenticar al usuario si el token es válido y no hay autenticación previa")
    void testDoFilterInternal_ValidToken() throws ServletException, IOException {
        // ARRANGE
        String token = "token.valido.jwt";
        String username = "usuarioPrueba";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.getUsernameFromToken(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.validateToken(token)).thenReturn(true);
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());

        // ACT
        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        // ASSERT
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
        verify(userDetailsService, times(1)).loadUserByUsername(username);
    }

    @Test
    @DisplayName("No debe re-autenticar si ya existe una autenticación en el contexto")
    void testDoFilterInternal_AlreadyAuthenticated() throws ServletException, IOException {
        // Simulamos que ya hay alguien autenticado (no nulo)
        SecurityContextHolder.getContext().setAuthentication(mock(org.springframework.security.authentication.UsernamePasswordAuthenticationToken.class));

        String token = "token.valido.jwt";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.getUsernameFromToken(token)).thenReturn("usuario");

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        // No debería llamar a loadUserByUsername porque ya hay autenticación
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("Debe capturar excepciones y continuar la cadena de filtros")
    void testDoFilterInternal_WithException() throws ServletException, IOException {
        String token = "token.corrupto";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.getUsernameFromToken(token)).thenThrow(new RuntimeException("Error de parseo"));

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        // A pesar del error, el filtro debe dejar pasar la petición (aunque sin autenticación)
        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}