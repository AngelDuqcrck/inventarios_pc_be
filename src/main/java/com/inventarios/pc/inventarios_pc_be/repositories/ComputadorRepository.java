package com.inventarios.pc.inventarios_pc_be.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventarios.pc.inventarios_pc_be.entities.Computador;
import com.inventarios.pc.inventarios_pc_be.entities.Ubicacion;
import com.inventarios.pc.inventarios_pc_be.entities.Usuario;

import java.util.List;


public interface ComputadorRepository extends JpaRepository<Computador, Integer> {
    

    List<Computador> findByUbicacion(Ubicacion ubicacion);

    List<Computador> findByResponsable(Usuario responsable);
    
}
