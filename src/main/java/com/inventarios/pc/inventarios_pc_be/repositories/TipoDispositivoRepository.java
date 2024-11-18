package com.inventarios.pc.inventarios_pc_be.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventarios.pc.inventarios_pc_be.entities.TipoDispositivo;
import java.util.*;
public interface TipoDispositivoRepository extends JpaRepository<TipoDispositivo, Integer>{
    
    List<TipoDispositivo> findAllByDeleteFlagFalse();
    boolean existsByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Integer id);
}
