package org.carnavawiky.back.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PageResponseTest {

    @Test
    @DisplayName("Debe probar el patrón Builder y los getters")
    void testBuilderAndGetters() {
        // ARRANGE
        List<String> content = List.of("Elemento 1", "Elemento 2");

        // ACT
        PageResponse<String> response = PageResponse.<String>builder()
                .content(content)
                .pageNumber(0)
                .pageSize(10)
                .totalElements(2L)
                .totalPages(1)
                .isLast(true)
                .build();

        // ASSERT
        assertEquals(content, response.getContent());
        assertEquals(0, response.getPageNumber());
        assertEquals(10, response.getPageSize());
        assertEquals(2L, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        assertTrue(response.isLast());
    }

    @Test
    @DisplayName("Debe crear un PageResponse correctamente desde un objeto Page de Spring")
    void testFromPage() {
        // ARRANGE
        // Usamos un Mock para simular el comportamiento de Page de Spring Data
        Page<String> mockPage = Mockito.mock(Page.class);
        List<String> content = List.of("Item A", "Item B");

        when(mockPage.getContent()).thenReturn(content);
        when(mockPage.getNumber()).thenReturn(5);
        when(mockPage.getSize()).thenReturn(20);
        when(mockPage.getTotalElements()).thenReturn(100L);
        when(mockPage.getTotalPages()).thenReturn(5);
        when(mockPage.isLast()).thenReturn(false);

        // ACT
        PageResponse<String> response = PageResponse.fromPage(mockPage);

        // ASSERT
        assertNotNull(response);
        assertEquals(content, response.getContent());
        assertEquals(5, response.getPageNumber());
        assertEquals(20, response.getPageSize());
        assertEquals(100L, response.getTotalElements());
        assertEquals(5, response.getTotalPages());
        assertFalse(response.isLast());
    }

    @Test
    @DisplayName("Debe probar los setters (cobertura de @Data)")
    void testSetters() {
        // ARRANGE
        PageResponse<Integer> response = PageResponse.<Integer>builder().build();
        List<Integer> numbers = List.of(1, 2, 3);

        // ACT
        response.setContent(numbers);
        response.setPageNumber(1);
        response.setPageSize(5);
        response.setTotalElements(15L);
        response.setTotalPages(3);
        response.setLast(false);

        // ASSERT
        assertEquals(numbers, response.getContent());
        assertEquals(1, response.getPageNumber());
        assertEquals(3, response.getTotalPages());
        assertFalse(response.isLast());
    }

    @Test
    @DisplayName("Debe validar equals, hashCode y toString")
    void testEqualsHashCodeToString() {
        // ARRANGE
        PageResponse<String> res1 = PageResponse.<String>builder().pageNumber(1).build();
        PageResponse<String> res2 = PageResponse.<String>builder().pageNumber(1).build();
        PageResponse<String> res3 = PageResponse.<String>builder().pageNumber(2).build();

        // ASSERT
        assertEquals(res1, res2);
        assertNotEquals(res1, res3);
        assertEquals(res1.hashCode(), res2.hashCode());
        assertNotNull(res1.toString());
        assertTrue(res1.toString().contains("pageNumber=1"));
    }
}