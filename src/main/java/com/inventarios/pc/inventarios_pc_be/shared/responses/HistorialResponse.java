package com.inventarios.pc.inventarios_pc_be.shared.responses;

import java.util.List;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistorialResponse {
    
    private Integer id;

    private String nombre;

    private List<HistorialDispositivosResponse> historialDispositivosResponses;
}
