package org.carnavawiky.back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.carnavawiky.back.config.FileStorageProperties;
import org.carnavawiky.back.config.SecurityConfig;
import org.carnavawiky.back.config.WebConfig;
import org.carnavawiky.back.dto.ImagenRequest;
import org.carnavawiky.back.dto.ImagenResponse;
import org.carnavawiky.back.repository.RoleRepository;
import org.carnavawiky.back.repository.UsuarioRepository;
import org.carnavawiky.back.security.JwtService;
import org.carnavawiky.back.service.ImagenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = ImagenController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class)
})
@Import({SecurityConfig.class, FileStorageProperties.class})
public class ImagenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ImagenService imagenService;

    // Mocks de infraestructura de seguridad obligatorios (según PersonaControllerTest y otros)
    @MockBean
    private JwtService jwtService;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private UsuarioRepository usuarioRepository;
    @MockBean
    private RoleRepository roleRepository;

    private ImagenResponse imagenResponse;
    private ImagenRequest imagenRequest;

    @BeforeEach
    void setUp() {
        // Inicialización basada en ImagenRequest.java
        imagenRequest = new ImagenRequest();
        imagenRequest.setAgrupacionId(1L);
        imagenRequest.setEsPortada(true);

        // Inicialización basada en ImagenResponse.java
        imagenResponse = new ImagenResponse();
        imagenResponse.setId(10L);
        imagenResponse.setNombreFichero("carnaval_foto.jpg");
        imagenResponse.setUrlPublica("http://localhost:8080/api/imagenes/ficheros/carnaval_foto.jpg");
        imagenResponse.setEsPortada(true);
        imagenResponse.setAgrupacionId(1L);
        imagenResponse.setAgrupacionNombre("Comparsa de Cádiz");
    }



    @Test
    @DisplayName("POST /api/imagenes - ADMIN sube imagen exitosamente")
    @WithMockUser(roles = "ADMIN")
    void testSubirImagen_Admin_Ok() throws Exception {
        // 1. Definimos el archivo de imagen
        MockMultipartFile filePart = new MockMultipartFile(
                "file",               // Nombre del parámetro en el @RequestParam
                "test.png",
                MediaType.IMAGE_PNG_VALUE,
                "image-data".getBytes()
        );

        // 2. Definimos la parte 'data' como un String JSON (no como un archivo JSON)
        // Tu controlador espera un String, por lo que lo pasamos como un simple parámetro
        String jsonRequest = objectMapper.writeValueAsString(imagenRequest);

        when(imagenService.guardarImagen(any(), any(ImagenRequest.class))).thenReturn(imagenResponse);

        // Usamos multipart pero pasando 'data' como parámetro de formulario
        mockMvc.perform(multipart("/api/imagenes")
                        .file(filePart)
                        .param("data", jsonRequest) // Enviamos el JSON como String plano
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10L));
    }

    @Test
    @DisplayName("GET /api/imagenes/agrupacion/{id} - Lista imágenes de una agrupación")
    @WithMockUser(roles = "USER")
    void testListarPorAgrupacion_Ok() throws Exception {
        when(imagenService.obtenerImagenesPorAgrupacion(1L)).thenReturn(List.of(imagenResponse));

        mockMvc.perform(get("/api/imagenes/agrupacion/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].agrupacionId").value(1L));
    }

    @Test
    @DisplayName("GET /api/imagenes/ficheros/{name} - Descarga de recurso")
    @WithMockUser(roles = "USER")
    void testDownloadFile_Ok() throws Exception {
        Resource resource = new ByteArrayResource("contenido".getBytes()) {
            @Override
            public String getFilename() { return "carnaval_foto.jpg"; }
        };

        when(imagenService.cargarFicheroComoRecurso("carnaval_foto.jpg")).thenReturn(resource);

        mockMvc.perform(get("/api/imagenes/ficheros/carnaval_foto.jpg"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "inline; filename=\"carnaval_foto.jpg\""));
    }

    @Test
    @DisplayName("DELETE /api/imagenes/{id} - ADMIN elimina imagen")
    @WithMockUser(roles = "ADMIN")
    void testEliminarImagen_Admin_Ok() throws Exception {
        doNothing().when(imagenService).eliminarImagen(10L);

        mockMvc.perform(delete("/api/imagenes/10")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(imagenService, times(1)).eliminarImagen(10L);
    }

    @Test
    @DisplayName("DELETE /api/imagenes/{id} - USER recibe 403 Forbidden")
    @WithMockUser(roles = "USER")
    void testEliminarImagen_User_Forbidden() throws Exception {
        mockMvc.perform(delete("/api/imagenes/10")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }
}