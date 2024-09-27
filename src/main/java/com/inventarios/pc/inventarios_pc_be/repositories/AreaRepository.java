package com.inventarios.pc.inventarios_pc_be.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventarios.pc.inventarios_pc_be.entities.AreaPC;

public interface AreaRepository extends JpaRepository<AreaPC, Integer> {
    
}
