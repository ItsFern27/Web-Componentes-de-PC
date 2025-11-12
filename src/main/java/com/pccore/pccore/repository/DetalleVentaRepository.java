package com.pccore.pccore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pccore.pccore.model.DetalleVenta;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
    
}
