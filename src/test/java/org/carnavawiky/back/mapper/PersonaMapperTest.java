package org.carnavawiky.back.mapper;

import org.carnavawiky.back.dto.PersonaRequest;
import org.carnavawiky.back.dto.PersonaResponse;
import org.carnavawiky.back.model.Localidad;
import org.carnavawiky.back.model.Persona;
import org.carnavawiky.back.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {PersonaMapper.class})
class PersonaMapperTest {

    @Autowired
    private PersonaMapper personaMapper;

    private Persona persona;
    private Localidad localidad;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        // Preparar Localidad
        localidad = new Localidad();
        localidad.setId(10L);
        localidad.setNombre("Cádiz");

        // Preparar Usuario
        usuario = new Usuario();
        usuario.setId(5L);
        usuario.setUsername("carnavalero_test");

        // Preparar Entidad Persona
        persona = new Persona();
        persona.setId(1L);
        persona.setNombreReal("Juan Pérez");
        persona.setApodo("El Juani");
        persona.setOrigen(localidad);
        persona.setUsuario(usuario);
    }

    @Test
    @DisplayName("Debe mapear de Entidad a PersonaResponse con relaciones completas")
    void testToResponse() {
        // ACT
        PersonaResponse response = personaMapper.toResponse(persona);

        // ASSERT
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getNombreReal()).isEqualTo("Juan Pérez");
        assertThat(response.getApodo()).isEqualTo("El Juani");

        // Verificación de Localidad mapeada
        assertThat(response.getLocalidadId()).isEqualTo(10L);
        assertThat(response.getLocalidadNombre()).isEqualTo("Cádiz");

        // Verificación de Usuario mapeado
        assertThat(response.getUsuarioId()).isEqualTo(5L);
        assertThat(response.getUsuarioUsername()).isEqualTo("carnavalero_test");
    }

    @Test
    @DisplayName("Debe mapear de PersonaRequest a Entidad pasando Localidad y Usuario")
    void testToEntity() {
        // ARRANGE
        PersonaRequest request = new PersonaRequest();
        request.setNombreReal("Maria Garcia");
        request.setApodo("La Mari");
        // Nota: El request suele traer IDs, pero el mapper toEntity recibe los objetos ya buscados

        // ACT - Pasando los 3 argumentos requeridos por tu implementación
        Persona entity = personaMapper.toEntity(request, localidad, usuario);

        // ASSERT
        assertThat(entity).isNotNull();
        assertThat(entity.getNombreReal()).isEqualTo("Maria Garcia");
        assertThat(entity.getApodo()).isEqualTo("La Mari");
        assertThat(entity.getOrigen()).isEqualTo(localidad);
        assertThat(entity.getUsuario()).isEqualTo(usuario);
    }

    @Test
    @DisplayName("Debe mapear a Entidad correctamente cuando el Usuario es nulo")
    void testToEntity_NullUsuario() {
        // ARRANGE
        PersonaRequest request = new PersonaRequest();
        request.setNombreReal("Anonimo");

        // ACT
        Persona entity = personaMapper.toEntity(request, localidad, null);

        // ASSERT
        assertThat(entity.getUsuario()).isNull();
        assertThat(entity.getOrigen()).isNotNull();
    }
}