package com.inventarios.pc.inventarios_pc_be.shared.responses;

import java.util.Date;

import lombok.*;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistorialDispositivosResponse {
    
    private Integer id;

    private String nombre;

    private String tipoDispositivo;

    private Date fechaCambio;

    private Date fechaDesvinculacion;
}
