package com.inventarios.pc.inventarios_pc_be.shared.DTOs;

import com.inventarios.pc.inventarios_pc_be.entities.AreaPC;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UbicacionDTO {
    private Integer id;

    private String nombre;

    private String desc;

    private AreaPC area;

    private Boolean deleteFlag;
}
