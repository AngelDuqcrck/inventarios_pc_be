package com.inventarios.pc.inventarios_pc_be.shared.responses;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DispositivosVinculadosResponse {
    
    private Integer id;

    private String nombre;

    private String tipoDispositivo;

}
