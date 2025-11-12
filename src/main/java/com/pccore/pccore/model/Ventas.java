package com.pccore.pccore.model;

import java.time.OffsetDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ventas")
@Getter
@Setter
public class Ventas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double total;

    @Column(insertable = true, updatable = false)
    private OffsetDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "id_cliente") // <- nombre de la columna en BD
    private Usuarios cliente;         // <- nombre del atributo en Java

    // Relacion con DetalleVenta
    @OneToMany(mappedBy = "venta")
    private List<DetalleVenta> detalleVenta;

}
