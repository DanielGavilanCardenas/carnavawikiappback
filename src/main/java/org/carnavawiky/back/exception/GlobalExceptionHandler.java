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
    // 2. MANEJO DE ARCHIVO NO ENCONTRADO (404 Not Found) - NUEVO
    // =======================================================
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleFileNotFoundException(
            FileNotFoundException exception,
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
    // 3. MANEJO DE PETICIONES MAL FORMADAS (400 Bad Request)
    // Se utiliza para errores de validación de Jakarta/Hibernate Validator (@Valid en DTOs)
    // =======================================================

    // Cambia esto en GlobalExceptionHandler.java
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleMethodArgumentNotValidException( // Cambia el nombre y tipo
                                                                               MethodArgumentNotValidException exception,
                                                                               WebRequest webRequest) {

        String errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getField() + ": " + x.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                "Errores de validación: " + errors,
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST.value()
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    // =======================================================
    // 4. MANEJO DE CONFLICTO DE DATOS (409 Conflict)
    // Se utiliza principalmente para violaciones de unicidad (UNIQUE constraints) o NOT NULL
    // =======================================================
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDetails> handleDataIntegrityViolationException(
            DataIntegrityViolationException exception,
            WebRequest webRequest) {

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
    // 5. MANEJO DE FALLO DE ALMACENAMIENTO (500 Internal Server Error) - NUEVO
    // Se utiliza para errores de permisos, I/O al guardar, o fallo de disco.
    // =======================================================
    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ErrorDetails> handleFileStorageException(
            FileStorageException exception,
            WebRequest webRequest) {

        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                "Error de almacenamiento en el servidor: " + exception.getMessage(),
                webRequest.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // =======================================================
    // 6. MANEJO DE EXCEPCIÓN GENÉRICA (500 Internal Server Error)
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

    @ExceptionHandler(org.springframework.security.authorization.AuthorizationDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(Exception ex) {
        return new ResponseEntity<>("Acceso denegado: no tienes permisos suficientes", org.springframework.http.HttpStatus.FORBIDDEN);
    }

}