package org.carnavawiky.back.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Entity
@Table(name = "premio", uniqueConstraints = {
        // Restricción 1: Un puesto/modalidad/edicion solo puede ser ocupado por una Agrupacion (Evita empates no deseados)
        @UniqueConstraint(columnNames = {"puesto", "edicion_id", "modalidad"}, name = "UK_PUESTO_EDICION_MODALIDAD"),
        // Restricción 2: Una Agrupacion solo puede tener un premio por edición (Evita que una agrupación gane dos premios)
        @UniqueConstraint(columnNames = {"agrupacion_id", "edicion_id"}, name = "UK_AGRUPACION_EDICION")
})
@NoArgsConstructor
@AllArgsConstructor
public class Premio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Puesto obtenido (1, 2, 3, etc.)
    @Min(value = 1, message = "El puesto debe ser un valor positivo.")
    @Column(nullable = false)
    private Integer puesto;

    // Modalidad premiada (ej: Comparsa, Chirigota)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Modalidad modalidad;

    // =======================================================
    // RELACIONES
    // =======================================================

    // Relación Muchos a Uno (N:1) con Agrupacion
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agrupacion_id", nullable = false)
    private Agrupacion agrupacion;

    // Relación Muchos a Uno (N:1) con Edicion
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "edicion_id", nullable = false)
    private Edicion edicion;
}