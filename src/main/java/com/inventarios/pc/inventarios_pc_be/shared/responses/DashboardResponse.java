package com.inventarios.pc.inventarios_pc_be.shared.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardResponse {

    private String nombre1;
    private int cantidad1;

    private String nombre2;
    private int cantidad2;

    private String nombre3;
    private int cantidad3;

    private String nombre4;
    private int cantidad4;
    
    private String nombre5;
    private int cantidad5;

    private String nombre6;
    private int cantidad6;

    private String nombre7;
    private int cantidad7;

    private String nombre8;
    private int cantidad8;

    private String nombre9;
    private int cantidad9;
}
