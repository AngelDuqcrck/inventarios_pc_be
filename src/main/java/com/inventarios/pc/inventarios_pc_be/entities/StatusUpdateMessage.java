package com.inventarios.pc.inventarios_pc_be.entities;

import java.time.LocalDateTime;
import java.util.Date;

import com.inventarios.pc.inventarios_pc_be.shared.DTOs.SolicitudDTO;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.TicketDTO;
import com.inventarios.pc.inventarios_pc_be.shared.responses.SolicitudesResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.TicketsResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusUpdateMessage {
    private String type;
    private Integer id;
    private String status;
    private Date date;
    private SolicitudesResponse solicitudes;
    private TicketsResponse tickets;
    private LocalDateTime timestamp;
}
