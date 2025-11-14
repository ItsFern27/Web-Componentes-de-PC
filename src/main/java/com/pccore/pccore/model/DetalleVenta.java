package com.pccore.pccore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "detalle_venta")
@Getter
@Setter
public class DetalleVenta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private double precio_venta;

    private double descuento;

    @Column(insertable = true, updatable = false)
    private double subtotal;

    // Relacion con Ventas
    @ManyToOne
    @JoinColumn(name = "id_venta")
    private Ventas venta;

    // Relacion con ItemProducto
    @ManyToOne
    @JoinColumn(name = "id_item", unique = true)
    private ItemProducto itemProducto;

}
