package org.carnavawiky.back.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Entity
@Table(name = "persona")
@NoArgsConstructor
@AllArgsConstructor
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre real (obligatorio)
    @Column(nullable = false, length = 100)
    private String nombreReal;

    // Apodo/Nombre artístico (opcional)
    @Column(length = 100)
    private String apodo;

    // =======================================================
    // RELACIONES
    // =======================================================

    // Relación Muchos a Uno (N:1) con Localidad (origen de la persona)
    @ManyToOne(fetch = FetchType.EAGER) // EAGER ya que es un dato esencial
    @JoinColumn(name = "localidad_id", nullable = false)
    private Localidad origen;

    // Relación Uno a Uno (1:1) con Usuario (opcional)
    // El 'unique = true' asegura que una Persona solo se vincule a un Usuario.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", unique = true, nullable = true)
    private Usuario usuario;
}