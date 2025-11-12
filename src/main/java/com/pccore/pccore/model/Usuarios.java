package com.pccore.pccore.model;

import java.time.OffsetDateTime;
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
@Table(name = "usuarios")
@Getter
@Setter
public class Usuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(nullable = false)
    private String rol;
    
    @Column(insertable = true, updatable = false)
    private OffsetDateTime fecha_creacion;
    
    @OneToMany(mappedBy = "cliente") // <- nombre del atributo en Ventas
    private List<Ventas> compras;

}
