package org.carnavawiky.back.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestResponseLoggingFilterTest {

    @InjectMocks
    private RequestResponseLoggingFilter filter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @Test
    @DisplayName("Debe ejecutar el filtro y continuar la cadena")
    void testDoFilter() throws ServletException, IOException {
        // ARRANGE
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/test");
        when(response.getStatus()).thenReturn(200);

        // ACT
        filter.doFilter(request, response, chain);

        // ASSERT
        // Verificamos que la cadena de filtros continúa
        verify(chain, times(1)).doFilter(request, response);

        // Verificamos que se accede a los datos para el log
        verify(request, atLeastOnce()).getMethod();
        verify(request, atLeastOnce()).getRequestURI();
        verify(response, atLeastOnce()).getStatus();
    }
}
