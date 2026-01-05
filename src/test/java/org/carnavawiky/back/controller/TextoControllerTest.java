package org.carnavawiky.back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.carnavawiky.back.config.FileStorageProperties;
import org.carnavawiky.back.config.SecurityConfig;
import org.carnavawiky.back.config.WebConfig;
import org.carnavawiky.back.model.Texto;
import org.carnavawiky.back.repository.RoleRepository;
import org.carnavawiky.back.repository.UsuarioRepository;
import org.carnavawiky.back.security.JwtService;
import org.carnavawiky.back.service.TextoService;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = TextoController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {WebConfig.class})
})
@Import({SecurityConfig.class, JwtService.class})
class TextoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TextoService textoService;

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

    private Texto texto;

    @BeforeEach
    void setUp() {
        texto = new Texto();
        texto.setKey("welcome_message");
        texto.setValue("Bienvenido a CarnavaWiki");
    }

    // =======================================================
    // 1. OBTENER TODOS (GET)
    // =======================================================
    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Debe listar todos los textos")
    void testGetAll() throws Exception {
        List<Texto> textos = Arrays.asList(texto);
        when(textoService.findAll()).thenReturn(textos);

        mockMvc.perform(get("/api/textos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].key").value("welcome_message"))
                .andExpect(jsonPath("$[0].value").value("Bienvenido a CarnavaWiki"));
    }

    // =======================================================
    // 2. OBTENER POR KEY (GET)
    // =======================================================
    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Debe obtener un texto por su clave")
    void testGetByKey() throws Exception {
        when(textoService.findByKey("welcome_message")).thenReturn(texto);

        mockMvc.perform(get("/api/textos/welcome_message"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.key").value("welcome_message"))
                .andExpect(jsonPath("$.value").value("Bienvenido a CarnavaWiki"));
    }

    // =======================================================
    // 3. CREAR (POST)
    // =======================================================
    @Test
    @WithMockUser(roles = "ADMIN") // Asumimos que crear requiere rol ADMIN, aunque el controller no lo especifica explícitamente con @PreAuthorize, la config de seguridad podría requerirlo.
    @DisplayName("Debe crear un nuevo texto")
    void testCreate() throws Exception {
        when(textoService.save(any(Texto.class))).thenReturn(texto);

        mockMvc.perform(post("/api/textos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(texto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.key").value("welcome_message"));
    }

    // =======================================================
    // 4. ACTUALIZAR (PUT)
    // =======================================================
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debe actualizar un texto existente")
    void testUpdate() throws Exception {
        String newValue = "Nuevo mensaje de bienvenida";
        Texto updatedTexto = new Texto();
        updatedTexto.setKey("welcome_message");
        updatedTexto.setValue(newValue);

        when(textoService.update(eq("welcome_message"), eq(newValue))).thenReturn(updatedTexto);

        // Nota: El controlador espera @RequestBody String value, no un JSON.
        mockMvc.perform(put("/api/textos/welcome_message")
                        .with(csrf())
                        .contentType(MediaType.TEXT_PLAIN) // O APPLICATION_JSON dependiendo de cómo Spring lo maneje, pero String raw suele ser TEXT_PLAIN o JSON si viene entre comillas.
                        // Sin embargo, si el controller dice @RequestBody String, Spring intentará mapear el body.
                        // Si enviamos texto plano:
                        .content(newValue))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value(newValue));
    }
    
    // Test alternativo si el controlador espera JSON string
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debe actualizar un texto existente enviando JSON")
    void testUpdateJson() throws Exception {
        String newValue = "Nuevo mensaje";
        Texto updatedTexto = new Texto();
        updatedTexto.setKey("welcome_message");
        updatedTexto.setValue(newValue);

        when(textoService.update(eq("welcome_message"), eq(newValue))).thenReturn(updatedTexto);

        mockMvc.perform(put("/api/textos/welcome_message")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON) 
                        .content(newValue)) // Spring a veces trata String body como raw string incluso con app/json
                .andExpect(status().isOk());
    }

    // =======================================================
    // 5. ELIMINAR (DELETE)
    // =======================================================
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debe eliminar un texto por su clave")
    void testDelete() throws Exception {
        doNothing().when(textoService).delete("welcome_message");

        mockMvc.perform(delete("/api/textos/welcome_message")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}