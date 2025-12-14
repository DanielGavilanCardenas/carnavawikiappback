package org.carnavawiky.back.exception;

import org.carnavawiky.back.dto.ErrorDetails;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    // =======================================================
    // 1. MANEJO DE RECURSO NO ENCONTRADO (404 Not Found)
    // =======================================================
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(
            ResourceNotFoundException exception,
            WebRequest webRequest) {

        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                exception.getMessage(),
                webRequest.getDescription(false),
                HttpStatus.NOT_FOUND.value()
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    // =======================================================
    // 2. MANEJO DE PETICIONES MAL FORMADAS (400 Bad Request)
    // Se utiliza para errores de validación de Jakarta/Hibernate Validator (@Valid en DTOs)
    // =======================================================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleValidationExceptions(
            MethodArgumentNotValidException exception,
            WebRequest webRequest) {

        // Recopila los mensajes de error de todos los campos que fallaron
        String detailedMessage = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                "Error de validación de entrada: " + detailedMessage,
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST.value()
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    // =======================================================
    // 3. MANEJO DE CONFLICTO DE DATOS (409 Conflict)
    // Se utiliza principalmente para violaciones de unicidad (UNIQUE constraints) o NOT NULL
    // =======================================================
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDetails> handleDataIntegrityViolationException(
            DataIntegrityViolationException exception,
            WebRequest webRequest) {

        // Intenta extraer un mensaje más amigable
        String rootMessage = "Violación de la integridad de datos. Posiblemente clave duplicada o campo nulo obligatorio.";
        if (exception.getRootCause() != null) {
            rootMessage = exception.getRootCause().getMessage();
        }

        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                rootMessage,
                webRequest.getDescription(false),
                HttpStatus.CONFLICT.value()
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    // =======================================================
    // 4. MANEJO DE EXCEPCIÓN GENÉRICA (500 Internal Server Error)
    // =======================================================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(
            Exception exception,
            WebRequest webRequest) {

        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                "Ha ocurrido un error inesperado en el servidor.",
                webRequest.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}