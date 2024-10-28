package com.inventarios.pc.inventarios_pc_be.shared.responses;

import java.util.Date;

import com.inventarios.pc.inventarios_pc_be.entities.Usuario;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketsResponse {
    private Integer id;

    private String nombre;

    private String usuario;

    private Date fechaCierre;

    private Date fecha_asig;

    private String estadoTicket;
}
