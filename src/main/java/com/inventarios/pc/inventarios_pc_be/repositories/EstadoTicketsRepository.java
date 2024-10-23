package com.inventarios.pc.inventarios_pc_be.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventarios.pc.inventarios_pc_be.entities.EstadoTickets;

public interface EstadoTicketsRepository extends JpaRepository<EstadoTickets, Integer>{

    Optional<EstadoTickets> findByNombre (String nombre);

}
