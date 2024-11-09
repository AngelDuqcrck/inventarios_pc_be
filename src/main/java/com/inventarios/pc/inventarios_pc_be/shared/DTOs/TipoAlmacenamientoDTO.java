package com.inventarios.pc.inventarios_pc_be.shared.DTOs;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TipoAlmacenamientoDTO {
    private Integer id;

    private String nombre;

    private Boolean deleteFlag;
}
