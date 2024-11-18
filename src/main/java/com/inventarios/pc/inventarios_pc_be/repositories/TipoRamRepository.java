package com.inventarios.pc.inventarios_pc_be.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventarios.pc.inventarios_pc_be.entities.TipoRam;

public interface TipoRamRepository extends JpaRepository<TipoRam, Integer> {
    boolean existsByNombre(String nombre);
}
