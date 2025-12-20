package org.carnavawiky.back.exception;

// Esta excepción se lanzará para errores de negocio del lado del cliente (ej: datos duplicados, login fallido)
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}