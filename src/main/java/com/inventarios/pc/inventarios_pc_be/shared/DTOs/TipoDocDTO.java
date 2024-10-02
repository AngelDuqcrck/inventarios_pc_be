package com.inventarios.pc.inventarios_pc_be.shared.DTOs;

import com.inventarios.pc.inventarios_pc_be.entities.SedePC;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoDocDTO {
    private Integer id;

    private String nombre;

    private Boolean deleteFlag;
}
