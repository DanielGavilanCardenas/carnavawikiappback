package org.carnavawiky.back.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "videos")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String urlYoutube;
    private boolean verificado = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agrupacion_id")
    private Agrupacion agrupacion;
}