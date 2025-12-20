package org.carnavawiky.back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.carnavawiky.back.config.FileStorageProperties;
import org.carnavawiky.back.config.SecurityConfig;
import org.carnavawiky.back.config.WebConfig;
import org.carnavawiky.back.dto.EdicionRequest;
import org.carnavawiky.back.dto.EdicionResponse;
import org.carnavawiky.back.dto.PageResponse;
import org.carnavawiky.back.repository.RoleRepository;
import org.carnavawiky.back.repository.UsuarioRepository;
import org.carnavawiky.back.security.JwtService;
import org.carnavawiky.back.service.EdicionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = EdicionController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class)
})
@Import({SecurityConfig.class, FileStorageProperties.class})
public class EdicionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EdicionService edicionService;

    // Mocks necesarios para el filtro de seguridad
    @MockBean
    private JwtService jwtService;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private UsuarioRepository usuarioRepository;
    @MockBean
    private RoleRepository roleRepository;

    private EdicionRequest edicionRequest;
    private EdicionResponse edicionResponse;

    @BeforeEach
    void setUp() {
        edicionRequest = new EdicionRequest();
        edicionRequest.setAnho(2024);
        edicionRequest.setConcursoId(1L);

        edicionResponse = new EdicionResponse();
        edicionResponse.setId(1L);
        edicionResponse.setAnho(2024);
        edicionResponse.setConcursoId(1L);
        edicionResponse.setConcursoNombre("COAC");
    }

    @Test
    @DisplayName("ADMIN puede crear una edición")
    @WithMockUser(roles = "ADMIN")
    void testCrearEdicion_Admin_Ok() throws Exception {
        when(edicionService.crearEdicion(any(EdicionRequest.class))).thenReturn(edicionResponse);

        mockMvc.perform(post("/api/ediciones")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(edicionRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.anho").value(2024))
                .andExpect(jsonPath("$.concursoNombre").value("COAC"));
    }

    @Test
    @DisplayName("USER no puede crear una edición")
    @WithMockUser(roles = "USER")
    void testCrearEdicion_User_Forbidden() throws Exception {
        mockMvc.perform(post("/api/ediciones")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(edicionRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Debe obtener todas las ediciones paginadas")
    @WithMockUser(roles = "USER")
    void testObtenerTodas_Ok() throws Exception {
        // ARRANGE
        PageResponse<EdicionResponse> pageResponse = PageResponse.<EdicionResponse>builder()
                .content(List.of(edicionResponse))
                .totalElements(1L)
                .build();

        when(edicionService.obtenerTodasEdiciones(any(Pageable.class), any()))
                .thenReturn(pageResponse);

        // ACT & ASSERT
        mockMvc.perform(get("/api/ediciones")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].anho").value(2024));
    }

    @Test
    @DisplayName("Debe obtener una edición por ID")
    @WithMockUser(roles = "USER")
    void testObtenerPorId_Ok() throws Exception {
        when(edicionService.obtenerEdicionPorId(1L)).thenReturn(edicionResponse);

        mockMvc.perform(get("/api/ediciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("ADMIN puede actualizar una edición")
    @WithMockUser(roles = "ADMIN")
    void testActualizarEdicion_Admin_Ok() throws Exception {
        when(edicionService.actualizarEdicion(eq(1L), any(EdicionRequest.class))).thenReturn(edicionResponse);

        mockMvc.perform(put("/api/ediciones/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(edicionRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("ADMIN puede eliminar una edición")
    @WithMockUser(roles = "ADMIN")
    void testEliminarEdicion_Admin_Ok() throws Exception {
        doNothing().when(edicionService).eliminarEdicion(1L);

        mockMvc.perform(delete("/api/ediciones/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(edicionService, times(1)).eliminarEdicion(1L);
    }
}