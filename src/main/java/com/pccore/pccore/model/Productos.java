package com.pccore.pccore.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "producto")
@Getter
@Setter
public class Productos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String marca;

    @Column(unique = true)
    private String modelo;
    
    @Column(nullable = true)
    private String descripcion;

    @Column(nullable = true)
    private String imagen;

    // Relaciones con ProductoCategoria
    @OneToMany(mappedBy = "producto")
    private Set<ProductoCategoria> categorias;

    // Relaciones con ItemProducto
    @OneToMany(mappedBy = "producto")
    private Set<ProductoCategoria> item;

}
