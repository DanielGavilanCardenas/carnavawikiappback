package org.carnavawiky.back.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Usaremos EnumType.STRING para guardar el nombre completo del rol (ej. ROLE_ADMIN)
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private RoleName name;

    // Enum para definir los roles posibles en la aplicación
    public enum RoleName {
        ROLE_ADMIN,
        ROLE_ESPECIALISTO
    }
}