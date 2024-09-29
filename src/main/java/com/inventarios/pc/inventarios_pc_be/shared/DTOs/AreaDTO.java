package com.inventarios.pc.inventarios_pc_be.shared.DTOs;

import com.inventarios.pc.inventarios_pc_be.entities.SedePC;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AreaDTO {
    
    private Integer id;

    private String nombre;

    private String desc;

    private SedePC sede;

    private Boolean deleteFlag;

}
