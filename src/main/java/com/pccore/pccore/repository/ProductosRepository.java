package com.pccore.pccore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pccore.pccore.model.Productos;

public interface ProductosRepository extends JpaRepository<Productos, Long> {
    
}
