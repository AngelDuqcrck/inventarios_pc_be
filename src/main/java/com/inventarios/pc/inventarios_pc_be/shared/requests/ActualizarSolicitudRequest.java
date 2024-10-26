package com.inventarios.pc.inventarios_pc_be.shared.requests;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActualizarSolicitudRequest {
    
    
    private String descripcion;
    
    
    private String titulo;
    
    private Integer computador;

    
    private Integer ubicacionOrigen;

    
    private Integer ubicacionDestino;

   
    private Integer dispositivoPC;
}
