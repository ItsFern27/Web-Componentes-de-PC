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

	// Buscar Productos por Nombre con el parametro, contenido en cualquier parte del nombre, ignorando mayusculas y minusculas
	List<Productos> findByNombreContainingIgnoreCase(String nombre);
    
	// Buscar Productos que tengan al menos una Categoría cuyo nombre
	// contenga el parámetro, ignorando mayúsculas y minúsculas
	List<Productos> findByCategorias_Categoria_NombreContainingIgnoreCase(String nombreCategoria);

	List<Productos> findByNombreContainingIgnoreCaseAndCategorias_Categoria_NombreContainingIgnoreCase(String nombreProducto, String nombreCategoria);

	@Query("select p from Productos p left join fetch p.item where p.id = :id")
    java.util.Optional<Productos> findByIdWithItems(Long id);

}
