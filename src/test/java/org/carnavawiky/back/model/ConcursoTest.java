package org.carnavawiky.back.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConcursoTest {

    private Concurso concurso;
    private Localidad localidad;

    @BeforeEach
    void setUp() {
        // Objeto de apoyo
        localidad = new Localidad(1L, "Cádiz");

        // Entidad a probar
        concurso = new Concurso();
        concurso.setId(1L);
        concurso.setNombre("COAC");
        concurso.setEstaActivo(true);
        concurso.setLocalidad(localidad);
    }

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        assertNotNull(concurso);
        assertEquals(1L, concurso.getId());
        assertEquals("COAC", concurso.getNombre());
        assertTrue(concurso.getEstaActivo());
        assertEquals(localidad, concurso.getLocalidad());
    }

    @Test
    @DisplayName("Debe funcionar el constructor con todos los argumentos")
    void testAllArgsConstructor() {
        Concurso c = new Concurso(2L, "Concurso Sevilla", false, localidad);
        
        assertEquals(2L, c.getId());
        assertEquals("Concurso Sevilla", c.getNombre());
        assertFalse(c.getEstaActivo());
        assertEquals(localidad, c.getLocalidad());
    }

    @Test
    @DisplayName("Debe tener el campo 'estaActivo' como true por defecto")
    void testDefaultValues() {
        Concurso nuevoConcurso = new Concurso();
        assertNotNull(nuevoConcurso);
        // Lombok @Data respeta la inicialización del campo: private Boolean estaActivo = true;
        assertTrue(nuevoConcurso.getEstaActivo(), "El campo 'estaActivo' debería ser true por defecto.");
    }

    // =======================================================
    // TESTS PARA COBERTURA DE MÉTODOS DE LOMBOK (@Data)
    // =======================================================

    @Test
    @DisplayName("Debe cubrir todas las ramas de equals y hashCode variando cada campo")
    void testEqualsAndHashCode_Exhaustivo() {
        Localidad loc1 = new Localidad(1L, "Cádiz");
        Localidad loc2 = new Localidad(2L, "Sevilla");

        // Objeto base
        Concurso c1 = new Concurso(1L, "COAC", true, loc1);
        // Copia exacta
        Concurso c2 = new Concurso(1L, "COAC", true, loc1);

        // --- IGUALDAD BÁSICA ---
        assertEquals(c1, c1); // Reflexivo
        assertEquals(c1, c2); // Simétrico
        assertEquals(c2, c1);
        assertEquals(c1.hashCode(), c2.hashCode()); // HashCode consistente

        assertNotEquals(c1, null); // No igual a null
        assertNotEquals(c1, new Object()); // No igual a otro tipo

        // --- VARIACIONES CAMPO A CAMPO ---

        // 1. ID Diferente
        Concurso cDiffId = new Concurso(2L, "COAC", true, loc1);
        assertNotEquals(c1, cDiffId);
        assertNotEquals(c1.hashCode(), cDiffId.hashCode());

        // 2. ID Null vs No Null
        Concurso cIdNull = new Concurso(null, "COAC", true, loc1);
        assertNotEquals(c1, cIdNull);
        assertNotEquals(cIdNull, c1);

        // 3. Nombre Diferente
        Concurso cDiffNombre = new Concurso(1L, "Otro Concurso", true, loc1);
        assertNotEquals(c1, cDiffNombre);
        assertNotEquals(c1.hashCode(), cDiffNombre.hashCode());

        // 4. Nombre Null vs No Null
        Concurso cNombreNull = new Concurso(1L, null, true, loc1);
        assertNotEquals(c1, cNombreNull);
        assertNotEquals(cNombreNull, c1);

        // 5. EstaActivo Diferente
        Concurso cDiffActivo = new Concurso(1L, "COAC", false, loc1);
        assertNotEquals(c1, cDiffActivo);
        assertNotEquals(c1.hashCode(), cDiffActivo.hashCode());

        // 6. EstaActivo Null vs No Null
        Concurso cActivoNull = new Concurso(1L, "COAC", null, loc1);
        assertNotEquals(c1, cActivoNull);
        assertNotEquals(cActivoNull, c1);

        // 7. Localidad Diferente
        Concurso cDiffLoc = new Concurso(1L, "COAC", true, loc2);
        assertNotEquals(c1, cDiffLoc);
        assertNotEquals(c1.hashCode(), cDiffLoc.hashCode());

        // 8. Localidad Null vs No Null
        Concurso cLocNull = new Concurso(1L, "COAC", true, null);
        assertNotEquals(c1, cLocNull);
        assertNotEquals(cLocNull, c1);
        
        // 9. Localidad Null vs Null (Iguales si el resto coincide)
        Concurso cLocNull2 = new Concurso(1L, "COAC", true, null);
        assertEquals(cLocNull, cLocNull2);
    }

    @Test
    @DisplayName("Debe generar una representación en String")
    void testToString() {
        String concursoString = concurso.toString();
        assertNotNull(concursoString);
        assertTrue(concursoString.contains("nombre=COAC"));
        assertTrue(concursoString.contains("estaActivo=true"));
        assertTrue(concursoString.contains("id=1"));
    }

    @Test
    @DisplayName("Debe cumplir con canEqual para la simetría de equals")
    void testCanEqual() {
        Concurso otroConcurso = new Concurso();
        assertTrue(concurso.canEqual(otroConcurso));
        assertFalse(concurso.canEqual(new Object()));
    }
}