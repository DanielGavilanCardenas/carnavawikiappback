package org.carnavawiky.back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.carnavawiky.back.config.FileStorageProperties;
import org.carnavawiky.back.config.SecurityConfig;
import org.carnavawiky.back.config.WebConfig;
import org.carnavawiky.back.dto.ConcursoRequest;
import org.carnavawiky.back.dto.ConcursoResponse;
import org.carnavawiky.back.dto.PageResponse;
import org.carnavawiky.back.repository.RoleRepository;
import org.carnavawiky.back.repository.UsuarioRepository;
import org.carnavawiky.back.security.JwtService;
import org.carnavawiky.back.service.ConcursoService;
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

@WebMvcTest(value = ConcursoController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class)
})
@Import({SecurityConfig.class, JwtService.class})
public class ConcursoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ConcursoService concursoService;

    // Mocks de infraestructura de seguridad
    @MockBean
    private FileStorageProperties fileStorageProperties;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private UsuarioRepository usuarioRepository;
    @MockBean
    private RoleRepository roleRepository;

    private ConcursoRequest concursoRequest;
    private ConcursoResponse concursoResponse;

    @BeforeEach
    void setUp() {
        concursoRequest = new ConcursoRequest();
        concursoRequest.setNombre("COAC");
        concursoRequest.setLocalidadId(1L);
        concursoRequest.setEstaActivo(true);

        concursoResponse = new ConcursoResponse();
        concursoResponse.setId(1L);
        concursoResponse.setNombre("COAC");
        concursoResponse.setLocalidadNombre("Cádiz");
        concursoResponse.setEstaActivo(true);
    }

    // =======================================================
    // 1. CREAR (POST)
    // =======================================================
    @Test
    @DisplayName("ADMIN puede crear un concurso")
    @WithMockUser(roles = "ADMIN")
    void testCrearConcurso_Admin_Ok() throws Exception {
        when(concursoService.crearConcurso(any(ConcursoRequest.class))).thenReturn(concursoResponse);

        mockMvc.perform(post("/api/concursos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(concursoRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("COAC"));
    }

    @Test
    @DisplayName("USER no puede crear un concurso")
    @WithMockUser(roles = "USER")
    void testCrearConcurso_User_Forbidden() throws Exception {
        mockMvc.perform(post("/api/concursos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(concursoRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Debe devolver 400 si el request es inválido (nombre vacío)")
    @WithMockUser(roles = "ADMIN")
    void testCrearConcurso_BadRequest() throws Exception {
        concursoRequest.setNombre(""); // Falla validación @NotBlank

        mockMvc.perform(post("/api/concursos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(concursoRequest)))
                .andExpect(status().isBadRequest());
    }

    // =======================================================
    // 2. OBTENER TODOS (GET)
    // =======================================================
    @Test
    @DisplayName("Obtener todos los concursos con paginación y búsqueda")
    @WithMockUser(roles = "USER")
    void testObtenerTodos_Ok() throws Exception {
        PageResponse<ConcursoResponse> pageResponse = PageResponse.<ConcursoResponse>builder()
                .content(List.of(concursoResponse))
                .totalElements(1L)
                .build();

        when(concursoService.obtenerTodosConcursos(any(Pageable.class), any()))
                .thenReturn(pageResponse);

        mockMvc.perform(get("/api/concursos")
                        .param("page", "0")
                        .param("size", "10")
                        .param("search", "COAC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nombre").value("COAC"));
    }

    // =======================================================
    // 3. OBTENER POR ID (GET)
    // =======================================================
    @Test
    @DisplayName("Obtener concurso por ID")
    @WithMockUser(roles = "USER")
    void testObtenerPorId_Ok() throws Exception {
        when(concursoService.obtenerConcursoPorId(1L)).thenReturn(concursoResponse);

        mockMvc.perform(get("/api/concursos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("COAC"));
    }

    // =======================================================
    // 4. ACTUALIZAR (PUT)
    // =======================================================
    @Test
    @DisplayName("ADMIN puede actualizar un concurso")
    @WithMockUser(roles = "ADMIN")
    void testActualizarConcurso_Admin_Ok() throws Exception {
        when(concursoService.actualizarConcurso(eq(1L), any(ConcursoRequest.class))).thenReturn(concursoResponse);

        mockMvc.perform(put("/api/concursos/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(concursoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("COAC"));
    }

    @Test
    @DisplayName("USER no puede actualizar un concurso")
    @WithMockUser(roles = "USER")
    void testActualizarConcurso_User_Forbidden() throws Exception {
        mockMvc.perform(put("/api/concursos/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(concursoRequest)))
                .andExpect(status().isForbidden());
    }

    // =======================================================
    // 5. ELIMINAR (DELETE)
    // =======================================================
    @Test
    @DisplayName("ADMIN puede eliminar un concurso")
    @WithMockUser(roles = "ADMIN")
    void testEliminarConcurso_Admin_Ok() throws Exception {
        doNothing().when(concursoService).eliminarConcurso(1L);

        mockMvc.perform(delete("/api/concursos/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(concursoService, times(1)).eliminarConcurso(1L);
    }

    @Test
    @DisplayName("USER no puede eliminar un concurso")
    @WithMockUser(roles = "USER")
    void testEliminarConcurso_User_Forbidden() throws Exception {
        mockMvc.perform(delete("/api/concursos/1")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }
}