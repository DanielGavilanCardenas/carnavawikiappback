package org.carnavawiky.back.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextoTest {

    @Test
    void testGettersAndSetters() {
        Texto texto = new Texto();
        Long id = 1L;
        String key = "welcome_msg";
        String value = "Bienvenido";

        texto.setId(id);
        texto.setKey(key);
        texto.setValue(value);

        assertEquals(id, texto.getId());
        assertEquals(key, texto.getKey());
        assertEquals(value, texto.getValue());
    }

    @Test
    void testEqualsAndHashCode() {
        Texto t1 = new Texto();
        t1.setId(1L);
        t1.setKey("key1");
        t1.setValue("value1");

        Texto t2 = new Texto();
        t2.setId(1L);
        t2.setKey("key1");
        t2.setValue("value1");

        // Test igualdad básica
        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
        assertEquals(t1, t1); // Reflexivo

        // Test desigualdad con null y otro tipo
        assertNotEquals(null, t1);
        assertNotEquals("String", t1);

        // Test desigualdad por campos individuales
        Texto t3 = new Texto();
        t3.setId(2L); // ID diferente
        t3.setKey("key1");
        t3.setValue("value1");
        assertNotEquals(t1, t3);

        t3.setId(1L);
        t3.setKey("key2"); // Key diferente
        assertNotEquals(t1, t3);

        t3.setKey("key1");
        t3.setValue("value2"); // Value diferente
        assertNotEquals(t1, t3);
    }

    @Test
    void testToString() {
        Texto texto = new Texto();
        texto.setId(1L);
        texto.setKey("test_key");
        texto.setValue("test_value");

        String toString = texto.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("Texto"));
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("key=test_key"));
        assertTrue(toString.contains("value=test_value"));
    }

    @Test
    void testCanEqual() {
        Texto t1 = new Texto();
        Texto t2 = new Texto();

        assertTrue(t1.canEqual(t2));
        assertFalse(t1.canEqual(new Object()));
    }
}