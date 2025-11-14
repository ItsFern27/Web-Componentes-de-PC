package com.pccore.pccore.model;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "item_producto")
@Getter
@Setter
public class ItemProducto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double precio;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = true)
    private String numero_de_serie;

    @Column(nullable = true)
    private int porcentaje_descuento;

    @Column(insertable = true, updatable = true)
    private OffsetDateTime fecha_ingreso;

    // Relacion con producto
    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Productos producto;

    // Relacion con proveedor
    @ManyToOne
    @JoinColumn(name = "id_proveedor", nullable = false)
    private Proveedores proveedor;

    // Relacion a detalleVenta
    @OneToOne(mappedBy = "itemProducto")
    private DetalleVenta venta;

}
