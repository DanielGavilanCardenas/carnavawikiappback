package org.carnavawiky.back.dto.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private String message;
    private int status;
    private String error; // Ejemplo: NOT_FOUND, BAD_REQUEST, INTERNAL_SERVER_ERROR
    private Instant timestamp;

}