package org.carnavawiky.back.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Entity
// Restricción única: No puede haber dos ediciones del mismo concurso en el mismo año.
@Table(name = "edicion", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"anho", "concurso_id"}, name = "UK_EDICION_ANHO_CONCURSO")
})
@NoArgsConstructor
@AllArgsConstructor
public class Edicion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Año de la edición
    @Column(nullable = false)
    private Integer anho;

    // Relación Muchos a Uno (N:1) con Concurso
    @ManyToOne(fetch = FetchType.EAGER) // Se carga al obtener la edición
    @JoinColumn(name = "concurso_id", nullable = false)
    private Concurso concurso;
}