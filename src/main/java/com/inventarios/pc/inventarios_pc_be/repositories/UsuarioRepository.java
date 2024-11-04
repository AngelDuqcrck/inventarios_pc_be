package com.inventarios.pc.inventarios_pc_be.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventarios.pc.inventarios_pc_be.entities.Rol;
import com.inventarios.pc.inventarios_pc_be.entities.Ubicacion;
import com.inventarios.pc.inventarios_pc_be.entities.Usuario;
import java.util.List;



@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Metodo para buscar un usuario mediante su correo
    Optional<Usuario> findByCorreo(String correo);

    // Metodo para verificar si el usuario existe en la base de datos
    Boolean existsByCorreo(String correo);
    

    Optional<Usuario> findByIdAndRolId(Integer id, Rol rol);

    Optional<Usuario> findByCorreoAndRolId(String correo, Rol rol);

    List<Usuario> findByUbicacionId(Ubicacion ubicacionId);
}
