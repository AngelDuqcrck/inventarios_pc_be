package com.inventarios.pc.inventarios_pc_be.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventarios.pc.inventarios_pc_be.entities.DispositivoPC;

public interface DispositivoRepository extends JpaRepository<DispositivoPC, Integer> {
    
}
