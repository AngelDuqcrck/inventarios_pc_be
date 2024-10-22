package com.inventarios.pc.inventarios_pc_be.shared.DTOs;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudDTO {
    
    private Integer id;

    
    private String descripcion;
    
    
    private String titulo;

    
    private Integer usuario;

    
    private Integer computador;

    
    private Integer tipoSolicitudes;

    
    private Integer estadoSolicitudes;

    
    private Integer ubicacionOrigen;

    
    private Integer ubicacionDestino;

   
    private Integer dispositivoPC;
}
