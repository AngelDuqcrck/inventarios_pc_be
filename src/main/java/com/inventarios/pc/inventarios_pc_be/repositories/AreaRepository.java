package com.inventarios.pc.inventarios_pc_be.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventarios.pc.inventarios_pc_be.entities.AreaPC;
import com.inventarios.pc.inventarios_pc_be.entities.SedePC;

public interface AreaRepository extends JpaRepository<AreaPC, Integer> {

    public List<AreaPC> findBySede(SedePC sede);
    
}
