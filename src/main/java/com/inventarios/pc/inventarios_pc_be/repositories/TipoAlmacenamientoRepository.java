package com.inventarios.pc.inventarios_pc_be.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventarios.pc.inventarios_pc_be.entities.TipoAlmacenamiento;

public interface TipoAlmacenamientoRepository extends JpaRepository<TipoAlmacenamiento, Integer> {
    
}
