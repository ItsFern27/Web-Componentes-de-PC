package com.pccore.pccore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

import com.pccore.pccore.model.Productos;

public interface ProductosRepository extends JpaRepository<Productos, Long> {
	// Trae productos junto con sus items para poder calcular precios sin problemas de LAZY
	@Query("select distinct p from Productos p left join fetch p.item")
	List<Productos> findAllWithItems();

	List<Productos> findTop3ByOrderByIdDesc();
    
}
