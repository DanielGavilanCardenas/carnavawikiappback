package org.carnavawiky.back.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class FileStoragePropertiesTest {

    @Test
    @DisplayName("Debe permitir establecer y obtener la ubicación de almacenamiento")
    void testGetSetLocation() {
        // ARRANGE
        FileStorageProperties properties = new FileStorageProperties();
        String expectedLocation = "/var/www/carnavawiky/uploads";

        // ACT
        properties.setLocation(expectedLocation);
        String actualLocation = properties.getLocation();

        // ASSERT
        assertEquals(expectedLocation, actualLocation, "La ubicación recuperada debe ser igual a la establecida.");
    }

    @Test
    @DisplayName("La ubicación debe ser nula por defecto")
    void testDefaultLocationIsNull() {
        // ARRANGE
        FileStorageProperties properties = new FileStorageProperties();

        // ASSERT
        assertNull(properties.getLocation(), "La ubicación inicial debería ser nula.");
    }
}