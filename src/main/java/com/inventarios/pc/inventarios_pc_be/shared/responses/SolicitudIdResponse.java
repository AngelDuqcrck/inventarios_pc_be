package com.inventarios.pc.inventarios_pc_be.shared.responses;

import java.util.Date;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudIdResponse {
    
    private Integer id;

    
    private String descripcion;
    
    
    private String titulo;

    private Date fechaCreacion;

    private Date fechaCierre;
    
    private String usuario;

    
    private String computador;

    
    private String tipoSolicitudes;

    
    private String estadoSolicitudes;

    private String areaOrigen;

    private String sedeOrigen;

    private String ubicacionOrigen;

    private Integer ubicacionOrigenId;

    
    private String areaDestino;

    private String sedeDestino;

    private String ubicacionDestino;

    private Boolean esHardaware;

    private String softwarePC;

   
    private String dispositivoPC;
}
