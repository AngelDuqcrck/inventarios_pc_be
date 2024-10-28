package com.inventarios.pc.inventarios_pc_be.shared.requests;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ObservacionRequest {
    
    private Integer ticketId;

    private String observacion;

}
