package org.carnavawiky.back.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UsuarioResponse {

    private Long id;
    private String username;
    private String email;
    private boolean enabled;
    private LocalDateTime fechaAlta;
    private Set<String> roles; // Nombres de los roles
}