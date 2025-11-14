package com.pccore.pccore.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import jakarta.persistence.Transient;
import java.text.DecimalFormat;

@Entity
@Table(name = "productos")
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
    private Set<ItemProducto> item;

    @Transient
    public Double getPrecio() {
        if (item == null || item.isEmpty()) return null;
        return item.stream().mapToDouble(ItemProducto::getPrecio).min().orElse(0);
    }

    @Transient
    public String getPrecioStr() {
        Double p = getPrecio();
        if (p == null || p <= 0) return "";
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return "S/ " + df.format(p);
    }

}
