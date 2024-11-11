package com.inventarios.pc.inventarios_pc_be.shared.requests;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CambiarEstadoDispositivoRequest {
    
    private Integer dispositivoId;

    private Integer nuevoEstadoDispositivoId;

    private Integer computadorId;

    private String justificacion;

}
