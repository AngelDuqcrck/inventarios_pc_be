package com.inventarios.pc.inventarios_pc_be.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventarios.pc.inventarios_pc_be.entities.EstadoDispositivo;
import java.util.List;
import java.util.Optional;


public interface EstadoDispositivoRepository extends JpaRepository<EstadoDispositivo, Integer> {
    
    Optional<EstadoDispositivo> findByNombre(String nombre);
}
