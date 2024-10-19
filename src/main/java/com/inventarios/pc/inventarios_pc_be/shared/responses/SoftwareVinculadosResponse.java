package com.inventarios.pc.inventarios_pc_be.shared.responses;

import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SoftwareVinculadosResponse {
    
    private Integer id;

    private String nombre;

    private String version;
}
