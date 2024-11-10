package com.inventarios.pc.inventarios_pc_be.shared.responses;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DispositivosVinculadosResponse {
    
    private Integer id;

    private String tipoDispositivo;

    private String nombre;

    private String modelo;

    private String serial;

    private String marca;

    private String placa;

}
