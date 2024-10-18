package com.inventarios.pc.inventarios_pc_be.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventarios.pc.inventarios_pc_be.entities.Computador;
import com.inventarios.pc.inventarios_pc_be.entities.HistorialDispositivo;
import com.inventarios.pc.inventarios_pc_be.entities.TipoDispositivo;

public interface HistorialDispositivoRepository extends JpaRepository<HistorialDispositivo, Integer> {
    
    boolean existsByComputadorAndDispositivoPC_TipoDispositivoAndFechaDesvinculacionIsNull(Computador computador, TipoDispositivo tipoDispositivo);

}
