package com.inventarios.pc.inventarios_pc_be.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventarios.pc.inventarios_pc_be.entities.EstadoSolicitudes;
import com.inventarios.pc.inventarios_pc_be.entities.Solicitudes;
import com.inventarios.pc.inventarios_pc_be.entities.Usuario;

import java.util.List;


public interface SolicitudRepository extends JpaRepository<Solicitudes, Integer> {
    
    List<Solicitudes> findByUsuario(Usuario usuario);

    List<Solicitudes> findByEstadoSolicitudes(EstadoSolicitudes estadoSolicitudes);;

    
}
