package com.inventarios.pc.inventarios_pc_be.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventarios.pc.inventarios_pc_be.entities.Rol;
import java.util.List;
import java.util.Optional;


@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
    
    //Metodo para buscar un rol por su nombre en la base de datos
    Optional<Rol> findByNombre(String nombre);
}
