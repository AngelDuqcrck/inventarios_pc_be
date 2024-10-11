package com.inventarios.pc.inventarios_pc_be.shared.responses;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SoftwareResponse {
    private Integer id;

    private String nombre;

    private String version;

    private String empresa;


    private Boolean deleteFlag;


    private String tipoSoftware;
}
