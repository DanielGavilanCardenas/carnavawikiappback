package org.carnavawiky.back.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class RefreshTokenTest {

    private RefreshToken refreshToken;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        // Objeto de apoyo
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("usuario_test");

        // Entidad a probar
        refreshToken = new RefreshToken();
        refreshToken.setId(10L);
        refreshToken.setToken("token-uuid-12345");
        refreshToken.setExpiryDate(Instant.now().plusSeconds(3600));
        refreshToken.setUsuario(usuario);
    }

    @Test
    @DisplayName("Debe establecer y recuperar todos los campos correctamente")
    void testGettersAndSetters() {
        assertNotNull(refreshToken);
        assertEquals(10L, refreshToken.getId());
        assertEquals("token-uuid-12345", refreshToken.getToken());
        assertNotNull(refreshToken.getExpiryDate());
        assertEquals(usuario, refreshToken.getUsuario());
    }

    // =======================================================
    // TESTS PARA COBERTURA DE MÉTODOS DE LOMBOK (@Data)
    // =======================================================

    @Test
    @DisplayName("Debe cubrir todas las ramas de equals y hashCode variando cada campo")
    void testEqualsAndHashCode_Exhaustivo() {
        Instant expiry = Instant.now();
        Usuario u1 = new Usuario(); u1.setId(1L);
        Usuario u2 = new Usuario(); u2.setId(2L);

        // Objeto base (Usamos setters porque no hay @AllArgsConstructor)
        RefreshToken rt1 = new RefreshToken();
        rt1.setId(1L);
        rt1.setToken("token-A");
        rt1.setExpiryDate(expiry);
        rt1.setUsuario(u1);

        // Copia exacta
        RefreshToken rt2 = new RefreshToken();
        rt2.setId(1L);
        rt2.setToken("token-A");
        rt2.setExpiryDate(expiry);
        rt2.setUsuario(u1);

        // --- IGUALDAD BÁSICA ---
        assertEquals(rt1, rt1); // Reflexivo
        assertEquals(rt1, rt2); // Simétrico
        assertEquals(rt2, rt1);
        assertEquals(rt1.hashCode(), rt2.hashCode()); // HashCode consistente

        assertNotEquals(rt1, null); // No igual a null
        assertNotEquals(rt1, new Object()); // No igual a otro tipo

        // --- VARIACIONES CAMPO A CAMPO ---

        // 1. ID Diferente
        RefreshToken rtDiffId = new RefreshToken();
        rtDiffId.setId(2L);
        rtDiffId.setToken("token-A");
        rtDiffId.setExpiryDate(expiry);
        rtDiffId.setUsuario(u1);
        assertNotEquals(rt1, rtDiffId);
        assertNotEquals(rt1.hashCode(), rtDiffId.hashCode());

        // 2. ID Null vs No Null
        RefreshToken rtIdNull = new RefreshToken();
        rtIdNull.setId(null);
        rtIdNull.setToken("token-A");
        rtIdNull.setExpiryDate(expiry);
        rtIdNull.setUsuario(u1);
        assertNotEquals(rt1, rtIdNull);
        assertNotEquals(rtIdNull, rt1);

        // 3. Token Diferente
        RefreshToken rtDiffToken = new RefreshToken();
        rtDiffToken.setId(1L);
        rtDiffToken.setToken("token-B");
        rtDiffToken.setExpiryDate(expiry);
        rtDiffToken.setUsuario(u1);
        assertNotEquals(rt1, rtDiffToken);
        assertNotEquals(rt1.hashCode(), rtDiffToken.hashCode());

        // 4. Token Null vs No Null
        RefreshToken rtTokenNull = new RefreshToken();
        rtTokenNull.setId(1L);
        rtTokenNull.setToken(null);
        rtTokenNull.setExpiryDate(expiry);
        rtTokenNull.setUsuario(u1);
        assertNotEquals(rt1, rtTokenNull);
        assertNotEquals(rtTokenNull, rt1);

        // 5. ExpiryDate Diferente
        RefreshToken rtDiffExpiry = new RefreshToken();
        rtDiffExpiry.setId(1L);
        rtDiffExpiry.setToken("token-A");
        rtDiffExpiry.setExpiryDate(expiry.plusSeconds(100));
        rtDiffExpiry.setUsuario(u1);
        assertNotEquals(rt1, rtDiffExpiry);
        assertNotEquals(rt1.hashCode(), rtDiffExpiry.hashCode());

        // 6. ExpiryDate Null vs No Null
        RefreshToken rtExpiryNull = new RefreshToken();
        rtExpiryNull.setId(1L);
        rtExpiryNull.setToken("token-A");
        rtExpiryNull.setExpiryDate(null);
        rtExpiryNull.setUsuario(u1);
        assertNotEquals(rt1, rtExpiryNull);
        assertNotEquals(rtExpiryNull, rt1);

        // 7. Usuario Diferente
        RefreshToken rtDiffUser = new RefreshToken();
        rtDiffUser.setId(1L);
        rtDiffUser.setToken("token-A");
        rtDiffUser.setExpiryDate(expiry);
        rtDiffUser.setUsuario(u2);
        assertNotEquals(rt1, rtDiffUser);
        assertNotEquals(rt1.hashCode(), rtDiffUser.hashCode());

        // 8. Usuario Null vs No Null
        RefreshToken rtUserNull = new RefreshToken();
        rtUserNull.setId(1L);
        rtUserNull.setToken("token-A");
        rtUserNull.setExpiryDate(expiry);
        rtUserNull.setUsuario(null);
        assertNotEquals(rt1, rtUserNull);
        assertNotEquals(rtUserNull, rt1);
        
        // 9. Usuario Null vs Null (Iguales si el resto coincide)
        RefreshToken rtUserNull2 = new RefreshToken();
        rtUserNull2.setId(1L);
        rtUserNull2.setToken("token-A");
        rtUserNull2.setExpiryDate(expiry);
        rtUserNull2.setUsuario(null);
        assertEquals(rtUserNull, rtUserNull2);
    }

    @Test
    @DisplayName("Debe generar una representación en String")
    void testToString() {
        String tokenString = refreshToken.toString();
        assertNotNull(tokenString);
        assertTrue(tokenString.contains("token=token-uuid-12345"));
        assertTrue(tokenString.contains("id=10"));
    }

    @Test
    @DisplayName("Debe cumplir con canEqual para la simetría de equals")
    void testCanEqual() {
        RefreshToken otroToken = new RefreshToken();
        assertTrue(refreshToken.canEqual(otroToken));
        assertFalse(refreshToken.canEqual(new Object()));
    }
}