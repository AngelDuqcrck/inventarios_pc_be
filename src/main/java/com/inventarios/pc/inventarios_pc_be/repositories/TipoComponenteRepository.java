package com.inventarios.pc.inventarios_pc_be.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventarios.pc.inventarios_pc_be.entities.TipoComponente;

public interface TipoComponenteRepository extends JpaRepository<TipoComponente, Integer> {
    
}