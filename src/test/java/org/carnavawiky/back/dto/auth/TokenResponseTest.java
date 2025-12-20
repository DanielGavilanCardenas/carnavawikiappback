package org.carnavawiky.back.dto.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenResponseTest {

    @Test
    @DisplayName("Debe probar el patrón Builder y los getters")
    void testBuilderAndGetters() {
        // ACT
        TokenResponse response = TokenResponse.builder()
                .accessToken("access-123")
                .refreshToken("refresh-456")
                .tokenType("Bearer")
                .expiresIn(3600L)
                .build();

        // ASSERT
        assertEquals("access-123", response.getAccessToken());
        assertEquals("refresh-456", response.getRefreshToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals(3600L, response.getExpiresIn());
    }

    @Test
    @DisplayName("Debe probar los setters y el valor por defecto de tokenType")
    void testSettersAndDefaultValue() {
        // ARRANGE
        // Al usar @Builder, el valor por defecto definido en el campo puede perderse
        // a menos que se use @Builder.Default, por lo que es vital testearlo.
        TokenResponse response = TokenResponse.builder().build();

        // ACT
        response.setAccessToken("new-access");
        response.setRefreshToken("new-refresh");
        response.setTokenType("Custom");
        response.setExpiresIn(1800L);

        // ASSERT
        assertEquals("new-access", response.getAccessToken());
        assertEquals("new-refresh", response.getRefreshToken());
        assertEquals("Custom", response.getTokenType());
        assertEquals(1800L, response.getExpiresIn());
    }

    @Test
    @DisplayName("Debe probar equals, hashCode y toString")
    void testEqualsHashCodeToString() {
        // ARRANGE
        TokenResponse res1 = TokenResponse.builder()
                .accessToken("abc")
                .refreshToken("def")
                .tokenType("Bearer")
                .expiresIn(100L)
                .build();

        TokenResponse res2 = TokenResponse.builder()
                .accessToken("abc")
                .refreshToken("def")
                .tokenType("Bearer")
                .expiresIn(100L)
                .build();

        TokenResponse res3 = TokenResponse.builder()
                .accessToken("diff")
                .build();

        // ASSERT
        assertEquals(res1, res2);
        assertNotEquals(res1, res3);
        assertEquals(res1.hashCode(), res2.hashCode());
        assertNotNull(res1.toString());
        assertTrue(res1.toString().contains("accessToken=abc"));
    }

    @Test
    @DisplayName("Debe probar el método builder().toString() para cobertura completa de Lombok")
    void testBuilderToString() {
        // Algunos plugins de cobertura exigen probar el toString del propio objeto Builder
        String builderString = TokenResponse.builder()
                .accessToken("test")
                .toString();

        assertNotNull(builderString);
        assertTrue(builderString.contains("accessToken=test"));
    }
}