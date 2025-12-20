package org.carnavawiky.back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.carnavawiky.back.config.FileStorageProperties;
import org.carnavawiky.back.config.SecurityConfig;
import org.carnavawiky.back.config.WebConfig;
import org.carnavawiky.back.dto.AgrupacionRequest;
import org.carnavawiky.back.dto.AgrupacionResponse;
import org.carnavawiky.back.dto.PageResponse;
import org.carnavawiky.back.model.Modalidad;
import org.carnavawiky.back.repository.RoleRepository;
import org.carnavawiky.back.repository.UsuarioRepository;
import org.carnavawiky.back.security.JwtService;
import org.carnavawiky.back.service.AgrupacionService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = AgrupacionController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {WebConfig.class})
})
@Import({SecurityConfig.class, JwtService.class})
class AgrupacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AgrupacionService agrupacionService;

    // Mocks necesarios para el contexto de seguridad y configuración
    @MockBean
    private FileStorageProperties fileStorageProperties;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private AgrupacionRequest agrupacionRequest;
    private AgrupacionResponse agrupacionResponse;

    @BeforeEach
    void setUp() {
        agrupacionRequest = new AgrupacionRequest();
        agrupacionRequest.setNombre("Los Yesterday");
        agrupacionRequest.setDescripcion("Chirigota mítica");
        agrupacionRequest.setModalidad(Modalidad.CHIRIGOTA);
        agrupacionRequest.setAnho(1999);
        agrupacionRequest.setLocalidadId(50L);

        agrupacionResponse = new AgrupacionResponse();
        agrupacionResponse.setId(1L);
        agrupacionResponse.setNombre("Los Yesterday");
        agrupacionResponse.setModalidad(Modalidad.CHIRIGOTA);
        agrupacionResponse.setAnho(1999);
        agrupacionResponse.setLocalidadNombre("Cádiz");
    }

    // =======================================================
    // 1. CREAR (POST) - Solo ADMIN
    // =======================================================
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debe permitir a ADMIN crear una agrupación")
    void testCrearAgrupacion_Admin_Ok() throws Exception {
        when(agrupacionService.crearAgrupacion(any(AgrupacionRequest.class))).thenReturn(agrupacionResponse);

        mockMvc.perform(post("/api/agrupaciones")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agrupacionRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Los Yesterday"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Debe denegar creación a un USER")
    void testCrearAgrupacion_User_Forbidden() throws Exception {
        mockMvc.perform(post("/api/agrupaciones")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agrupacionRequest)))
                .andExpect(status().isForbidden());
    }

    // =======================================================
    // 2. OBTENER TODAS (GET) - Abierto a USER/ADMIN
    // =======================================================
    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Debe listar agrupaciones paginadas")
    void testListarAgrupaciones() throws Exception {
        PageResponse<AgrupacionResponse> pageResponse = PageResponse.<AgrupacionResponse>builder()
                .content(List.of(agrupacionResponse))
                .totalElements(1L)
                .build();

        when(agrupacionService.obtenerTodasAgrupaciones(any(Pageable.class), any())).thenReturn(pageResponse);

        mockMvc.perform(get("/api/agrupaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nombre").value("Los Yesterday"));
    }

    // =======================================================
    // 3. OBTENER POR ID (GET)
    // =======================================================
    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Debe obtener una agrupación por su ID")
    void testObtenerPorId_Ok() throws Exception {
        when(agrupacionService.obtenerAgrupacionPorId(1L)).thenReturn(agrupacionResponse);

        mockMvc.perform(get("/api/agrupaciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    // =======================================================
    // 4. ACTUALIZAR (PUT) - Solo ADMIN
    // =======================================================
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debe permitir a ADMIN actualizar una agrupación")
    void testActualizarAgrupacion_Ok() throws Exception {
        when(agrupacionService.actualizarAgrupacion(eq(1L), any(AgrupacionRequest.class))).thenReturn(agrupacionResponse);

        mockMvc.perform(put("/api/agrupaciones/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agrupacionRequest)))
                .andExpect(status().isOk());
    }

    // =======================================================
    // 5. ELIMINAR (DELETE) - Solo ADMIN
    // =======================================================
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debe permitir a ADMIN eliminar una agrupación")
    void testEliminarAgrupacion_Ok() throws Exception {
        mockMvc.perform(delete("/api/agrupaciones/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    // =======================================================
    // 6. VALIDACIÓN (400 BAD REQUEST)
    // =======================================================
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debe devolver 400 si el request es inválido (nombre vacío)")
    void testCrearAgrupacion_BadRequest() throws Exception {
        agrupacionRequest.setNombre(""); // Falla validación @NotBlank

        mockMvc.perform(post("/api/agrupaciones")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agrupacionRequest)))
                .andExpect(status().isBadRequest());
    }
}