package com.inventarios.pc.inventarios_pc_be.repositories;
import com.inventarios.pc.inventarios_pc_be.entities.Componente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComponenteRepository extends JpaRepository<Componente, Integer> {
    
}
