package org.carnavawiky.back.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EdicionTest {

    private Edicion edicion;
    private Concurso concurso;

    @BeforeEach
    void setUp() {
        // Objeto de apoyo
        concurso = new Concurso();
        concurso.setId(1L);
        concurso.setNombre("COAC");

        // Entidad a probar
        edicion = new Edicion();
        edicion.setId(1L);
        edicion.setAnho(2024);
        edicion.setConcurso(concurso);
    }

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        assertNotNull(edicion);
        assertEquals(1L, edicion.getId());
        assertEquals(2024, edicion.getAnho());
        assertEquals(concurso, edicion.getConcurso());
    }

    @Test
    @DisplayName("Debe funcionar el constructor con todos los argumentos")
    void testAllArgsConstructor() {
        Edicion e = new Edicion(2L, 1999, concurso);
        
        assertEquals(2L, e.getId());
        assertEquals(1999, e.getAnho());
        assertEquals(concurso, e.getConcurso());
    }

    // =======================================================
    // TESTS PARA COBERTURA DE MÉTODOS DE LOMBOK (@Data)
    // =======================================================

    @Test
    @DisplayName("Debe cubrir todas las ramas de equals y hashCode variando cada campo")
    void testEqualsAndHashCode_Exhaustivo() {
        Concurso c1 = new Concurso(); c1.setId(1L);
        Concurso c2 = new Concurso(); c2.setId(2L);

        // Objeto base
        Edicion e1 = new Edicion(1L, 2024, c1);
        // Copia exacta
        Edicion e2 = new Edicion(1L, 2024, c1);

        // --- IGUALDAD BÁSICA ---
        assertEquals(e1, e1); // Reflexivo
        assertEquals(e1, e2); // Simétrico
        assertEquals(e2, e1);
        assertEquals(e1.hashCode(), e2.hashCode()); // HashCode consistente

        assertNotEquals(e1, null); // No igual a null
        assertNotEquals(e1, new Object()); // No igual a otro tipo

        // --- VARIACIONES CAMPO A CAMPO ---

        // 1. ID Diferente
        Edicion eDiffId = new Edicion(2L, 2024, c1);
        assertNotEquals(e1, eDiffId);
        assertNotEquals(e1.hashCode(), eDiffId.hashCode());

        // 2. ID Null vs No Null
        Edicion eIdNull = new Edicion(null, 2024, c1);
        assertNotEquals(e1, eIdNull);
        assertNotEquals(eIdNull, e1);

        // 3. Anho Diferente
        Edicion eDiffAnho = new Edicion(1L, 2025, c1);
        assertNotEquals(e1, eDiffAnho);
        assertNotEquals(e1.hashCode(), eDiffAnho.hashCode());

        // 4. Anho Null vs No Null
        Edicion eAnhoNull = new Edicion(1L, null, c1);
        assertNotEquals(e1, eAnhoNull);
        assertNotEquals(eAnhoNull, e1);

        // 5. Concurso Diferente
        Edicion eDiffConcurso = new Edicion(1L, 2024, c2);
        assertNotEquals(e1, eDiffConcurso);
        assertNotEquals(e1.hashCode(), eDiffConcurso.hashCode());

        // 6. Concurso Null vs No Null
        Edicion eConcursoNull = new Edicion(1L, 2024, null);
        assertNotEquals(e1, eConcursoNull);
        assertNotEquals(eConcursoNull, e1);
        
        // 7. Concurso Null vs Null (Iguales si el resto coincide)
        Edicion eConcursoNull2 = new Edicion(1L, 2024, null);
        assertEquals(eConcursoNull, eConcursoNull2);
    }

    @Test
    @DisplayName("Debe generar una representación en String")
    void testToString() {
        String edicionString = edicion.toString();
        assertNotNull(edicionString);
        assertTrue(edicionString.contains("anho=2024"));
        assertTrue(edicionString.contains("id=1"));
    }

    @Test
    @DisplayName("Debe cumplir con canEqual para la simetría de equals")
    void testCanEqual() {
        Edicion otraEdicion = new Edicion();
        assertTrue(edicion.canEqual(otraEdicion));
        assertFalse(edicion.canEqual(new Object()));
    }
}