package org.carnavawiky.back.exception;

import org.carnavawiky.back.dto.ErrorDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
        webRequest = mock(WebRequest.class);
        when(webRequest.getDescription(false)).thenReturn("uri=/api/test");
    }

    // 1. ResourceNotFoundException (404)
    @Test
    @DisplayName("Debe manejar ResourceNotFoundException")
    void handleResourceNotFoundExceptionTest() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Localidad", "id", 1L);
        ResponseEntity<ErrorDetails> response = globalExceptionHandler.handleResourceNotFoundException(ex, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(404, response.getBody().getStatus());
    }

    // 2. FileNotFoundException (404)
    @Test
    @DisplayName("Debe manejar FileNotFoundException")
    void handleFileNotFoundExceptionTest() {
        FileNotFoundException ex = new FileNotFoundException("Archivo no encontrado");
        ResponseEntity<ErrorDetails> response = globalExceptionHandler.handleFileNotFoundException(ex, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Archivo no encontrado", response.getBody().getMessage());
    }

    // 3. MethodArgumentNotValidException (400 - Validación de DTOs)
    @Test
    @DisplayName("Debe manejar errores de validación @Valid")
    void handleMethodArgumentNotValidExceptionTest2() {
        // Creamos un error de validación simulado
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "objectName");
        bindingResult.addError(new FieldError("objectName", "campo", "no puede ser nulo"));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(
                mock(MethodParameter.class), bindingResult);

        ResponseEntity<ErrorDetails> response = globalExceptionHandler.handleMethodArgumentNotValidException(ex, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("campo: no puede ser nulo"));
    }

    // 4. DataIntegrityViolationException (400 - Duplicados/Constraints)
    @Test
    @DisplayName("Debe manejar errores de validación @Valid (400)")
    void handleMethodArgumentNotValidExceptionTest() {
        // 1. Simular errores de validación
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "request");
        bindingResult.addError(new FieldError("request", "nombre", "no puede estar vacío"));

        // 2. Crear la excepción con el bindingResult
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(
                mock(org.springframework.core.MethodParameter.class), bindingResult);

        // 3. Llamar al método (Ahora el método existe y es público)
        ResponseEntity<ErrorDetails> response = globalExceptionHandler.handleMethodArgumentNotValidException(ex, webRequest);

        // 4. Verificaciones
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("nombre: no puede estar vacío"));
        assertEquals(400, response.getBody().getStatus());
    }

    // 5. FileStorageException (500)
    @Test
    @DisplayName("Debe manejar FileStorageException")
    void handleFileStorageExceptionTest() {
        FileStorageException ex = new FileStorageException("Error de disco");
        ResponseEntity<ErrorDetails> response = globalExceptionHandler.handleFileStorageException(ex, webRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("Error de almacenamiento"));
    }

    // 6. Exception genérica (500)
    @Test
    @DisplayName("Debe manejar excepciones inesperadas")
    void handleGlobalExceptionTest() {
        Exception ex = new RuntimeException("Crash");
        ResponseEntity<ErrorDetails> response = globalExceptionHandler.handleGlobalException(ex, webRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Ha ocurrido un error inesperado en el servidor.", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Debe manejar DataIntegrityViolationException devolviendo 409 Conflict")
    void handleDataIntegrityViolationExceptionTest() {
        // ARRANGE
        // Simulamos una causa raíz para probar la lógica de obtención del mensaje
        Throwable rootCause = new Throwable("Duplicate entry 'admin' for key 'users.username'");
        DataIntegrityViolationException ex = new DataIntegrityViolationException("Error de BD", rootCause);

        // ACT
        ResponseEntity<ErrorDetails> response = globalExceptionHandler.handleDataIntegrityViolationException(ex, webRequest);

        // ASSERT
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(409, response.getBody().getStatus());
        // Verificamos que el mensaje contenga la causa raíz detectada
        assertTrue(response.getBody().getMessage().contains("Duplicate entry"));
    }

}