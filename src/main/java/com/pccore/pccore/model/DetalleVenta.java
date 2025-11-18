package com.pccore.pccore.model;

import java.math.BigDecimal;

@Entity
@Table(name = "detalle_venta")
@Getter
@Setter
public class DetalleVenta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private BigDecimal precioVenta;

    private BigDecimal descuento;

    @Column(insertable = true, updatable = false)
    private BigDecimal subtotal;

    // Relacion con Ventas
    @ManyToOne
    @JoinColumn(name = "id_venta")
    private Ventas venta;

    // Relacion con ItemProducto
    @ManyToOne
    @JoinColumn(name = "id_item", unique = true)
    private ItemProducto itemProducto;

}
