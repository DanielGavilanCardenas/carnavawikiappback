package org.carnavawiky.back.dto;

import org.carnavawiky.back.model.Modalidad;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AgrupacionResponseTest {

    @Test
    @DisplayName("Debe verificar que los getters y setters funcionan correctamente")
    void testGettersAndSetters() {
        // Arrange (Preparar)
        AgrupacionResponse response = new AgrupacionResponse();
        Long id = 1L;
        String nombre = "Los Miuras";
        String descripcion = "Chirigota mítica";
        LocalDateTime fechaAlta = LocalDateTime.now();
        String usuario = "admin";
        Integer anho = 1991;
        Modalidad modalidad = Modalidad.CHIRIGOTA;
        Long localidadId = 10L;
        String localidadNombre = "Cádiz";

        // Act (Actuar)
        response.setId(id);
        response.setNombre(nombre);
        response.setDescripcion(descripcion);
        response.setFechaAlta(fechaAlta);
        response.setNombreUsuarioCreador(usuario);
        response.setAnho(anho);
        response.setModalidad(modalidad);
        response.setLocalidadId(localidadId);
        response.setLocalidadNombre(localidadNombre);

        // Assert (Verificar)
        assertAll("Verificación de propiedades del DTO",
                () -> assertEquals(id, response.getId()),
                () -> assertEquals(nombre, response.getNombre()),
                () -> assertEquals(descripcion, response.getDescripcion()),
                () -> assertEquals(fechaAlta, response.getFechaAlta()),
                () -> assertEquals(usuario, response.getNombreUsuarioCreador()),
                () -> assertEquals(anho, response.getAnho()),
                () -> assertEquals(modalidad, response.getModalidad()),
                () -> assertEquals(localidadId, response.getLocalidadId()),
                () -> assertEquals(localidadNombre, response.getLocalidadNombre())
        );
    }

    @Test
    @DisplayName("Debe verificar la igualdad y el hashcode (Lombok @Data)")
    void testEqualsAndHashCode() {
        AgrupacionResponse res1 = new AgrupacionResponse();
        res1.setId(1L);
        res1.setNombre("Prueba");

        AgrupacionResponse res2 = new AgrupacionResponse();
        res2.setId(1L);
        res2.setNombre("Prueba");

        assertEquals(res1, res2, "Dos objetos con los mismos valores deben ser iguales");
        assertEquals(res1.hashCode(), res2.hashCode(), "Dos objetos iguales deben tener el mismo HashCode");
    }

    @Test
    @DisplayName("Debe verificar que el método toString contiene campos clave")
    void testToString() {
        AgrupacionResponse response = new AgrupacionResponse();
        response.setNombre("Test Agrupación");

        String toString = response.toString();

        assertTrue(toString.contains("Test Agrupación"), "El toString debe incluir el nombre");
        assertTrue(toString.contains("AgrupacionResponse"), "El toString debe incluir el nombre de la clase");
    }
}