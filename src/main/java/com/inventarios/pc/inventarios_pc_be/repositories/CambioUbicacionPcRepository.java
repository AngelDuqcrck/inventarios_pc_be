package com.inventarios.pc.inventarios_pc_be.repositories;

import com.inventarios.pc.inventarios_pc_be.entities.Computador;
import com.inventarios.pc.inventarios_pc_be.entities.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventarios.pc.inventarios_pc_be.entities.CambioUbicacionPc;
import java.util.List;


@Repository
public interface CambioUbicacionPcRepository extends JpaRepository<CambioUbicacionPc, Integer>{
    
    CambioUbicacionPc findTopByComputadorAndAndUbicacionOrderByFechaIngresoDesc(Computador computador, Ubicacion ubicacion);

    CambioUbicacionPc findFirstByComputadorAndUbicacionAndFechaCambioIsNull(Computador computador, Ubicacion ubicacion);

    List<CambioUbicacionPc> findByUbicacion(Ubicacion ubicacion);

    List<CambioUbicacionPc> findByComputador(Computador computador);
}
