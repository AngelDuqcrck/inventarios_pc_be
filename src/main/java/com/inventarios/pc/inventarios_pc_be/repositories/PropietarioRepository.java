package com.inventarios.pc.inventarios_pc_be.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventarios.pc.inventarios_pc_be.entities.Propietario;

public interface PropietarioRepository extends JpaRepository <Propietario, Integer> {

    boolean existsByNombre (String nombre);

}
