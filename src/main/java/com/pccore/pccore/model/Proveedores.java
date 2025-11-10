package com.pccore.pccore.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "proveedores")
@Getter
@Setter
public class Proveedores {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = true)
    private int telefono;

    @Column(nullable = true)
    private String email;

    // Relacion a ItemProducto
    @OneToMany(mappedBy = "proveedor") // <- nombre del atributo en Ventas
    private List<ItemProducto> item;

}
