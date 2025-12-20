package org.carnavawiky.back.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comentario")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String contenido;

    @Column(nullable = true)
    private Integer puntuacion;

    // =======================================================
    // CAMPO DE MODERACIÓN AÑADIDO
    // =======================================================
    @Column(nullable = false)
    private Boolean aprobado = false; // Por defecto, requiere aprobación

    // =======================================================
    // AUDITORÍA
    // =======================================================
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    // =======================================================
    // RELACIONES
    // =======================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agrupacion_id", nullable = false)
    private Agrupacion agrupacion;
}