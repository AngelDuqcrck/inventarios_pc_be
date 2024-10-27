package com.inventarios.pc.inventarios_pc_be.shared.responses;

import java.util.Date;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketIdResponse {
    private Integer id;

    
    private String nombre;

    private Date fechaCierre;

   
    private String usuario;

    private Date fecha_asig;

    private String descripcion;


    private Integer solicitudId;
    
    private String solicitudes;

    private String observacion;


    private String estadoTickets;
}
