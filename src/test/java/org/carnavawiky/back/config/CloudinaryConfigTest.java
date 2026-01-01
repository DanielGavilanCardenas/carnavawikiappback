package org.carnavawiky.back.config;

import com.cloudinary.Cloudinary;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CloudinaryConfig.class)
@TestPropertySource(properties = {
        "cloudinary.cloud_name=test_cloud",
        "cloudinary.api_key=123456789",
        "cloudinary.api_secret=abcdefg"
})
class CloudinaryConfigTest {

    @Autowired
    private Cloudinary cloudinary;

    @Test
    @DisplayName("Debe crear el bean de Cloudinary con las propiedades inyectadas")
    void testCloudinaryBeanCreation() {
        assertNotNull(cloudinary, "El bean de Cloudinary no debería ser nulo");
        
        // Verificamos que la configuración interna coincida con las propiedades de prueba
        assertEquals("test_cloud", cloudinary.config.cloudName);
        assertEquals("123456789", cloudinary.config.apiKey);
        assertEquals("abcdefg", cloudinary.config.apiSecret);
        assertTrue(cloudinary.config.secure, "La configuración secure debería ser true");
    }
}
