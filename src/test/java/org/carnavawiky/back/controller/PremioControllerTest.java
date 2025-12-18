package org.carnavawiky.back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.carnavawiky.back.config.FileStorageProperties;
import org.carnavawiky.back.config.SecurityConfig;
import org.carnavawiky.back.config.WebConfig;
import org.carnavawiky.back.dto.PageResponse;
import org.carnavawiky.back.dto.PremioRequest;
import org.carnavawiky.back.dto.PremioResponse;
import org.carnavawiky.back.model.Modalidad;
import org.carnavawiky.back.repository.RoleRepository;
import org.carnavawiky.back.repository.UsuarioRepository;
import org.carnavawiky.back.security.JwtService;
import org.carnavawiky.back.service.PremioService;
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

@WebMvcTest(value = PremioController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {WebConfig.class})
})
@Import({SecurityConfig.class, JwtService.class})
class PremioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PremioService premioService;

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

    private PremioRequest premioRequest;
    private PremioResponse premioResponse;

    @BeforeEach
    void setUp() {
        premioRequest = new PremioRequest();
        premioRequest.setPuesto(1);
        premioRequest.setModalidad(Modalidad.COMPARSA);
        premioRequest.setAgrupacionId(10L);
        premioRequest.setEdicionId(20L);

        premioResponse = new PremioResponse();
        premioResponse.setId(1L);
        premioResponse.setPuesto(1);
        premioResponse.setAgrupacionNombre("La Eternidad");
    }

    // =======================================================
    // 1. CREAR (POST) - Solo ADMIN
    // =======================================================
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debe permitir a ADMIN crear un premio")
    void testCrearPremio_Admin_Ok() throws Exception {
        when(premioService.crearPremio(any(PremioRequest.class))).thenReturn(premioResponse);

        mockMvc.perform(post("/api/premios")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(premioRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Debe denegar creación a un USER")
    void testCrearPremio_User_Forbidden() throws Exception {
        mockMvc.perform(post("/api/premios")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(premioRequest)))
                .andExpect(status().isForbidden());
    }

    // =======================================================
    // 2. OBTENER TODOS (GET) - Abierto a USER/ADMIN
    // =======================================================
    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Debe listar premios paginados")
    void testListarPremios() throws Exception {
        PageResponse<PremioResponse> pageResponse = PageResponse.<PremioResponse>builder()
                .content(List.of(premioResponse))
                .totalElements(1L)
                .build();

        when(premioService.obtenerTodosPremios(any(Pageable.class), any())).thenReturn(pageResponse);

        mockMvc.perform(get("/api/premios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].agrupacionNombre").value("La Eternidad"));
    }

    // =======================================================
    // 3. OBTENER POR ID (GET)
    // =======================================================
    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Debe obtener un premio por su ID")
    void testObtenerPorId_Ok() throws Exception {
        when(premioService.obtenerPremioPorId(1L)).thenReturn(premioResponse);

        mockMvc.perform(get("/api/premios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    // =======================================================
    // 4. ACTUALIZAR (PUT) - Solo ADMIN
    // =======================================================
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debe permitir a ADMIN actualizar un premio")
    void testActualizarPremio_Ok() throws Exception {
        when(premioService.actualizarPremio(eq(1L), any(PremioRequest.class))).thenReturn(premioResponse);

        mockMvc.perform(put("/api/premios/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(premioRequest)))
                .andExpect(status().isOk());
    }

    // =======================================================
    // 5. ELIMINAR (DELETE) - Solo ADMIN
    // =======================================================
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debe permitir a ADMIN eliminar un premio")
    void testEliminarPremio_Ok() throws Exception {
        mockMvc.perform(delete("/api/premios/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    // =======================================================
    // 6. VALIDACIÓN (400 BAD REQUEST)
    // =======================================================
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debe devolver 400 si el request es inválido (puesto < 1)")
    void testCrearPremio_BadRequest() throws Exception {
        premioRequest.setPuesto(0); // Falla validación @Min(1)

        mockMvc.perform(post("/api/premios")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(premioRequest)))
                .andExpect(status().isBadRequest());
    }
}