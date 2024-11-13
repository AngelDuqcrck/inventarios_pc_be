package com.inventarios.pc.inventarios_pc_be.shared.responses;

import java.util.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HojaVidaPcResponse {
    
    private Integer id;

    
    private String tipoPC;

    
    private String responsable;
    
    //private String resPrimerNombre;

    private String propietario;

    private String ubicacion;

    private String sede;

    private String area;

   
    private String nombre;


    private String modelo;

    private String serial;

    private String marca;

   
    private String procesador;

    
    private String ram;

   
    private String almacenamiento;

 
    private String tipoAlmacenamiento;

    
    private String tipoRam;

    
    private String placa;

    private String ipAsignada;

    
    private String estadoDispositivo;

    private List<SoftwareVinculadosResponse> softwareVinculados;

    private List<DispositivosVinculadosResponse> dispositivosVinculados;
}
