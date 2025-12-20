package org.carnavawiky.back.exception;

// Esta excepción se lanzará cuando se solicite un recurso (ej: una Agrupación) que no existe.
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s no encontrada con %s : '%s'", resourceName, fieldName, fieldValue));
    }
}