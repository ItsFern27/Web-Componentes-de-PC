package com.pccore.pccore.model;

import java.math.BigDecimal;

@Entity
@Table(name = "ventas")
@Getter
@Setter
public class Ventas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal total;

    @Column(insertable = true, updatable = false)
    private OffsetDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "id_cliente") // <- nombre de la columna en BD
    private Usuarios cliente;         // <- nombre del atributo en Java

    // Relacion con DetalleVenta
    @OneToMany(mappedBy = "venta")
    private List<DetalleVenta> detalleVenta;

}
