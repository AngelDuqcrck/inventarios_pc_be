package com.inventarios.pc.inventarios_pc_be.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventarios.pc.inventarios_pc_be.entities.Computador;
import com.inventarios.pc.inventarios_pc_be.entities.SoftwareCSA;
import com.inventarios.pc.inventarios_pc_be.entities.SoftwarePC;

public interface SoftwareCsaRepository extends JpaRepository<SoftwareCSA, Integer> {
    
    SoftwareCSA findByComputadorAndSoftwarePCAndFechaDesvinculacionIsNull(Computador computador, SoftwarePC softwarePC);

    boolean existsByComputadorAndSoftwarePCAndFechaDesvinculacionIsNull(Computador computador, SoftwarePC softwarePC);


}
