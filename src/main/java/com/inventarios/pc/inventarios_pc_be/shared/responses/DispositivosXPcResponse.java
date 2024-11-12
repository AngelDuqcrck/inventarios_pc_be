package com.inventarios.pc.inventarios_pc_be.shared.responses;

import java.util.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DispositivosXPcResponse {
    
    private Integer id;

    private String nombre;

    private String placa;

    private List<DispositivosVinculadosResponse> dispositivosVinculados;

}
