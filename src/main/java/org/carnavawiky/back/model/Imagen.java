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

    // Nombre único generado (UUID) para el archivo en el disco
    @Column(nullable = false)
    private String nombreFichero;

    // Ruta completa en el sistema de archivos (ej: C:/devfiles/...)
    @Column(nullable = false)
    private String rutaAbsoluta;

    // URL que se devuelve al frontend (ej: /api/imagenes/nombre-archivo.jpg)
    @Column(nullable = false)
    private String urlPublica;

    @Column(nullable = false)
    private Boolean esPortada = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agrupacion_id", nullable = false)
    private Agrupacion agrupacion;
}