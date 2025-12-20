package org.carnavawiky.back.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Entity
@Table(name = "imagen")
@NoArgsConstructor
@AllArgsConstructor
public class Imagen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre único del fichero guardado en el servidor
    @Column(nullable = false)
    private String nombreFichero;

    // Ruta absoluta donde está almacenado el fichero en el sistema del servidor
    @Column(nullable = false)
    private String rutaAbsoluta;

    // URL pública para acceder al recurso estático (ej: /images/fichero.jpg)
    @Column(nullable = false)
    private String urlPublica;

    // Indicador si esta imagen es la portada principal de la Agrupación.
    @Column(nullable = false)
    private Boolean esPortada = false;

    // =======================================================
    // RELACIÓN
    // =======================================================

    // Relación Muchos a Uno (N:1) con Agrupacion
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agrupacion_id", nullable = false)
    private Agrupacion agrupacion;
}