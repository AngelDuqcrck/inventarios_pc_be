package com.inventarios.pc.inventarios_pc_be.shared.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EstadosDispositivoDTO {

    private int id;
    private String nombre;
    private boolean deleteFlag;
}
