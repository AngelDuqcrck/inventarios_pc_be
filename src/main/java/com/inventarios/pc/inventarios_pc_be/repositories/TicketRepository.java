package com.inventarios.pc.inventarios_pc_be.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventarios.pc.inventarios_pc_be.entities.Solicitudes;
import com.inventarios.pc.inventarios_pc_be.entities.Tickets;
import com.inventarios.pc.inventarios_pc_be.entities.Usuario;

import java.util.List;
import java.util.Optional;


public interface TicketRepository extends JpaRepository<Tickets, Integer> {
    
    List<Tickets> findByUsuario(Usuario usuario);

    Optional<Tickets> findBySolicitudesAndEstadoTicketsNombreNot(Solicitudes solicitud, String estadoNombre);
}
