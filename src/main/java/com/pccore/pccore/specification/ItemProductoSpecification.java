package com.pccore.pccore.specification;

import com.pccore.pccore.model.ItemProducto;
import com.pccore.pccore.model.ProductoCategoria;
import com.pccore.pccore.model.Productos;
import com.pccore.pccore.model.Proveedores;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class ItemProductoSpecification {

    public static Specification<ItemProducto> findByCriteria(
            String productoNombre,
            String productoMarca,
            Long proveedorId,
            Long categoriaId,
            LocalDate fechaDesde,
            LocalDate fechaHasta,
            Boolean disponibleFilter) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<ItemProducto, Productos> productoJoin = root.join("producto");

            if (StringUtils.hasText(productoNombre)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(productoJoin.get("nombre")), "%" + productoNombre.toLowerCase() + "%"));
            }

            if (StringUtils.hasText(productoMarca)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(productoJoin.get("marca")), "%" + productoMarca.toLowerCase() + "%"));
            }

            if (proveedorId != null) {
                Join<ItemProducto, Proveedores> proveedorJoin = root.join("proveedor");
                predicates.add(criteriaBuilder.equal(proveedorJoin.get("id"), proveedorId));
            }

            if (categoriaId != null) {
                Join<Productos, ProductoCategoria> categoriaJoin = productoJoin.join("categorias");
                predicates.add(criteriaBuilder.equal(categoriaJoin.get("categoria").get("id"), categoriaId));
            }

            if (fechaDesde != null) {
                OffsetDateTime startOfDay = OffsetDateTime.of(fechaDesde, LocalTime.MIN, ZoneOffset.UTC);
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("fecha_ingreso"), startOfDay));
            }

            if (fechaHasta != null) {
                OffsetDateTime endOfDay = OffsetDateTime.of(fechaHasta, LocalTime.MAX, ZoneOffset.UTC);
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("fecha_ingreso"), endOfDay));
            }

            if (disponibleFilter != null && disponibleFilter) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("estado")), "disponible"));
            }
            
            query.distinct(true);

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
