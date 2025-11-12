package com.pccore.pccore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pccore.pccore.model.Usuarios;
import java.util.Optional;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuarios, Long> {
    
    // Obliga a usar orElseThrow para evitar errores de NullPointer
    Optional<Usuarios> findByEmail(String email);
}
