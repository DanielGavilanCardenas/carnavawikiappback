package org.carnavawiky.back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.carnavawiky.back.config.FileStorageProperties;
import org.carnavawiky.back.config.SecurityConfig;
import org.carnavawiky.back.security.JwtService;
import org.carnavawiky.back.dto.LocalidadRequest;
import org.carnavawiky.back.dto.LocalidadResponse;
import org.carnavawiky.back.dto.PageResponse;
import org.carnavawiky.back.exception.ResourceNotFoundException;
import org.carnavawiky.back.repository.RoleRepository;
import org.carnavawiky.back.repository.UsuarioRepository;
import org.carnavawiky.back.service.LocalidadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import; // <--- Importación necesaria para la corrección
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService; // <--- Importación necesaria
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.carnavawiky.back.config.WebConfig;

import javax.sql.DataSource;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

/**
 * Pruebas de Integración Slice para LocalidadController.
 */
@Import(SecurityConfig.class) // <--- CORRECCIÓN 1: Sustitución de imports por @Import
@WebMvcTest(
        controllers = LocalidadController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class),
        properties = {
                "file.upload.location=test-uploads",
                "app.security.seed-enabled=false" // Desactiva el Data Seeder
        }
)
public class LocalidadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Dependencia principal del Controller
    @MockBean
    private LocalidadService localidadService;

    // =======================================================
    // Mocks requeridos por el contexto de Spring Boot para iniciar:
    // =======================================================

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private FileStorageProperties fileStorageProperties;

    @MockBean
    private JwtService jwtService;

    // <--- CORRECCIÓN 2: Añadir MockBean para UserDetailsService para satisfacer el JwtTokenFilter
    @MockBean
    private UserDetailsService userDetailsService;

    // Opcional, para evitar que Spring intente inicializar la DB:
    @MockBean
    private DataSource dataSource;

    // =======================================================
    // Datos de Prueba (fixtures)
    // =======================================================
    private LocalidadRequest validRequest;
    private LocalidadResponse validResponse;
    private final Long LOCALIDAD_ID = 1L;
    private final String API_BASE = "/api/localidades";

    @BeforeEach
    void setUp() {
        validRequest = new LocalidadRequest();
        validRequest.setNombre("Cádiz");

        validResponse = new LocalidadResponse();
        validResponse.setId(LOCALIDAD_ID);
        validResponse.setNombre("Cádiz");
    }

    // =======================================================
    // 1. PRUEBAS PARA CREAR (POST /api/localidades) - Solo ADMIN
    // =======================================================

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCrearLocalidad_Admin_DebeDevolver201Created() throws Exception {
        // ARRANGE
        when(localidadService.crearLocalidad(any(LocalidadRequest.class))).thenReturn(validResponse);

        // ACT & ASSERT
        mockMvc.perform(post(API_BASE)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(LOCALIDAD_ID.intValue())))
                .andExpect(jsonPath("$.nombre", is("Cádiz")));

        verify(localidadService, times(1)).crearLocalidad(any(LocalidadRequest.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testCrearLocalidad_UserSinPermiso_DebeDevolver403Forbidden() throws Exception {
        // ACT & ASSERT
        mockMvc.perform(post(API_BASE)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isForbidden());

        verify(localidadService, never()).crearLocalidad(any(LocalidadRequest.class));
    }


    // =======================================================
    // 2. PRUEBAS PARA OBTENER TODAS (GET /api/localidades) - USER y ADMIN
    // =======================================================

    @Test
    @WithMockUser(roles = "USER")
    void testObtenerTodas_DebeDevolver200Ok() throws Exception {
        // ARRANGE
        PageResponse<LocalidadResponse> pageResponse = PageResponse.<LocalidadResponse>builder()
                .content(Collections.singletonList(validResponse))
                .totalElements(1L)
                .totalPages(1)
                .build();

        when(localidadService.obtenerTodasLocalidades(any(), eq(null))).thenReturn(pageResponse);

        // ACT & ASSERT
        mockMvc.perform(get(API_BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(1)));

        verify(localidadService, times(1)).obtenerTodasLocalidades(any(), eq(null));
    }

    // =======================================================
    // 3. PRUEBAS PARA OBTENER POR ID (GET /api/localidades/{id}) - USER y ADMIN
    // =======================================================

    @Test
    @WithMockUser(roles = "USER")
    void testObtenerPorId_Existente_DebeDevolver200Ok() throws Exception {
        // ARRANGE
        when(localidadService.obtenerLocalidadPorId(LOCALIDAD_ID)).thenReturn(validResponse);

        // ACT & ASSERT
        mockMvc.perform(get(API_BASE + "/{id}", LOCALIDAD_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Cádiz")));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testObtenerPorId_NoExistente_DebeDevolver404NotFound() throws Exception {
        // ARRANGE: Simular la excepción de negocio del Service
        when(localidadService.obtenerLocalidadPorId(LOCALIDAD_ID))
                .thenThrow(new ResourceNotFoundException("Localidad", "id", LOCALIDAD_ID));

        // ACT & ASSERT
        mockMvc.perform(get(API_BASE + "/{id}", LOCALIDAD_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // =======================================================
    // 4. PRUEBAS PARA ACTUALIZAR (PUT /api/localidades/{id}) - Solo ADMIN
    // =======================================================

    @Test
    @WithMockUser(roles = "ADMIN")
    void testActualizarLocalidad_Admin_DebeDevolver200Ok() throws Exception {
        // ARRANGE
        when(localidadService.actualizarLocalidad(eq(LOCALIDAD_ID), any(LocalidadRequest.class)))
                .thenReturn(validResponse);

        // ACT & ASSERT
        mockMvc.perform(put(API_BASE + "/{id}", LOCALIDAD_ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Cádiz")));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testActualizarLocalidad_UserSinPermiso_DebeDevolver403Forbidden() throws Exception {
        // ACT & ASSERT
        mockMvc.perform(put(API_BASE + "/{id}", LOCALIDAD_ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isForbidden());

        verify(localidadService, never()).actualizarLocalidad(anyLong(), any());
    }

    // =======================================================
    // 5. PRUEBAS PARA ELIMINAR (DELETE /api/localidades/{id}) - Solo ADMIN
    // =======================================================

    @Test
    @WithMockUser(roles = "ADMIN")
    void testEliminarLocalidad_Admin_DebeDevolver204NoContent() throws Exception {
        // ARRANGE
        doNothing().when(localidadService).eliminarLocalidad(LOCALIDAD_ID);

        // ACT & ASSERT
        mockMvc.perform(delete(API_BASE + "/{id}", LOCALIDAD_ID)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(localidadService, times(1)).eliminarLocalidad(LOCALIDAD_ID);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testEliminarLocalidad_NoExistente_DebeDevolver404NotFound() throws Exception {
        // ARRANGE
        doThrow(new ResourceNotFoundException("Localidad", "id", LOCALIDAD_ID))
                .when(localidadService).eliminarLocalidad(LOCALIDAD_ID);

        // ACT & ASSERT
        mockMvc.perform(delete(API_BASE + "/{id}", LOCALIDAD_ID)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}