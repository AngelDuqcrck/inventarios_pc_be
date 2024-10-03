package com.inventarios.pc.inventarios_pc_be.shared.DTOs;

import com.inventarios.pc.inventarios_pc_be.entities.TipoSoftware;

import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SoftwarePcDTO {

    private int id;
    
    private String nombre;

    private String version;

    private String empresa;


    private Boolean deleteFlag;
    

    private TipoSoftware tipoSoftware;
}
