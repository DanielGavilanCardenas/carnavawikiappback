package org.carnavawiky.back.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "agrupacion")
@EntityListeners(AuditingEntityListener.class)
public class Agrupacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false)
    private Integer anho;

    @Column(nullable = false)
    private boolean oficial;

    @Column(length = 1000)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Modalidad modalidad;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaAlta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_creador_id", nullable = false)
    private Usuario usuarioCreador;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "localidad_id", nullable = false)
    private Localidad localidad;

    @OneToMany(mappedBy = "agrupacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Video> videos = new ArrayList<>();

    @OneToMany(mappedBy = "agrupacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Imagen> imagenes = new ArrayList<>();

    @OneToMany(mappedBy = "agrupacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Componente> componentes = new ArrayList<>();
}