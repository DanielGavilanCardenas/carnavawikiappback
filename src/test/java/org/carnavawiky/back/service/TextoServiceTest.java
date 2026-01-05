package org.carnavawiky.back.service;

import org.carnavawiky.back.model.Texto;
import org.carnavawiky.back.repository.TextoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TextoServiceTest {

    @InjectMocks
    private TextoService textoService;

    @Mock
    private TextoRepository textoRepository;

    private Texto texto;

    @BeforeEach
    void setUp() {
        texto = new Texto();
        texto.setId(1L);
        texto.setKey("welcome_msg");
        texto.setValue("Bienvenido");
    }

    // =======================================================
    // 1. PRUEBA findAll
    // =======================================================
    @Test
    void testFindAll() {
        // Arrange
        when(textoRepository.findAll()).thenReturn(Arrays.asList(texto));

        // Act
        List<Texto> result = textoService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("welcome_msg", result.get(0).getKey());
        verify(textoRepository, times(1)).findAll();
    }

    // =======================================================
    // 2. PRUEBA findByKey
    // =======================================================
    @Test
    void testFindByKey_Success() {
        // Arrange
        when(textoRepository.findByKey("welcome_msg")).thenReturn(Optional.of(texto));

        // Act
        Texto result = textoService.findByKey("welcome_msg");

        // Assert
        assertNotNull(result);
        assertEquals("welcome_msg", result.getKey());
        verify(textoRepository, times(1)).findByKey("welcome_msg");
    }

    @Test
    void testFindByKey_NotFound() {
        // Arrange
        when(textoRepository.findByKey("unknown")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            textoService.findByKey("unknown");
        });
        
        assertTrue(exception.getMessage().contains("Key no encontrada"));
        verify(textoRepository, times(1)).findByKey("unknown");
    }

    // =======================================================
    // 3. PRUEBA save
    // =======================================================
    @Test
    void testSave() {
        // Arrange
        when(textoRepository.save(any(Texto.class))).thenReturn(texto);

        // Act
        Texto result = textoService.save(texto);

        // Assert
        assertNotNull(result);
        assertEquals("welcome_msg", result.getKey());
        verify(textoRepository, times(1)).save(texto);
    }

    // =======================================================
    // 4. PRUEBA update
    // =======================================================
    @Test
    void testUpdate_Success() {
        // Arrange
        String newValue = "Nuevo Mensaje";
        Texto updatedTexto = new Texto();
        updatedTexto.setId(1L);
        updatedTexto.setKey("welcome_msg");
        updatedTexto.setValue(newValue);

        when(textoRepository.findByKey("welcome_msg")).thenReturn(Optional.of(texto));
        when(textoRepository.save(any(Texto.class))).thenReturn(updatedTexto);

        // Act
        Texto result = textoService.update("welcome_msg", newValue);

        // Assert
        assertNotNull(result);
        assertEquals(newValue, result.getValue());
        
        // Verificar que se llamó a save con el valor actualizado
        verify(textoRepository, times(1)).save(argThat(t -> t.getValue().equals(newValue)));
    }

    @Test
    void testUpdate_NotFound() {
        // Arrange
        when(textoRepository.findByKey("unknown")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            textoService.update("unknown", "value");
        });
        
        verify(textoRepository, never()).save(any());
    }

    // =======================================================
    // 5. PRUEBA delete
    // =======================================================
    @Test
    void testDelete() {
        // Act
        textoService.delete("welcome_msg");

        // Assert
        verify(textoRepository, times(1)).deleteByKey("welcome_msg");
    }
}