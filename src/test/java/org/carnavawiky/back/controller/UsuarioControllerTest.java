package org.carnavawiky.back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.carnavawiky.back.config.FileStorageProperties;
import org.carnavawiky.back.config.SecurityConfig;
import org.carnavawiky.back.config.WebConfig;
import org.carnavawiky.back.dto.PageResponse;
import org.carnavawiky.back.dto.UsuarioRequest;
import org.carnavawiky.back.dto.UsuarioResponse;
import org.carnavawiky.back.exception.GlobalExceptionHandler;
import org.carnavawiky.back.repository.RoleRepository;
import org.carnavawiky.back.repository.UsuarioRepository;
import org.carnavawiky.back.security.JwtService;
import org.carnavawiky.back.service.UsuarioService;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import({SecurityConfig.class, GlobalExceptionHandler.class})
@WebMvcTest(
        controllers = UsuarioController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class),
        properties = {
                "file.upload.location=test-uploads",
                "app.security.seed-enabled=false"
        }
)
class UsuarioControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private DataSource dataSource;

    @MockBean
    private FileStorageProperties fileStorageProperties;

    private UsuarioResponse usuarioResponse;
    private UsuarioRequest usuarioRequest;

    @BeforeEach
    void setUp() {
        // Configuración crítica: Aplicamos springSecurity() y permitimos que el
        // GlobalExceptionHandler resuelva las excepciones antes de que MockMvc las lance como error 500
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        when(fileStorageProperties.getLocation()).thenReturn("test-uploads");

        usuarioResponse = new UsuarioResponse();
        usuarioResponse.setId(1L);
        usuarioResponse.setUsername("admin_test");
        usuarioResponse.setEmail("admin@test.com");
        usuarioResponse.setRoles(Set.of("ROLE_ADMIN"));

        usuarioRequest = new UsuarioRequest();
        usuarioRequest.setUsername("testuser");
        usuarioRequest.setEmail("test@carnavawiky.org");
        usuarioRequest.setPassword("Password123!");
        usuarioRequest.setEnabled(true);
        usuarioRequest.setRoleIds(Set.of(1L));
    }

    @Test
    @DisplayName("Debe devolver 403 Forbidden si el usuario no es ADMIN")
    @WithMockUser(roles = "USER")
    void testListarUsuarios_Forbidden() throws Exception {
        // Ejecutamos la petición. Si falla con 500 es porque la excepción no se traduce.
        // Forzamos el uso de CSRF para que la cadena de filtros sea completa.
        mockMvc.perform(get("/api/usuarios")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Admin puede listar usuarios")
    @WithMockUser(roles = "ADMIN")
    void testListarUsuarios_Ok() throws Exception {
        PageResponse<UsuarioResponse> pageResponse = PageResponse.<UsuarioResponse>builder()
                .content(List.of(usuarioResponse))
                .totalElements(1L)
                .build();

        when(usuarioService.obtenerTodosUsuarios(any(Pageable.class), any()))
                .thenReturn(pageResponse);

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].username").value("admin_test"));
    }

    @Test
    @DisplayName("Admin puede crear usuario")
    @WithMockUser(roles = "ADMIN")
    void testCrearUsuario_Ok() throws Exception {
        when(usuarioService.crearUsuario(any(UsuarioRequest.class))).thenReturn(usuarioResponse);

        mockMvc.perform(post("/api/usuarios")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Admin puede obtener usuario por ID")
    @WithMockUser(roles = "ADMIN")
    void testObtenerPorId_Ok() throws Exception {
        when(usuarioService.obtenerUsuarioPorId(1L)).thenReturn(usuarioResponse);

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("Admin puede actualizar usuario")
    @WithMockUser(roles = "ADMIN")
    void testActualizar_Ok() throws Exception {
        when(usuarioService.actualizarUsuario(eq(1L), any(UsuarioRequest.class)))
                .thenReturn(usuarioResponse);

        mockMvc.perform(put("/api/usuarios/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Admin puede eliminar usuario")
    @WithMockUser(roles = "ADMIN")
    void testEliminar_Ok() throws Exception {
        mockMvc.perform(delete("/api/usuarios/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}