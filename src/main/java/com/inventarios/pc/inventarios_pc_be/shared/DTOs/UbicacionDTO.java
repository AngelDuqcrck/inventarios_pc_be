package com.inventarios.pc.inventarios_pc_be.shared.DTOs;


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

    private Integer area;

    private Boolean deleteFlag;
}
