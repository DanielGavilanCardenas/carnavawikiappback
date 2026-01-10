package org.carnavawiky.back.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.Set;

@Data
public class UsuarioRequest {

    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres.")
    private String username;

    // Usaremos un PasswordEncoder en el servicio para cifrar esto
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres.")
    private String password;

    @NotBlank(message = "El email es obligatorio.")
    @Email(message = "El formato del email es inválido.")
    private String email;

    @NotNull(message = "Debe especificar el ID de al menos un rol.")
    private Set<Long> roleIds; // IDs de los roles a asignar

    // El campo 'enabled' se puede gestionar aquí si se desea dar control al admin
    private Boolean enabled = true;
}