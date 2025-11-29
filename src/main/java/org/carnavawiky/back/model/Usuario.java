package org.carnavawiky.back.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "usuario")
@EntityListeners(AuditingEntityListener.class) // Necesario para la Auditoría
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password; // Contraseña cifrada

    @Column(nullable = false, unique = true)
    private String email;

    // Campos de la BBDD del usuario
    private boolean enabled = false; // Estado de activación (Activación por email)

    @Column(name = "activation_token")
    private String activationToken;  // Token para el proceso de activación

    // =======================================================
    // AUDITORÍA
    // =======================================================
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaAlta;

    // =======================================================
    // RELACIONES
    // =======================================================

    // Relación Muchos a Muchos (M:M) con Roles
    // Usamos FetchType.EAGER para asegurar que los roles se carguen al autenticar al usuario
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_roles",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    // Relación Uno a Uno (1:1) con Perfil (se implementará más adelante)
    // @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private Perfil perfil;

    // =======================================================
    // IMPLEMENTACIÓN UserDetails (Spring Security)
    // =======================================================

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Mapea los Roles a GrantedAuthority (ej: ROLE_ADMIN -> SimpleGrantedAuthority("ROLE_ADMIN"))
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return enabled; }
}