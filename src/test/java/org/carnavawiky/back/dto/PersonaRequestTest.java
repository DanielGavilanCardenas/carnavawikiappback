package org.carnavawiky.back.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonaRequestTest {

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        PersonaRequest request = new PersonaRequest();
        
        request.setNombreReal("Juan Carlos");
        request.setApodo("Capitán");
        request.setLocalidadId(10L);
        request.setUsuarioId(20L);

        assertNotNull(request);
        assertEquals("Juan Carlos", request.getNombreReal());
        assertEquals("Capitán", request.getApodo());
        assertEquals(10L, request.getLocalidadId());
        assertEquals(20L, request.getUsuarioId());
    }

    // =======================================================
    // TESTS PARA COBERTURA DE MÉTODOS DE LOMBOK (@Data)
    // =======================================================

    @Test
    @DisplayName("Debe cubrir todas las ramas de equals y hashCode variando cada campo")
    void testEqualsAndHashCode_Exhaustivo() {
        // Objeto base
        PersonaRequest r1 = new PersonaRequest();
        r1.setNombreReal("Nombre");
        r1.setApodo("Apodo");
        r1.setLocalidadId(10L);
        r1.setUsuarioId(20L);

        // Copia exacta
        PersonaRequest r2 = new PersonaRequest();
        r2.setNombreReal("Nombre");
        r2.setApodo("Apodo");
        r2.setLocalidadId(10L);
        r2.setUsuarioId(20L);

        // --- IGUALDAD BÁSICA ---
        assertEquals(r1, r1); // Reflexivo
        assertEquals(r1, r2); // Simétrico
        assertEquals(r2, r1);
        assertEquals(r1.hashCode(), r2.hashCode()); // HashCode consistente

        assertNotEquals(r1, null); // No igual a null
        assertNotEquals(r1, new Object()); // No igual a otro tipo

        // --- VARIACIONES CAMPO A CAMPO ---

        // 1. NombreReal
        PersonaRequest rDiffNom = copiar(r1); rDiffNom.setNombreReal("Otro");
        assertNotEquals(r1, rDiffNom);
        assertNotEquals(r1.hashCode(), rDiffNom.hashCode());
        
        PersonaRequest rNomNull = copiar(r1); rNomNull.setNombreReal(null);
        assertNotEquals(r1, rNomNull);
        assertNotEquals(rNomNull, r1);

        // 2. Apodo
        PersonaRequest rDiffApodo = copiar(r1); rDiffApodo.setApodo("Otro");
        assertNotEquals(r1, rDiffApodo);
        assertNotEquals(r1.hashCode(), rDiffApodo.hashCode());
        
        PersonaRequest rApodoNull = copiar(r1); rApodoNull.setApodo(null);
        assertNotEquals(r1, rApodoNull);
        assertNotEquals(rApodoNull, r1);

        // 3. LocalidadId
        PersonaRequest rDiffLoc = copiar(r1); rDiffLoc.setLocalidadId(99L);
        assertNotEquals(r1, rDiffLoc);
        assertNotEquals(r1.hashCode(), rDiffLoc.hashCode());
        
        PersonaRequest rLocNull = copiar(r1); rLocNull.setLocalidadId(null);
        assertNotEquals(r1, rLocNull);
        assertNotEquals(rLocNull, r1);

        // 4. UsuarioId
        PersonaRequest rDiffUser = copiar(r1); rDiffUser.setUsuarioId(99L);
        assertNotEquals(r1, rDiffUser);
        assertNotEquals(r1.hashCode(), rDiffUser.hashCode());
        
        PersonaRequest rUserNull = copiar(r1); rUserNull.setUsuarioId(null);
        assertNotEquals(r1, rUserNull);
        assertNotEquals(rUserNull, r1);
        
        // 5. UsuarioId Null vs Null
        PersonaRequest rUserNull2 = copiar(r1); rUserNull2.setUsuarioId(null);
        assertEquals(rUserNull, rUserNull2);
    }

    // Método auxiliar para clonar
    private PersonaRequest copiar(PersonaRequest original) {
        PersonaRequest copia = new PersonaRequest();
        copia.setNombreReal(original.getNombreReal());
        copia.setApodo(original.getApodo());
        copia.setLocalidadId(original.getLocalidadId());
        copia.setUsuarioId(original.getUsuarioId());
        return copia;
    }

    @Test
    @DisplayName("Debe generar una representación en String")
    void testToString() {
        PersonaRequest request = new PersonaRequest();
        request.setNombreReal("Test");
        
        String s = request.toString();
        assertNotNull(s);
        assertTrue(s.contains("nombreReal=Test"));
    }

    @Test
    @DisplayName("Debe cumplir con canEqual para la simetría de equals")
    void testCanEqual() {
        PersonaRequest r = new PersonaRequest();
        assertTrue(r.canEqual(new PersonaRequest()));
        assertFalse(r.canEqual(new Object()));
    }
}