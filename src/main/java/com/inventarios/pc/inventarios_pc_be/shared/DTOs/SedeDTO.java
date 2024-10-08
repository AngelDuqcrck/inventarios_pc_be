package com.inventarios.pc.inventarios_pc_be.shared.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SedeDTO {
    private Integer id;

    private String nombre;

    private String desc;
    
    private String direccion;

    private Boolean deleteFlag;
}
