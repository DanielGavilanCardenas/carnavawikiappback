package org.carnavawiky.back.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Entity
@Table(name = "concurso")
@NoArgsConstructor
@AllArgsConstructor
public class Concurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre del concurso (único, obligatorio)
    @Column(nullable = false, unique = true, length = 200)
    private String nombre;

    // Indica si el concurso está en curso o es histórico
    @Column(nullable = false)
    private Boolean estaActivo = true;

    // Relación Muchos a Uno (N:1) con Localidad
    @ManyToOne(fetch = FetchType.EAGER) // Se carga al obtener el concurso
    @JoinColumn(name = "localidad_id", nullable = false)
    private Localidad localidad;
}