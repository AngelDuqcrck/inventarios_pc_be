package com.inventarios.pc.inventarios_pc_be.shared.DTOs;
import org.hibernate.validator.constraints.UniqueElements;

import com.inventarios.pc.inventarios_pc_be.entities.EstadoDispositivo;
import com.inventarios.pc.inventarios_pc_be.entities.Marca;
import com.inventarios.pc.inventarios_pc_be.entities.TipoDispositivo;


import lombok.*;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispositivoDTO {
    
    private Integer id;

    private TipoDispositivo tipoDispositivo;

    private String modelo;

    private EstadoDispositivo estadoDispositivo;
    
    private String placa;

    private Marca marca;
    
    private String serial;
   
    private String nombre;
}
