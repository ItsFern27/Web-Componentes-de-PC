package com.pccore.pccore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pccore.pccore.model.Ventas;

public interface VentasRepository extends JpaRepository<Ventas, Long> {
    
}
