package com.inventarios.pc.inventarios_pc_be.repositories;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

import com.inventarios.pc.inventarios_pc_be.entities.Computador;
import com.inventarios.pc.inventarios_pc_be.entities.SoftwareCSA;
import com.inventarios.pc.inventarios_pc_be.entities.SoftwarePC;
import java.util.List;
import java.util.Date;



public interface SoftwareCsaRepository extends JpaRepository<SoftwareCSA, Integer> {
    
    SoftwareCSA findByComputadorAndSoftwarePCAndFechaDesvinculacionIsNull(Computador computador, SoftwarePC softwarePC);

    boolean existsByComputadorAndSoftwarePCAndFechaDesvinculacionIsNull(Computador computador, SoftwarePC softwarePC);

    List<SoftwareCSA> findAllByComputadorAndFechaDesvinculacionIsNull(Computador computador);

    List<SoftwareCSA> findByComputador(Computador computador);

    List<SoftwareCSA> findByComputadorAndFechaDesvinculacionIsNull(Computador computador);

    SoftwareCSA findTopByComputadorAndSoftwarePCOrderByFechaDesvinculacionDesc(Computador computador, SoftwarePC softwarePC);

}
