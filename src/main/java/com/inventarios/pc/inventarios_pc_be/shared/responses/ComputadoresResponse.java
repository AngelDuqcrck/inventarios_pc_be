package com.inventarios.pc.inventarios_pc_be.shared.responses;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComputadoresResponse {
    
    private Integer id;

    private String tipoPC;
    
    private String responsable;

    private String primerNombreUser;

    private String sede;

    private String area;

    private String ubicacion;

    private String propietario;
   
    private String nombre;
    
    private String placa;

    private String modelo;

    private String serial;

    private String marca;
    
    private String estadoDispositivo;

    private String ipAsignada;

}
