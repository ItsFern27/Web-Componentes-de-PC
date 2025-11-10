package com.pccore.pccore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pccore.pccore.model.Categorias;

@Repository
public interface CategoriasRepository extends JpaRepository<Categorias, Long> {
    
}