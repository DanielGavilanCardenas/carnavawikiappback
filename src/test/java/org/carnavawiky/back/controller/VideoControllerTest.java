package org.carnavawiky.back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.carnavawiky.back.config.FileStorageProperties;
import org.carnavawiky.back.config.SecurityConfig;
import org.carnavawiky.back.config.WebConfig;
import org.carnavawiky.back.dto.VideoRequest;
import org.carnavawiky.back.dto.VideoResponse;
import org.carnavawiky.back.repository.RoleRepository;
import org.carnavawiky.back.repository.UsuarioRepository;
import org.carnavawiky.back.security.JwtService;
import org.carnavawiky.back.service.VideoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = VideoController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {WebConfig.class})
})
@Import({SecurityConfig.class, JwtService.class})
class VideoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VideoService videoService;

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

    private VideoRequest videoRequest;
    private VideoResponse videoResponse;

    @BeforeEach
    void setUp() {
        videoRequest = new VideoRequest();
        videoRequest.setTitulo("Actuación Final");
        videoRequest.setUrlYoutube("https://youtube.com/watch?v=12345");
        videoRequest.setAgrupacionId(10L);

        videoResponse = new VideoResponse();
        videoResponse.setId(1L);
        videoResponse.setTitulo("Actuación Final");
        videoResponse.setUrlYoutube("https://youtube.com/watch?v=12345");
        videoResponse.setVerificado(false);
        videoResponse.setAgrupacionId(10L);
    }

    // =======================================================
    // 1. GET PUBLIC VIDEOS
    // =======================================================
    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Debe listar vídeos verificados (público)")
    void testGetPublicVideos() throws Exception {
        when(videoService.listarVerificados()).thenReturn(List.of(videoResponse));

        mockMvc.perform(get("/api/videos/public"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Actuación Final"));
    }

    // =======================================================
    // 2. CREATE VIDEO (POST) - Solo ESPECIALISTO
    // =======================================================
    @Test
    @WithMockUser(roles = "ESPECIALISTO")
    @DisplayName("Debe permitir a ESPECIALISTO crear un vídeo")
    void testCreateVideo_Admin_Ok() throws Exception {
        when(videoService.guardar(any(VideoRequest.class))).thenReturn(videoResponse);

        mockMvc.perform(post("/api/videos/admin")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(videoRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.titulo").value("Actuación Final"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Debe denegar creación a un USER")
    void testCreateVideo_User_Forbidden() throws Exception {
        mockMvc.perform(post("/api/videos/admin")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(videoRequest)))
                .andExpect(status().isForbidden());
    }

    // =======================================================
    // 3. VERIFY VIDEO (PUT) - Solo ESPECIALISTO
    // =======================================================
    @Test
    @WithMockUser(roles = "ESPECIALISTO")
    @DisplayName("Debe permitir a ESPECIALISTO verificar un vídeo")
    void testVerifyVideo_Admin_Ok() throws Exception {
        videoResponse.setVerificado(true);
        when(videoService.verificarVideo(1L)).thenReturn(videoResponse);

        mockMvc.perform(put("/api/videos/admin/verificar/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.verificado").value(true));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Debe denegar verificación a un USER")
    void testVerifyVideo_User_Forbidden() throws Exception {
        mockMvc.perform(put("/api/videos/admin/verificar/1")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    // =======================================================
    // 4. DELETE VIDEO (DELETE) - Solo ESPECIALISTO
    // =======================================================
    @Test
    @WithMockUser(roles = "ESPECIALISTO")
    @DisplayName("Debe permitir a ESPECIALISTO eliminar un vídeo")
    void testDeleteVideo_Admin_Ok() throws Exception {
        doNothing().when(videoService).eliminar(1L);

        mockMvc.perform(delete("/api/videos/admin/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Debe denegar eliminación a un USER")
    void testDeleteVideo_User_Forbidden() throws Exception {
        mockMvc.perform(delete("/api/videos/admin/1")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    // =======================================================
    // 5. VALIDACIÓN (400 BAD REQUEST)
    // =======================================================
    @Test
    @WithMockUser(roles = "ESPECIALISTO")
    @DisplayName("Debe devolver 400 si el request es inválido (título vacío)")
    void testCreateVideo_BadRequest() throws Exception {
        videoRequest.setTitulo(""); // Falla validación @NotBlank

        mockMvc.perform(post("/api/videos/admin")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(videoRequest)))
                .andExpect(status().isBadRequest());
    }
}
