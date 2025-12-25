package org.carnavawiky.back.dto;

import org.carnavawiky.back.model.Modalidad;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AgrupacionRequestTest {

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        AgrupacionRequest request = new AgrupacionRequest();
        
        request.setNombre("Los Piratas");
        request.setAnho(1998);
        request.setDescripcion("Comparsa");
        request.setModalidad(Modalidad.COMPARSA);
        request.setLocalidadId(10L);

        assertNotNull(request);
        assertEquals("Los Piratas", request.getNombre());
        assertEquals(1998, request.getAnho());
        assertEquals("Comparsa", request.getDescripcion());
        assertEquals(Modalidad.COMPARSA, request.getModalidad());
        assertEquals(10L, request.getLocalidadId());
    }

    // =======================================================
    // TESTS PARA COBERTURA DE MÉTODOS DE LOMBOK (@Data)
    // =======================================================

    @Test
    @DisplayName("Debe cubrir todas las ramas de equals y hashCode variando cada campo")
    void testEqualsAndHashCode_Exhaustivo() {
        // Objeto base
        AgrupacionRequest r1 = new AgrupacionRequest();
        r1.setNombre("Nombre");
        r1.setAnho(2000);
        r1.setDescripcion("Desc");
        r1.setModalidad(Modalidad.CHIRIGOTA);
        r1.setLocalidadId(10L);

        // Copia exacta
        AgrupacionRequest r2 = new AgrupacionRequest();
        r2.setNombre("Nombre");
        r2.setAnho(2000);
        r2.setDescripcion("Desc");
        r2.setModalidad(Modalidad.CHIRIGOTA);
        r2.setLocalidadId(10L);

        // --- IGUALDAD BÁSICA ---
        assertEquals(r1, r1); // Reflexivo
        assertEquals(r1, r2); // Simétrico
        assertEquals(r2, r1);
        assertEquals(r1.hashCode(), r2.hashCode()); // HashCode consistente

        assertNotEquals(r1, null); // No igual a null
        assertNotEquals(r1, new Object()); // No igual a otro tipo

        // --- VARIACIONES CAMPO A CAMPO ---

        // 1. Nombre
        AgrupacionRequest rDiffNombre = copiar(r1); rDiffNombre.setNombre("Otro");
        assertNotEquals(r1, rDiffNombre);
        assertNotEquals(r1.hashCode(), rDiffNombre.hashCode());
        
        AgrupacionRequest rNombreNull = copiar(r1); rNombreNull.setNombre(null);
        assertNotEquals(r1, rNombreNull);
        assertNotEquals(rNombreNull, r1);

        // 2. Anho
        AgrupacionRequest rDiffAnho = copiar(r1); rDiffAnho.setAnho(2025);
        assertNotEquals(r1, rDiffAnho);
        assertNotEquals(r1.hashCode(), rDiffAnho.hashCode());
        
        AgrupacionRequest rAnhoNull = copiar(r1); rAnhoNull.setAnho(null);
        assertNotEquals(r1, rAnhoNull);
        assertNotEquals(rAnhoNull, r1);

        // 3. Descripcion
        AgrupacionRequest rDiffDesc = copiar(r1); rDiffDesc.setDescripcion("Otra");
        assertNotEquals(r1, rDiffDesc);
        assertNotEquals(r1.hashCode(), rDiffDesc.hashCode());
        
        AgrupacionRequest rDescNull = copiar(r1); rDescNull.setDescripcion(null);
        assertNotEquals(r1, rDescNull);
        assertNotEquals(rDescNull, r1);

        // 4. Modalidad
        AgrupacionRequest rDiffMod = copiar(r1); rDiffMod.setModalidad(Modalidad.CORO);
        assertNotEquals(r1, rDiffMod);
        assertNotEquals(r1.hashCode(), rDiffMod.hashCode());
        
        AgrupacionRequest rModNull = copiar(r1); rModNull.setModalidad(null);
        assertNotEquals(r1, rModNull);
        assertNotEquals(rModNull, r1);

        // 5. LocalidadId
        AgrupacionRequest rDiffLoc = copiar(r1); rDiffLoc.setLocalidadId(99L);
        assertNotEquals(r1, rDiffLoc);
        assertNotEquals(r1.hashCode(), rDiffLoc.hashCode());
        
        AgrupacionRequest rLocNull = copiar(r1); rLocNull.setLocalidadId(null);
        assertNotEquals(r1, rLocNull);
        assertNotEquals(rLocNull, r1);
        
        // 6. LocalidadId Null vs Null
        AgrupacionRequest rLocNull2 = copiar(r1); rLocNull2.setLocalidadId(null);
        assertEquals(rLocNull, rLocNull2);
    }

    // Método auxiliar para clonar
    private AgrupacionRequest copiar(AgrupacionRequest original) {
        AgrupacionRequest copia = new AgrupacionRequest();
        copia.setNombre(original.getNombre());
        copia.setAnho(original.getAnho());
        copia.setDescripcion(original.getDescripcion());
        copia.setModalidad(original.getModalidad());
        copia.setLocalidadId(original.getLocalidadId());
        return copia;
    }

    @Test
    @DisplayName("Debe generar una representación en String")
    void testToString() {
        AgrupacionRequest request = new AgrupacionRequest();
        request.setNombre("Test");
        
        String s = request.toString();
        assertNotNull(s);
        assertTrue(s.contains("nombre=Test"));
    }

    @Test
    @DisplayName("Debe cumplir con canEqual para la simetría de equals")
    void testCanEqual() {
        AgrupacionRequest r = new AgrupacionRequest();
        assertTrue(r.canEqual(new AgrupacionRequest()));
        assertFalse(r.canEqual(new Object()));
    }
}