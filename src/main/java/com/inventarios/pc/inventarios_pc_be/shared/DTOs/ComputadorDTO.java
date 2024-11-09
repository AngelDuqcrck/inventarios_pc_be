package com.inventarios.pc.inventarios_pc_be.shared.DTOs;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComputadorDTO {
    private Integer id;

    
    private Integer tipoPC;

    
    private Integer responsable;

    
    private Integer ubicacion;

   
    private String nombre;


    private String modelo;

    private String serial;

    private Integer marca;

   
    private Integer procesador;

    
    private Integer ram;

   
    private Integer almacenamiento;

 
    private Integer tipoAlmacenamiento;

    
    private Integer tipoRam;

    
    private String placa;

    private String ipAsignada;

    
    private Integer estadoDispositivo;

    private Integer propietario;

    private String justificacion; //Solo para actualizar un computador

    
}
