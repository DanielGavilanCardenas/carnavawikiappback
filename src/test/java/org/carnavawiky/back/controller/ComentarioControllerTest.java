package org.carnavawiky.back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.carnavawiky.back.config.FileStorageProperties;
import org.carnavawiky.back.config.SecurityConfig;
import org.carnavawiky.back.config.WebConfig;
import org.carnavawiky.back.dto.ComentarioRequest;
import org.carnavawiky.back.dto.ComentarioResponse;
import org.carnavawiky.back.dto.PageResponse;
import org.carnavawiky.back.repository.RoleRepository;
import org.carnavawiky.back.repository.UsuarioRepository;
import org.carnavawiky.back.security.JwtService;
import org.carnavawiky.back.service.ComentarioService;
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

@WebMvcTest(value = ComentarioController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {WebConfig.class})
})
@Import({SecurityConfig.class, JwtService.class})
class ComentarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ComentarioService comentarioService;

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

    private ComentarioRequest comentarioRequest;
    private ComentarioResponse comentarioResponse;

    @BeforeEach
    void setUp() {
        comentarioRequest = new ComentarioRequest();
        comentarioRequest.setContenido("¡Qué gran actuación!");
        comentarioRequest.setPuntuacion(5);
        comentarioRequest.setAgrupacionId(50L);
        comentarioRequest.setUsuarioId(10L);

        comentarioResponse = new ComentarioResponse();
        comentarioResponse.setId(1L);
        comentarioResponse.setContenido("¡Qué gran actuación!");
        comentarioResponse.setPuntuacion(5);
        comentarioResponse.setUsuarioUsername("carnavalero_pro");
    }

    // =======================================================
    // 1. CREAR (POST) - Abierto a USER
    // =======================================================
    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Debe permitir a USER crear un comentario")
    void testCrearComentario_User_Ok() throws Exception {
        when(comentarioService.crearComentario(any(ComentarioRequest.class))).thenReturn(comentarioResponse);

        mockMvc.perform(post("/api/comentarios")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comentarioRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.contenido").value("¡Qué gran actuación!"));
    }

    // =======================================================
    // 2. OBTENER TODOS (GET) - Abierto a USER/ADMIN
    // =======================================================
    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Debe listar comentarios paginados")
    void testListarComentarios() throws Exception {
        PageResponse<ComentarioResponse> pageResponse = PageResponse.<ComentarioResponse>builder()
                .content(List.of(comentarioResponse))
                .totalElements(1L)
                .build();

        when(comentarioService.obtenerTodosComentarios(any(Pageable.class), any())).thenReturn(pageResponse);

        mockMvc.perform(get("/api/comentarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].contenido").value("¡Qué gran actuación!"));
    }

    // =======================================================
    // 3. OBTENER POR ID (GET)
    // =======================================================
    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Debe obtener un comentario por su ID")
    void testObtenerPorId_Ok() throws Exception {
        when(comentarioService.obtenerComentarioPorId(1L)).thenReturn(comentarioResponse);

        mockMvc.perform(get("/api/comentarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    // =======================================================
    // 4. ACTUALIZAR (PUT) - Solo ADMIN
    // =======================================================
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debe permitir a ADMIN actualizar un comentario")
    void testActualizarComentario_Admin_Ok() throws Exception {
        when(comentarioService.actualizarComentario(eq(1L), any(ComentarioRequest.class))).thenReturn(comentarioResponse);

        mockMvc.perform(put("/api/comentarios/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comentarioRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Debe denegar actualización a un USER")
    void testActualizarComentario_User_Forbidden() throws Exception {
        mockMvc.perform(put("/api/comentarios/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comentarioRequest)))
                .andExpect(status().isForbidden());
    }

    // =======================================================
    // 5. ELIMINAR (DELETE) - Solo ADMIN
    // =======================================================
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debe permitir a ADMIN eliminar un comentario")
    void testEliminarComentario_Ok() throws Exception {
        mockMvc.perform(delete("/api/comentarios/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    // =======================================================
    // 6. APROBAR (PUT /aprobar) - Solo ADMIN
    // =======================================================
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debe permitir a ADMIN aprobar un comentario")
    void testAprobarComentario_Ok() throws Exception {
        when(comentarioService.aprobarComentario(1L)).thenReturn(comentarioResponse);

        mockMvc.perform(put("/api/comentarios/1/aprobar")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Debe denegar aprobación a un USER")
    void testAprobarComentario_User_Forbidden() throws Exception {
        mockMvc.perform(put("/api/comentarios/1/aprobar")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    // =======================================================
    // 7. VALIDACIÓN (400 BAD REQUEST)
    // =======================================================
    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Debe devolver 400 si el request es inválido (contenido vacío)")
    void testCrearComentario_BadRequest() throws Exception {
        comentarioRequest.setContenido(""); // Falla validación @NotBlank

        mockMvc.perform(post("/api/comentarios")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comentarioRequest)))
                .andExpect(status().isBadRequest());
    }
}