package com.inventarios.pc.inventarios_pc_be.shared.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UbicarPcRequest {
    
    private Integer computadorId;
    
    private Integer ubicacionId;

    private Integer usuarioId;
}
