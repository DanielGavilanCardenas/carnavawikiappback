package org.carnavawiky.back.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "agrupacion")
@EntityListeners(AuditingEntityListener.class) // Habilitar auditoría (fecha de alta)
public class Agrupacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre; // Nombre único de la agrupación

    @Column(length = 1000)
    private String descripcion;

    @Enumerated(EnumType.STRING) // Persiste el nombre del Enum como String en la BBDD
    @Column(nullable = false)
    private Modalidad modalidad;

    // =======================================================
    // AUDITORÍA
    // =======================================================

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaAlta;

    // =======================================================
    // RELACIONES
    // =======================================================

    // Relación Muchos a Uno (N:1) con el Usuario que la creó
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_creador_id", nullable = false)
    private Usuario usuarioCreador;

    // =======================================================
    // AÑADIDO: Localidad (N:1)
    // =======================================================
    @ManyToOne(fetch = FetchType.EAGER) // Se carga al obtener la Agrupación (preferible para catálogos pequeños)
    @JoinColumn(name = "localidad_id", nullable = false) // Columna de clave foránea
    private Localidad localidad;
}