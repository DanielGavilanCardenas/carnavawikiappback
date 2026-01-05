package org.carnavawiky.back.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "textos")
@Data
public class Texto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "clave_texto", unique = true, nullable = false)
    private String key;

    @Column(name ="value_texto", columnDefinition = "TEXT", nullable = false)
    private String value;
}