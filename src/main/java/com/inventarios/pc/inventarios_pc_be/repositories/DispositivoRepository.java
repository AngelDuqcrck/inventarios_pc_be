package com.inventarios.pc.inventarios_pc_be.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventarios.pc.inventarios_pc_be.entities.DispositivoPC;
import com.inventarios.pc.inventarios_pc_be.entities.EstadoDispositivo;

import java.util.List;
import com.inventarios.pc.inventarios_pc_be.entities.TipoDispositivo;


public interface DispositivoRepository extends JpaRepository<DispositivoPC, Integer> {
    
    List<DispositivoPC> findByTipoDispositivo(TipoDispositivo tipoDispositivo);
    List<DispositivoPC> findByTipoDispositivoAndEstadoDispositivo(TipoDispositivo tipoDispositivo, EstadoDispositivo estadoDispositivo);

    boolean existsByPlaca(String placa);
}
