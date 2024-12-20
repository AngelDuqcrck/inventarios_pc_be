package com.inventarios.pc.inventarios_pc_be.shared.DTOs;
import com.inventarios.pc.inventarios_pc_be.entities.TipoComponente;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComponenteDTO {
    
    private int id;

    private String nombre;

    private String unidad;

    private Boolean deleteFlag;

    private Integer tipoComponente;
}
