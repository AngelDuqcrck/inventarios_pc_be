package com.inventarios.pc.inventarios_pc_be.shared.requests;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DispositivoRequest {

    // private TipoDispositivo tipoDispositivo;
 
     private Integer tipoDispositivo;
 
   
     private String modelo;
 
     // private EstadoDispositivo estadoDispositivo;
 
     private Integer estadoDispositivo;
 
     
     private String placa;
 
     // private Marca marca;
 
     private Integer marca;
 
     
     private String serial;
 
    private Integer propietario;   
     //private String nombre;
}
