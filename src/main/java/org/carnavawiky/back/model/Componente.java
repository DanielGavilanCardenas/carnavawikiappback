package org.carnavawiky.back.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Entity
@Table(name = "componente")
@NoArgsConstructor
@AllArgsConstructor
public class Componente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Rol dentro de la agrupación
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RolComponente rol;

    // =======================================================
    // RELACIONES
    // =======================================================

    // Relación Muchos a Uno (N:1) con Persona (el individuo)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "persona_id", nullable = false)
    private Persona persona;

    // Relación Muchos a Uno (N:1) con Agrupacion
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agrupacion_id", nullable = false)
    private Agrupacion agrupacion;
}