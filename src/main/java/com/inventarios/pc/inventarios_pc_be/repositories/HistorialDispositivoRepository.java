package com.inventarios.pc.inventarios_pc_be.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import com.inventarios.pc.inventarios_pc_be.entities.Computador;
import com.inventarios.pc.inventarios_pc_be.entities.DispositivoPC;
import com.inventarios.pc.inventarios_pc_be.entities.HistorialDispositivo;
import com.inventarios.pc.inventarios_pc_be.entities.TipoDispositivo;
import java.util.List;


public interface HistorialDispositivoRepository extends JpaRepository<HistorialDispositivo, Integer> {
    
    boolean existsByComputadorAndDispositivoPC_TipoDispositivoAndFechaDesvinculacionIsNull(Computador computador, TipoDispositivo tipoDispositivo);

    boolean existsByComputadorAndDispositivoPCAndFechaDesvinculacionIsNull(Computador computador, DispositivoPC dispositivoPC);

    boolean existsByDispositivoPCAndFechaDesvinculacionIsNull(DispositivoPC dispositivoPC);

    HistorialDispositivo findFirstByDispositivoPCAndFechaDesvinculacionIsNull(DispositivoPC dispositivoPC);

    HistorialDispositivo findByComputadorAndDispositivoPCAndFechaDesvinculacionIsNull(Computador computador, DispositivoPC dispositivoPC);

    HistorialDispositivo findFirstByComputadorAndDispositivoPC_TipoDispositivoAndFechaDesvinculacionIsNull(
    Computador computador, TipoDispositivo tipoDispositivo);

    List<HistorialDispositivo> findByComputador(Computador computador);

    HistorialDispositivo findTopByComputadorAndDispositivoPCOrderByFechaDesvinculacionDesc(Computador computador, DispositivoPC dispositivoPC);

    List<HistorialDispositivo> findByComputadorAndFechaDesvinculacionIsNull(Computador computador);
    
}
