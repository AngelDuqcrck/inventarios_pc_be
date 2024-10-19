package com.inventarios.pc.inventarios_pc_be.shared.responses;

import java.util.*;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SoftwareXPcResponse {
    
     private Integer id;

    private String nombre;

    private List<SoftwareVinculadosResponse> softwareVinculados;
}
