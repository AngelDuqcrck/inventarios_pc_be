package com.inventarios.pc.inventarios_pc_be.shared.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoComponenteDTO {
    private Integer id;

    private String nombre;

    private Boolean deleteFlag;
}
