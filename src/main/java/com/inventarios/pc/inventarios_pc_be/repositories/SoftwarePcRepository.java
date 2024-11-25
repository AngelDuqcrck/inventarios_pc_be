package com.inventarios.pc.inventarios_pc_be.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventarios.pc.inventarios_pc_be.entities.Computador;
import com.inventarios.pc.inventarios_pc_be.entities.SoftwareCSA;
import com.inventarios.pc.inventarios_pc_be.entities.SoftwarePC;

public interface SoftwarePcRepository extends JpaRepository <SoftwarePC, Integer>{

    boolean existsByNombreIgnoreCaseAndVersion(String nombre, String version); 

    boolean existsByNombreIgnoreCaseAndVersionAndIdNot(String nombre, String version, Integer id);
}
