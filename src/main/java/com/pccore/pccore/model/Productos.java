package com.pccore.pccore.model;
import java.math.BigDecimal;

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
    public BigDecimal getPrecio() {
        if (item == null || item.isEmpty()) return BigDecimal.ZERO;
        return item.stream()
                   .map(ItemProducto::getPrecio)
                   .min(BigDecimal::compareTo)
                   .orElse(BigDecimal.ZERO);
    }

    @Transient
    public String getPrecioStr() {
        BigDecimal p = getPrecio();
        if (p == null || p.compareTo(BigDecimal.ZERO) <= 0) return "";
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return "S/ " + df.format(p);
    }

}
