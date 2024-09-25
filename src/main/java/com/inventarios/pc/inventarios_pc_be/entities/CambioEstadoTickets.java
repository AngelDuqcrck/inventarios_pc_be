package com.inventarios.pc.inventarios_pc_be.entities;

import java.util.*;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cambio_estado_tickets")
public class CambioEstadoTickets {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tickets_id")
    private Tickets tickets;

    @ManyToOne
    @JoinColumn(name = "estado_tickets_id")
    private EstadoTickets estadoTickets;


    private Date fecha_cambio;

}
