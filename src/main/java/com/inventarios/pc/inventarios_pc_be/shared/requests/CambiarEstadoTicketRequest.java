package com.inventarios.pc.inventarios_pc_be.shared.requests;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CambiarEstadoTicketRequest {
    private Integer ticketId;

    private Integer nuevoEstadoTicketId;

    private Boolean resuelto;

}
