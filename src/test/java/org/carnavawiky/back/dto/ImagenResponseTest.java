package org.carnavawiky.back.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImagenResponseTest {

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        ImagenResponse response = new ImagenResponse();
        
        response.setId(1L);
        response.setNombreFichero("foto.jpg");
        response.setUrlPublica("http://localhost/foto.jpg");
        response.setEsPortada(true);
        response.setAgrupacionId(10L);
        response.setAgrupacionNombre("Los Piratas");

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("foto.jpg", response.getNombreFichero());
        assertEquals("http://localhost/foto.jpg", response.getUrlPublica());
        assertTrue(response.getEsPortada());
        assertEquals(10L, response.getAgrupacionId());
        assertEquals("Los Piratas", response.getAgrupacionNombre());
    }

    // =======================================================
    // TESTS PARA COBERTURA DE MÉTODOS DE LOMBOK (@Data)
    // =======================================================

    @Test
    @DisplayName("Debe cubrir todas las ramas de equals y hashCode variando cada campo")
    void testEqualsAndHashCode_Exhaustivo() {
        // Objeto base
        ImagenResponse r1 = new ImagenResponse();
        r1.setId(1L);
        r1.setNombreFichero("foto.jpg");
        r1.setUrlPublica("url");
        r1.setEsPortada(true);
        r1.setAgrupacionId(10L);
        r1.setAgrupacionNombre("Nombre");

        // Copia exacta
        ImagenResponse r2 = new ImagenResponse();
        r2.setId(1L);
        r2.setNombreFichero("foto.jpg");
        r2.setUrlPublica("url");
        r2.setEsPortada(true);
        r2.setAgrupacionId(10L);
        r2.setAgrupacionNombre("Nombre");

        // --- IGUALDAD BÁSICA ---
        assertEquals(r1, r1); // Reflexivo
        assertEquals(r1, r2); // Simétrico
        assertEquals(r2, r1);
        assertEquals(r1.hashCode(), r2.hashCode()); // HashCode consistente

        assertNotEquals(r1, null); // No igual a null
        assertNotEquals(r1, new Object()); // No igual a otro tipo

        // --- VARIACIONES CAMPO A CAMPO ---

        // 1. ID
        ImagenResponse rDiffId = copiar(r1); rDiffId.setId(2L);
        assertNotEquals(r1, rDiffId);
        assertNotEquals(r1.hashCode(), rDiffId.hashCode());
        
        ImagenResponse rIdNull = copiar(r1); rIdNull.setId(null);
        assertNotEquals(r1, rIdNull);
        assertNotEquals(rIdNull, r1);

        // 2. NombreFichero
        ImagenResponse rDiffNom = copiar(r1); rDiffNom.setNombreFichero("otro.jpg");
        assertNotEquals(r1, rDiffNom);
        assertNotEquals(r1.hashCode(), rDiffNom.hashCode());
        
        ImagenResponse rNomNull = copiar(r1); rNomNull.setNombreFichero(null);
        assertNotEquals(r1, rNomNull);
        assertNotEquals(rNomNull, r1);

        // 3. UrlPublica
        ImagenResponse rDiffUrl = copiar(r1); rDiffUrl.setUrlPublica("otraUrl");
        assertNotEquals(r1, rDiffUrl);
        assertNotEquals(r1.hashCode(), rDiffUrl.hashCode());
        
        ImagenResponse rUrlNull = copiar(r1); rUrlNull.setUrlPublica(null);
        assertNotEquals(r1, rUrlNull);
        assertNotEquals(rUrlNull, r1);

        // 4. EsPortada
        ImagenResponse rDiffPortada = copiar(r1); rDiffPortada.setEsPortada(false);
        assertNotEquals(r1, rDiffPortada);
        assertNotEquals(r1.hashCode(), rDiffPortada.hashCode());
        
        ImagenResponse rPortadaNull = copiar(r1); rPortadaNull.setEsPortada(null);
        assertNotEquals(r1, rPortadaNull);
        assertNotEquals(rPortadaNull, r1);

        // 5. AgrupacionId
        ImagenResponse rDiffAgrId = copiar(r1); rDiffAgrId.setAgrupacionId(99L);
        assertNotEquals(r1, rDiffAgrId);
        assertNotEquals(r1.hashCode(), rDiffAgrId.hashCode());
        
        ImagenResponse rAgrIdNull = copiar(r1); rAgrIdNull.setAgrupacionId(null);
        assertNotEquals(r1, rAgrIdNull);
        assertNotEquals(rAgrIdNull, r1);

        // 6. AgrupacionNombre
        ImagenResponse rDiffAgrNom = copiar(r1); rDiffAgrNom.setAgrupacionNombre("Otro");
        assertNotEquals(r1, rDiffAgrNom);
        assertNotEquals(r1.hashCode(), rDiffAgrNom.hashCode());
        
        ImagenResponse rAgrNomNull = copiar(r1); rAgrNomNull.setAgrupacionNombre(null);
        assertNotEquals(r1, rAgrNomNull);
        assertNotEquals(rAgrNomNull, r1);
        
        // 7. AgrupacionNombre Null vs Null
        ImagenResponse rAgrNomNull2 = copiar(r1); rAgrNomNull2.setAgrupacionNombre(null);
        assertEquals(rAgrNomNull, rAgrNomNull2);
    }

    // Método auxiliar para clonar
    private ImagenResponse copiar(ImagenResponse original) {
        ImagenResponse copia = new ImagenResponse();
        copia.setId(original.getId());
        copia.setNombreFichero(original.getNombreFichero());
        copia.setUrlPublica(original.getUrlPublica());
        copia.setEsPortada(original.getEsPortada());
        copia.setAgrupacionId(original.getAgrupacionId());
        copia.setAgrupacionNombre(original.getAgrupacionNombre());
        return copia;
    }

    @Test
    @DisplayName("Debe generar una representación en String")
    void testToString() {
        ImagenResponse response = new ImagenResponse();
        response.setId(1L);
        response.setNombreFichero("foto.jpg");
        
        String s = response.toString();
        assertNotNull(s);
        assertTrue(s.contains("id=1"));
        assertTrue(s.contains("nombreFichero=foto.jpg"));
    }

    @Test
    @DisplayName("Debe cumplir con canEqual para la simetría de equals")
    void testCanEqual() {
        ImagenResponse r = new ImagenResponse();
        assertTrue(r.canEqual(new ImagenResponse()));
        assertFalse(r.canEqual(new Object()));
    }
}