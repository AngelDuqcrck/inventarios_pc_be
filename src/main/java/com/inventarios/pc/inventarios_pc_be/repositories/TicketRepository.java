package com.inventarios.pc.inventarios_pc_be.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventarios.pc.inventarios_pc_be.entities.Tickets;
import com.inventarios.pc.inventarios_pc_be.entities.Usuario;

import java.util.List;


public interface TicketRepository extends JpaRepository<Tickets, Integer> {
    
    List<Tickets> findByUsuario(Usuario usuario);
}
