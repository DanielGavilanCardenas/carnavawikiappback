package org.carnavawiky.back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.carnavawiky.back.config.FileStorageProperties;
import org.carnavawiky.back.config.SecurityConfig;
import org.carnavawiky.back.config.WebConfig;
import org.carnavawiky.back.dto.ComponenteRequest;
import org.carnavawiky.back.dto.ComponenteResponse;
import org.carnavawiky.back.dto.PageResponse;
import org.carnavawiky.back.model.RolComponente;
import org.carnavawiky.back.repository.RoleRepository;
import org.carnavawiky.back.repository.UsuarioRepository;
import org.carnavawiky.back.security.JwtService;
import org.carnavawiky.back.service.ComponenteService;
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

@WebMvcTest(value = ComponenteController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {WebConfig.class})
})
@Import({SecurityConfig.class, JwtService.class})
class ComponenteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ComponenteService componenteService;

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

    private ComponenteRequest componenteRequest;
    private ComponenteResponse componenteResponse;

    @BeforeEach
    void setUp() {
        componenteRequest = new ComponenteRequest();
        componenteRequest.setRol(RolComponente.DIRECTOR);
        componenteRequest.setPersonaId(100L);
        componenteRequest.setAgrupacionId(50L);

        componenteResponse = new ComponenteResponse();
        componenteResponse.setId(1L);
        componenteResponse.setRol(RolComponente.DIRECTOR);
        componenteResponse.setNombreReal("Juan Carlos Aragón");
        componenteResponse.setAgrupacionNombre("Los Yesterday");
    }

    // =======================================================
    // 1. CREAR (POST) - Solo ADMIN
    // =======================================================
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debe permitir a ADMIN crear un componente")
    void testCrearComponente_Admin_Ok() throws Exception {
        when(componenteService.crearComponente(any(ComponenteRequest.class))).thenReturn(componenteResponse);

        mockMvc.perform(post("/api/componentes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(componenteRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.rol").value("DIRECTOR"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Debe denegar creación a un USER")
    void testCrearComponente_User_Forbidden() throws Exception {
        mockMvc.perform(post("/api/componentes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(componenteRequest)))
                .andExpect(status().isForbidden());
    }

    // =======================================================
    // 2. OBTENER TODOS (GET) - Abierto a USER/ADMIN
    // =======================================================
    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Debe listar componentes paginados")
    void testListarComponentes() throws Exception {
        PageResponse<ComponenteResponse> pageResponse = PageResponse.<ComponenteResponse>builder()
                .content(List.of(componenteResponse))
                .totalElements(1L)
                .build();

        when(componenteService.obtenerTodosComponentes(any(Pageable.class), any())).thenReturn(pageResponse);

        mockMvc.perform(get("/api/componentes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nombreReal").value("Juan Carlos Aragón"));
    }

    // =======================================================
    // 3. OBTENER POR ID (GET)
    // =======================================================
    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Debe obtener un componente por su ID")
    void testObtenerPorId_Ok() throws Exception {
        when(componenteService.obtenerComponentePorId(1L)).thenReturn(componenteResponse);

        mockMvc.perform(get("/api/componentes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    // =======================================================
    // 4. ACTUALIZAR (PUT) - Solo ADMIN
    // =======================================================
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debe permitir a ADMIN actualizar un componente")
    void testActualizarComponente_Ok() throws Exception {
        when(componenteService.actualizarComponente(eq(1L), any(ComponenteRequest.class))).thenReturn(componenteResponse);

        mockMvc.perform(put("/api/componentes/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(componenteRequest)))
                .andExpect(status().isOk());
    }

    // =======================================================
    // 5. ELIMINAR (DELETE) - Solo ADMIN
    // =======================================================
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debe permitir a ADMIN eliminar un componente")
    void testEliminarComponente_Ok() throws Exception {
        mockMvc.perform(delete("/api/componentes/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    // =======================================================
    // 6. VALIDACIÓN (400 BAD REQUEST)
    // =======================================================
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debe devolver 400 si el request es inválido (rol nulo)")
    void testCrearComponente_BadRequest() throws Exception {
        componenteRequest.setRol(null); // Falla validación @NotNull

        mockMvc.perform(post("/api/componentes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(componenteRequest)))
                .andExpect(status().isBadRequest());
    }
}