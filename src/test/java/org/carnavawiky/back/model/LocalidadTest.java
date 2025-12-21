package org.carnavawiky.back.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LocalidadTest {

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        // Entidad a probar
        Localidad localidad = new Localidad();

        // Establecer valores
        localidad.setId(1L);
        localidad.setNombre("Cádiz");

        // Verificar valores
        assertNotNull(localidad);
        assertEquals(1L, localidad.getId());
        assertEquals("Cádiz", localidad.getNombre());
    }

    @Test
    @DisplayName("Debe funcionar el constructor con todos los argumentos")
    void testAllArgsConstructor() {
        Localidad localidad = new Localidad(1L, "Sevilla");

        assertNotNull(localidad);
        assertEquals(1L, localidad.getId());
        assertEquals("Sevilla", localidad.getNombre());
    }
}