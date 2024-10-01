package com.inventarios.pc.inventarios_pc_be.shared.responses;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UbicacionResponse {
    private Integer id;

    private String nombre;

    private String desc;

    private String area;

    private Boolean deleteFlag;
}
