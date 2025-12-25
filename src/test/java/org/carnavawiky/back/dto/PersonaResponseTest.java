package org.carnavawiky.back.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonaResponseTest {

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        PersonaResponse response = new PersonaResponse();
        
        response.setId(1L);
        response.setNombreReal("Juan Carlos");
        response.setApodo("Capitán");
        response.setLocalidadId(10L);
        response.setLocalidadNombre("Cádiz");
        response.setUsuarioId(20L);
        response.setUsuarioUsername("juancarlos");

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Juan Carlos", response.getNombreReal());
        assertEquals("Capitán", response.getApodo());
        assertEquals(10L, response.getLocalidadId());
        assertEquals("Cádiz", response.getLocalidadNombre());
        assertEquals(20L, response.getUsuarioId());
        assertEquals("juancarlos", response.getUsuarioUsername());
    }

    // =======================================================
    // TESTS PARA COBERTURA DE MÉTODOS DE LOMBOK (@Data)
    // =======================================================

    @Test
    @DisplayName("Debe cubrir todas las ramas de equals y hashCode variando cada campo")
    void testEqualsAndHashCode_Exhaustivo() {
        // Objeto base
        PersonaResponse r1 = new PersonaResponse();
        r1.setId(1L);
        r1.setNombreReal("Nombre");
        r1.setApodo("Apodo");
        r1.setLocalidadId(10L);
        r1.setLocalidadNombre("Loc");
        r1.setUsuarioId(20L);
        r1.setUsuarioUsername("User");

        // Copia exacta
        PersonaResponse r2 = new PersonaResponse();
        r2.setId(1L);
        r2.setNombreReal("Nombre");
        r2.setApodo("Apodo");
        r2.setLocalidadId(10L);
        r2.setLocalidadNombre("Loc");
        r2.setUsuarioId(20L);
        r2.setUsuarioUsername("User");

        // --- IGUALDAD BÁSICA ---
        assertEquals(r1, r1); // Reflexivo
        assertEquals(r1, r2); // Simétrico
        assertEquals(r2, r1);
        assertEquals(r1.hashCode(), r2.hashCode()); // HashCode consistente

        assertNotEquals(r1, null); // No igual a null
        assertNotEquals(r1, new Object()); // No igual a otro tipo

        // --- VARIACIONES CAMPO A CAMPO ---

        // 1. ID
        PersonaResponse rDiffId = copiar(r1); rDiffId.setId(2L);
        assertNotEquals(r1, rDiffId);
        assertNotEquals(r1.hashCode(), rDiffId.hashCode());
        
        PersonaResponse rIdNull = copiar(r1); rIdNull.setId(null);
        assertNotEquals(r1, rIdNull);
        assertNotEquals(rIdNull, r1);

        // 2. NombreReal
        PersonaResponse rDiffNom = copiar(r1); rDiffNom.setNombreReal("Otro");
        assertNotEquals(r1, rDiffNom);
        assertNotEquals(r1.hashCode(), rDiffNom.hashCode());
        
        PersonaResponse rNomNull = copiar(r1); rNomNull.setNombreReal(null);
        assertNotEquals(r1, rNomNull);
        assertNotEquals(rNomNull, r1);

        // 3. Apodo
        PersonaResponse rDiffApodo = copiar(r1); rDiffApodo.setApodo("Otro");
        assertNotEquals(r1, rDiffApodo);
        assertNotEquals(r1.hashCode(), rDiffApodo.hashCode());
        
        PersonaResponse rApodoNull = copiar(r1); rApodoNull.setApodo(null);
        assertNotEquals(r1, rApodoNull);
        assertNotEquals(rApodoNull, r1);

        // 4. LocalidadId
        PersonaResponse rDiffLocId = copiar(r1); rDiffLocId.setLocalidadId(99L);
        assertNotEquals(r1, rDiffLocId);
        assertNotEquals(r1.hashCode(), rDiffLocId.hashCode());
        
        PersonaResponse rLocIdNull = copiar(r1); rLocIdNull.setLocalidadId(null);
        assertNotEquals(r1, rLocIdNull);
        assertNotEquals(rLocIdNull, r1);

        // 5. LocalidadNombre
        PersonaResponse rDiffLocNom = copiar(r1); rDiffLocNom.setLocalidadNombre("Otro");
        assertNotEquals(r1, rDiffLocNom);
        assertNotEquals(r1.hashCode(), rDiffLocNom.hashCode());
        
        PersonaResponse rLocNomNull = copiar(r1); rLocNomNull.setLocalidadNombre(null);
        assertNotEquals(r1, rLocNomNull);
        assertNotEquals(rLocNomNull, r1);

        // 6. UsuarioId
        PersonaResponse rDiffUserId = copiar(r1); rDiffUserId.setUsuarioId(99L);
        assertNotEquals(r1, rDiffUserId);
        assertNotEquals(r1.hashCode(), rDiffUserId.hashCode());
        
        PersonaResponse rUserIdNull = copiar(r1); rUserIdNull.setUsuarioId(null);
        assertNotEquals(r1, rUserIdNull);
        assertNotEquals(rUserIdNull, r1);

        // 7. UsuarioUsername
        PersonaResponse rDiffUserNom = copiar(r1); rDiffUserNom.setUsuarioUsername("Otro");
        assertNotEquals(r1, rDiffUserNom);
        assertNotEquals(r1.hashCode(), rDiffUserNom.hashCode());
        
        PersonaResponse rUserNomNull = copiar(r1); rUserNomNull.setUsuarioUsername(null);
        assertNotEquals(r1, rUserNomNull);
        assertNotEquals(rUserNomNull, r1);
        
        // 8. UsuarioUsername Null vs Null
        PersonaResponse rUserNomNull2 = copiar(r1); rUserNomNull2.setUsuarioUsername(null);
        assertEquals(rUserNomNull, rUserNomNull2);
    }

    // Método auxiliar para clonar
    private PersonaResponse copiar(PersonaResponse original) {
        PersonaResponse copia = new PersonaResponse();
        copia.setId(original.getId());
        copia.setNombreReal(original.getNombreReal());
        copia.setApodo(original.getApodo());
        copia.setLocalidadId(original.getLocalidadId());
        copia.setLocalidadNombre(original.getLocalidadNombre());
        copia.setUsuarioId(original.getUsuarioId());
        copia.setUsuarioUsername(original.getUsuarioUsername());
        return copia;
    }

    @Test
    @DisplayName("Debe generar una representación en String")
    void testToString() {
        PersonaResponse response = new PersonaResponse();
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
        PersonaResponse r = new PersonaResponse();
        assertTrue(r.canEqual(new PersonaResponse()));
        assertFalse(r.canEqual(new Object()));
    }
}