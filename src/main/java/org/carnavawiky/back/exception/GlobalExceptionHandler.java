package org.carnavawiky.back.exception;

import org.carnavawiky.back.dto.error.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    // Método para manejar errores de validación de DTOs (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationExceptions( // <--- CORRECCIÓN 1: ResponseEntity usa ErrorResponse
                                                                     MethodArgumentNotValidException ex, WebRequest request) {

        // Extraemos todos los errores de campo y los concatenamos
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        // Si no hay errores de campo específicos, usamos un mensaje genérico
        if (errors.isEmpty()) {
            errors = "Error de validación de datos de entrada.";
        }

        // Creamos la respuesta de error estructurada
        ErrorResponse errorResponse = new ErrorResponse( // <--- CORRECCIÓN 2: Se crea un objeto ErrorResponse
                errors,
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                Instant.now()
        );

        // Registramos el error en el log (opcional, pero recomendado)
        // logger.error("Error de Validación: " + errors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST); // <--- CORRECCIÓN 3: Devuelve ErrorResponse
    }


    /**
     * Maneja las excepciones BadRequestException (HTTP 400)
     * Utilizada para errores de negocio (ej: nombre de usuario ya existe)
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        logger.warn("Solicitud inválida: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                status.value(),
                status.getReasonPhrase(),
                Instant.now()
        );
        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * Maneja las excepciones ResourceNotFoundException (HTTP 404)
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        logger.warn("Recurso no encontrado: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                status.value(),
                status.getReasonPhrase(),
                Instant.now()
        );
        return new ResponseEntity<>(errorResponse, status);
    }



    /**
     * Maneja las excepciones relacionadas con el Refresh Token (HTTP 403 Forbidden)
     */
    @ExceptionHandler(TokenRefreshException.class)
    public ResponseEntity<ErrorResponse> handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        logger.warn("Excepción de Refresh Token: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                status.value(),
                status.getReasonPhrase(),
                Instant.now()
        );
        // Devolvemos el estado 403 FORBIDDEN
        return new ResponseEntity<>(errorResponse, status);
    }


    /**
     * Maneja excepciones genéricas no previstas (HTTP 500)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        // Registro del error en nivel ERROR (CRÍTICO)
        logger.error("Error Interno del Servidor: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = new ErrorResponse(
                "Ha ocurrido un error inesperado en el servidor.",
                status.value(),
                status.getReasonPhrase(),
                Instant.now()
        );
        return new ResponseEntity<>(errorResponse, status);
    }
}