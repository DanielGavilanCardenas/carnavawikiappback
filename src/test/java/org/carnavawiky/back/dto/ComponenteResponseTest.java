package org.carnavawiky.back.dto;

import org.carnavawiky.back.model.RolComponente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComponenteResponseTest {

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        ComponenteResponse response = new ComponenteResponse();
        
        response.setId(1L);
        response.setRol(RolComponente.DIRECTOR);
        response.setPersonaId(100L);
        response.setNombreArtistico("El Capitán");
        response.setNombreReal("Juan Carlos");
        response.setAgrupacionId(50L);
        response.setAgrupacionNombre("Los Yesterday");

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(RolComponente.DIRECTOR, response.getRol());
        assertEquals(100L, response.getPersonaId());
        assertEquals("El Capitán", response.getNombreArtistico());
        assertEquals("Juan Carlos", response.getNombreReal());
        assertEquals(50L, response.getAgrupacionId());
        assertEquals("Los Yesterday", response.getAgrupacionNombre());
    }

    // =======================================================
    // TESTS PARA COBERTURA DE MÉTODOS DE LOMBOK (@Data)
    // =======================================================

    @Test
    @DisplayName("Debe cubrir todas las ramas de equals y hashCode variando cada campo")
    void testEqualsAndHashCode_Exhaustivo() {
        // Objeto base
        ComponenteResponse r1 = new ComponenteResponse();
        r1.setId(1L);
        r1.setRol(RolComponente.DIRECTOR);
        r1.setPersonaId(100L);
        r1.setNombreArtistico("Apodo");
        r1.setNombreReal("Nombre");
        r1.setAgrupacionId(50L);
        r1.setAgrupacionNombre("Agrupacion");

        // Copia exacta
        ComponenteResponse r2 = new ComponenteResponse();
        r2.setId(1L);
        r2.setRol(RolComponente.DIRECTOR);
        r2.setPersonaId(100L);
        r2.setNombreArtistico("Apodo");
        r2.setNombreReal("Nombre");
        r2.setAgrupacionId(50L);
        r2.setAgrupacionNombre("Agrupacion");

        // --- IGUALDAD BÁSICA ---
        assertEquals(r1, r1); // Reflexivo
        assertEquals(r1, r2); // Simétrico
        assertEquals(r2, r1);
        assertEquals(r1.hashCode(), r2.hashCode()); // HashCode consistente

        assertNotEquals(r1, null); // No igual a null
        assertNotEquals(r1, new Object()); // No igual a otro tipo

        // --- VARIACIONES CAMPO A CAMPO ---

        // 1. ID
        ComponenteResponse rDiffId = copiar(r1); rDiffId.setId(2L);
        assertNotEquals(r1, rDiffId);
        assertNotEquals(r1.hashCode(), rDiffId.hashCode());
        
        ComponenteResponse rIdNull = copiar(r1); rIdNull.setId(null);
        assertNotEquals(r1, rIdNull);
        assertNotEquals(rIdNull, r1);

        // 2. Rol
        ComponenteResponse rDiffRol = copiar(r1); rDiffRol.setRol(RolComponente.CAJA);
        assertNotEquals(r1, rDiffRol);
        assertNotEquals(r1.hashCode(), rDiffRol.hashCode());
        
        ComponenteResponse rRolNull = copiar(r1); rRolNull.setRol(null);
        assertNotEquals(r1, rRolNull);
        assertNotEquals(rRolNull, r1);

        // 3. PersonaId
        ComponenteResponse rDiffPersId = copiar(r1); rDiffPersId.setPersonaId(99L);
        assertNotEquals(r1, rDiffPersId);
        assertNotEquals(r1.hashCode(), rDiffPersId.hashCode());
        
        ComponenteResponse rPersIdNull = copiar(r1); rPersIdNull.setPersonaId(null);
        assertNotEquals(r1, rPersIdNull);
        assertNotEquals(rPersIdNull, r1);

        // 4. NombreArtistico
        ComponenteResponse rDiffApodo = copiar(r1); rDiffApodo.setNombreArtistico("Otro");
        assertNotEquals(r1, rDiffApodo);
        assertNotEquals(r1.hashCode(), rDiffApodo.hashCode());
        
        ComponenteResponse rApodoNull = copiar(r1); rApodoNull.setNombreArtistico(null);
        assertNotEquals(r1, rApodoNull);
        assertNotEquals(rApodoNull, r1);

        // 5. NombreReal
        ComponenteResponse rDiffNombre = copiar(r1); rDiffNombre.setNombreReal("Otro");
        assertNotEquals(r1, rDiffNombre);
        assertNotEquals(r1.hashCode(), rDiffNombre.hashCode());
        
        ComponenteResponse rNombreNull = copiar(r1); rNombreNull.setNombreReal(null);
        assertNotEquals(r1, rNombreNull);
        assertNotEquals(rNombreNull, r1);

        // 6. AgrupacionId
        ComponenteResponse rDiffAgrId = copiar(r1); rDiffAgrId.setAgrupacionId(99L);
        assertNotEquals(r1, rDiffAgrId);
        assertNotEquals(r1.hashCode(), rDiffAgrId.hashCode());
        
        ComponenteResponse rAgrIdNull = copiar(r1); rAgrIdNull.setAgrupacionId(null);
        assertNotEquals(r1, rAgrIdNull);
        assertNotEquals(rAgrIdNull, r1);

        // 7. AgrupacionNombre
        ComponenteResponse rDiffAgrNom = copiar(r1); rDiffAgrNom.setAgrupacionNombre("Otro");
        assertNotEquals(r1, rDiffAgrNom);
        assertNotEquals(r1.hashCode(), rDiffAgrNom.hashCode());
        
        ComponenteResponse rAgrNomNull = copiar(r1); rAgrNomNull.setAgrupacionNombre(null);
        assertNotEquals(r1, rAgrNomNull);
        assertNotEquals(rAgrNomNull, r1);
        
        // 8. AgrupacionNombre Null vs Null
        ComponenteResponse rAgrNomNull2 = copiar(r1); rAgrNomNull2.setAgrupacionNombre(null);
        assertEquals(rAgrNomNull, rAgrNomNull2);
    }

    // Método auxiliar para clonar
    private ComponenteResponse copiar(ComponenteResponse original) {
        ComponenteResponse copia = new ComponenteResponse();
        copia.setId(original.getId());
        copia.setRol(original.getRol());
        copia.setPersonaId(original.getPersonaId());
        copia.setNombreArtistico(original.getNombreArtistico());
        copia.setNombreReal(original.getNombreReal());
        copia.setAgrupacionId(original.getAgrupacionId());
        copia.setAgrupacionNombre(original.getAgrupacionNombre());
        return copia;
    }

    @Test
    @DisplayName("Debe generar una representación en String")
    void testToString() {
        ComponenteResponse response = new ComponenteResponse();
        response.setId(1L);
        response.setNombreReal("Test");
        
        String s = response.toString();
        assertNotNull(s);
        assertTrue(s.contains("id=1"));
        assertTrue(s.contains("nombreReal=Test"));
    }

    @Test
    @DisplayName("Debe cumplir con canEqual para la simetría de equals")
    void testCanEqual() {
        ComponenteResponse r = new ComponenteResponse();
        assertTrue(r.canEqual(new ComponenteResponse()));
        assertFalse(r.canEqual(new Object()));
    }
}