package com.inventarios.pc.inventarios_pc_be.shared.responses;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DispositivoResponse {
    private Integer id;

    // private TipoDispositivo tipoDispositivo;
 
     private String tipoDispositivo;
 
   
     private String modelo;
 
     // private EstadoDispositivo estadoDispositivo;
 
     private String estadoDispositivo;
 
     
     private String placa;
 
     // private Marca marca;
 
     private String marca;
 
     
     private String serial;
 
    
     private String nombre;
}
