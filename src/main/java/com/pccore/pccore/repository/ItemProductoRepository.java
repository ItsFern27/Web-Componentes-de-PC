package com.pccore.pccore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pccore.pccore.model.ItemProducto;

public interface ItemProductoRepository extends JpaRepository<ItemProducto, Long> {
    
}
