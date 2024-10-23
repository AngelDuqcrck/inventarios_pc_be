package com.inventarios.pc.inventarios_pc_be.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventarios.pc.inventarios_pc_be.entities.AreaPC;
import com.inventarios.pc.inventarios_pc_be.entities.Ubicacion;

public interface UbicacionRepository extends JpaRepository<Ubicacion, Integer> {

    public List<Ubicacion> findByArea(AreaPC area);
    
}
