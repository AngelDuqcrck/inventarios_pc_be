package com.inventarios.pc.inventarios_pc_be.shared.responses;

import java.util.Date;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistorialUbicaciones {
    
    private Integer id;

    private String nombre;

    private String sede;

    private String area;

    private Date fechaCambio;

    private Date fechaIngreso;

    private String justificacion;
}
