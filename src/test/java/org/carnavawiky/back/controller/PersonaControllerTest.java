package org.carnavawiky.back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.carnavawiky.back.config.FileStorageProperties;
import org.carnavawiky.back.config.SecurityConfig;
import org.carnavawiky.back.config.WebConfig;
import org.carnavawiky.back.dto.PageResponse;
import org.carnavawiky.back.dto.PersonaRequest;
import org.carnavawiky.back.dto.PersonaResponse;
import org.carnavawiky.back.repository.RoleRepository;
import org.carnavawiky.back.repository.UsuarioRepository;
import org.carnavawiky.back.security.JwtService;
import org.carnavawiky.back.service.PersonaService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)
@WebMvcTest(
        controllers = PersonaController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class),
        properties = {
                "file.upload.location=test-uploads",
                "app.security.seed-enabled=false"
        }
)
class PersonaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonaService personaService;

    // Mocks necesarios para el contexto de seguridad
    @MockBean private JwtService jwtService;
    @MockBean private UserDetailsService userDetailsService;
    @MockBean private UsuarioRepository usuarioRepository;
    @MockBean private RoleRepository roleRepository;
    @MockBean private PasswordEncoder passwordEncoder;
    @MockBean private FileStorageProperties fileStorageProperties;
    @MockBean private DataSource dataSource;

    private PersonaRequest personaRequest;
    private PersonaResponse personaResponse;

    @BeforeEach
    void setUp() {
        personaRequest = new PersonaRequest();
        personaRequest.setNombreReal("Juan Pérez");
        personaRequest.setApodo("El Juani");
        personaRequest.setLocalidadId(1L);

        personaResponse = new PersonaResponse();
        personaResponse.setId(1L);
        personaResponse.setNombreReal("Juan Pérez");
        personaResponse.setApodo("El Juani");
    }

    // =======================================================
    // 1. CREAR (POST)
    // =======================================================

    @Test
    @DisplayName("Admin puede crear una persona")
    @WithMockUser(roles = "ADMIN")
    void testCrearPersona_Ok() throws Exception {
        when(personaService.crearPersona(any(PersonaRequest.class))).thenReturn(personaResponse);

        mockMvc.perform(post("/api/personas")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personaRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombreReal").value("Juan Pérez"));
    }

    @Test
    @DisplayName("Usuario normal no puede crear una persona")
    @WithMockUser(roles = "USER")
    void testCrearPersona_Forbidden() throws Exception {
        mockMvc.perform(post("/api/personas")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personaRequest)))
                .andExpect(status().isForbidden());
    }

    // =======================================================
    // 2. LISTAR (GET)
    // =======================================================

    @Test
    @DisplayName("Cualquier usuario autenticado puede listar personas")
    @WithMockUser(roles = "USER")
    void testListarPersonas_Ok() throws Exception {
        PageResponse<PersonaResponse> pageResponse = PageResponse.<PersonaResponse>builder()
                .content(List.of(personaResponse))
                .totalElements(1L)
                .build();

        when(personaService.obtenerTodasPersonas(any(Pageable.class), any()))
                .thenReturn(pageResponse);

        mockMvc.perform(get("/api/personas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nombreReal").value("Juan Pérez"));
    }

    // =======================================================
    // 3. OBTENER POR ID (GET)
    // =======================================================

    @Test
    @DisplayName("Obtener persona por ID devuelve 200")
    @WithMockUser(roles = "USER")
    void testObtenerPorId_Ok() throws Exception {
        when(personaService.obtenerPersonaPorId(1L)).thenReturn(personaResponse);

        mockMvc.perform(get("/api/personas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    // =======================================================
    // 4. ACTUALIZAR (PUT)
    // =======================================================

    @Test
    @DisplayName("Admin puede actualizar una persona")
    @WithMockUser(roles = "ADMIN")
    void testActualizarPersona_Ok() throws Exception {
        when(personaService.actualizarPersona(eq(1L), any(PersonaRequest.class)))
                .thenReturn(personaResponse);

        mockMvc.perform(put("/api/personas/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personaRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreReal").value("Juan Pérez"));
    }

    // =======================================================
    // 5. ELIMINAR (DELETE)
    // =======================================================

    @Test
    @DisplayName("Admin puede eliminar una persona")
    @WithMockUser(roles = "ADMIN")
    void testEliminarPersona_Ok() throws Exception {
        mockMvc.perform(delete("/api/personas/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }


    // =======================================================
    // 6. PRUEBAS DE VALIDACIÓN (POST/PUT)
    // =======================================================

    @Test
    @DisplayName("Debe devolver 400 Bad Request si el nombreReal está vacío")
    @WithMockUser(roles = "ADMIN")
    void testCrearPersona_ValidationError() throws Exception {
        // Creamos un request inválido (sin nombre)
        PersonaRequest invalidRequest = new PersonaRequest();
        invalidRequest.setApodo("El mudo");
        invalidRequest.setLocalidadId(1L);

        mockMvc.perform(post("/api/personas")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    // =======================================================
    // 7. PRUEBAS DE RECURSO NO ENCONTRADO (GET/PUT/DELETE)
    // =======================================================

    @Test
    @DisplayName("Debe devolver 404 si la persona no existe al buscar por ID")
    @WithMockUser(roles = "USER")
    void testObtenerPorId_NotFound() throws Exception {
        // Simulamos que el servicio lanza la excepción personalizada
        when(personaService.obtenerPersonaPorId(99L))
                .thenThrow(new org.carnavawiky.back.exception.ResourceNotFoundException("Persona", "id", 99L));

        mockMvc.perform(get("/api/personas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Debe devolver 404 si se intenta eliminar una persona inexistente")
    @WithMockUser(roles = "ADMIN")
    void testEliminarPersona_NotFound() throws Exception {
        doThrow(new org.carnavawiky.back.exception.ResourceNotFoundException("Persona", "id", 99L))
                .when(personaService).eliminarPersona(99L);

        mockMvc.perform(delete("/api/personas/99")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    // =======================================================
    // 8. PRUEBAS DE BÚSQUEDA Y PAGINACIÓN (GET)
    // =======================================================

    @Test
    @DisplayName("Debe filtrar personas por nombre mediante el parámetro search")
    @WithMockUser(roles = "USER")
    void testListarPersonas_WithSearch() throws Exception {
        PageResponse<PersonaResponse> pageResponse = PageResponse.<PersonaResponse>builder()
                .content(List.of(personaResponse))
                .totalElements(1L)
                .build();

        // Verificamos que el servicio reciba el String de búsqueda "Juan"
        when(personaService.obtenerTodasPersonas(any(Pageable.class), eq("Juan")))
                .thenReturn(pageResponse);

        mockMvc.perform(get("/api/personas")
                        .param("search", "Juan")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nombreReal").value("Juan Pérez"));
    }


}