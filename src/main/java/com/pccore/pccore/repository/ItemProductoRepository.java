package com.pccore.pccore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.pccore.pccore.model.ItemProducto;

public interface ItemProductoRepository extends JpaRepository<ItemProducto, Long>, JpaSpecificationExecutor<ItemProducto> {
    boolean existsByProductoId(Long productoId);
}
