package com.inventarios.pc.inventarios_pc_be.shared.DTOs;

import java.util.Date;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketDTO {
    private Integer id;

    private String nombre;

    private Integer usuario;

    private String descripcion;

    private Integer solicitudes;

    private String observacion;

    
}
